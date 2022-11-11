package kr.co.bookand.backend.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.Role;
import kr.co.bookand.backend.account.domain.SocialType;
import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.account.domain.dto.AppleDto;
import kr.co.bookand.backend.account.domain.dto.TokenDto;
import kr.co.bookand.backend.account.exception.*;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.common.ApiService;
import kr.co.bookand.backend.common.Message;
import kr.co.bookand.backend.config.jwt.JwtException;
import kr.co.bookand.backend.config.jwt.RefreshToken;
import kr.co.bookand.backend.config.jwt.RefreshTokenRepository;
import kr.co.bookand.backend.config.jwt.TokenFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

import static kr.co.bookand.backend.account.domain.dto.AuthDto.*;
import static kr.co.bookand.backend.account.domain.dto.TokenDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenFactory tokenFactory;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final ApiService<MultiValueMap<String, String>> apiService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    ParameterizedTypeReference<Map<String, Object>> RESPONSE_TYPE  =  new ParameterizedTypeReference<>(){};

    private String suffix;

    @Transactional
    public TokenResponse socialAccess(AuthRequest authRequestDto) {
        String userId = getSocialIdWithAccessToken(authRequestDto).getUserId();
        String providerEmail = getSocialIdWithAccessToken(authRequestDto).getEmail();
        authRequestDto.insertId(userId);
        String email = authRequestDto.extraEmail();
        Optional<Account> account = accountRepository.findByEmail(email);
        SocialType socialType = authRequestDto.getSocialType();

        if (account.isPresent()) {
            // 로그인
            TokenDto tokenDto = login(account.get().toAccountRequestDto(suffix).toLoginRequest());
            return tokenDto.toTokenDto();

        }else {
            MiddleAccount middleAccount = MiddleAccount.builder()
                    .email(email)
                    .socialType(socialType)
                    .providerEmail(providerEmail)
                    .build();

            // 회원가입
            TokenDto tokenMessage = socialSignUp(middleAccount);
            return tokenMessage.toTokenDto();
        }
    }

    public TokenDto login(AccountDto.LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        accountRepository.findByEmail(email).orElseThrow(() -> new NotFoundUserInformationException(email));
        return getTokenDto(loginRequest);
    }

    public TokenDto loginAdmin(AccountDto.LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        Account admin = accountRepository.findByEmail(email).orElseThrow(() -> new NotFoundUserInformationException(email));
        if (!admin.getRole().equals(Role.ADMIN)) {
            throw new NotRoleUserException(admin.getRole());
        }
        return getTokenDto(loginRequest);
    }

    private TokenDto getTokenDto(AccountDto.LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenFactory.generateTokenDto(authentication);

        // refresh token 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return tokenDto;
    }

    @Transactional
    public TokenDto socialSignUp(MiddleAccount middleAccount) {
        String nickname = nicknameRandom();
        duplicateEmailAndNickName(middleAccount.getEmail(), nickname);
        Account account = middleAccount.toAccount(passwordEncoder,suffix, nickname, middleAccount.getProviderEmail());
        accountRepository.save(account);
        return login(account.toAccountRequestDto(suffix).toLoginRequest());

    }

    private String nicknameRandom() {
        String url = "https://nickname.hwanmoo.kr/?format=json&count=1&max_length=10";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = apiService.httpEntityPost(url, HttpMethod.GET, request, RESPONSE_TYPE);
        Map<String, Object> stringObjectMap = Objects.requireNonNull(response.getBody());
        return stringObjectMap.get("words").toString().replaceAll("\\[", "").replaceAll("]", "");
    }

    private void duplicateEmailAndNickName(String email, String nickname) {
        if (accountRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }
        if (accountRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException(nickname);
        }
    }


    private ProviderIdAndEmail getSocialIdWithAccessToken(AuthRequest data) {
        SocialType socialType = data.getSocialType();
        if (socialType.equals(SocialType.GOOGLE)) {
            Map<String, Object> googleIdAndEmail = getGoogleIdAndEmail(data);
            String userId = (String) googleIdAndEmail.get("sub");
            String providerEmail = (String) googleIdAndEmail.get("email");
            return ProviderIdAndEmail.toProviderDto(userId, providerEmail);
        } else if (socialType.equals(SocialType.APPLE)) {
            String appleId = getAppleId(data.getAccessToken());
            String providerEmail = appleId + "@apple";
            return ProviderIdAndEmail.toProviderDto(appleId, providerEmail);
        } else {
            throw new RuntimeException();
        }
    }

    private Map<String, Object> getGoogleIdAndEmail(AuthRequest data) {
        String url = data.getSocialType().getUserInfoUrl();
        HttpMethod method = data.getSocialType().getMethod();
        HttpHeaders headers = setHeaders(data.getAccessToken());
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = apiService.httpEntityPost(url, method, request, RESPONSE_TYPE);
        return response.getBody();
    }

    private AppleDto getAppleAuthPublicKey(){
        String url = SocialType.APPLE.getUserInfoUrl();
        HttpMethod method = SocialType.APPLE.getMethod();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
        ResponseEntity<AppleDto> response =  apiService.getAppleKeys(url,method, request, AppleDto.class);
        return response.getBody();
    }

    private String getAppleId(String identityToken) {
        AppleDto appleKeyStorage = getAppleAuthPublicKey();
        try {
            String headerToken = identityToken.substring(0,identityToken.indexOf("."));
            Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerToken), StandardCharsets.UTF_8), Map.class);
            AppleDto.AppleKey key = appleKeyStorage.getMatchedKeyBy(header.get("kid"), header.get("alg")).orElseThrow();

            byte[] nBytes = Base64.getUrlDecoder().decode(key.n());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.e());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.kty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            Claims claims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(identityToken).getBody();
            String subject = claims.getSubject();
            return subject;

        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException |
                MalformedJwtException | ExpiredJwtException | IllegalArgumentException e) {
            throw new AppleLoginException(e);
        }
    }

    public HttpHeaders setHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    @Transactional
    public Message logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginAccount = authentication.getName();
        if (accountRepository.findByEmail(loginAccount).isPresent() ||
                refreshTokenRepository.findByKey(authentication.getName()).isPresent()){
            // 리프레시 토큰 삭제
            RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName()).get();
            refreshTokenRepository.delete(refreshToken);
        }else{
            throw new NotFoundUserInformationException(loginAccount);
        }
        return Message.of("로그아웃 성공");
    }

    @Transactional
    public TokenResponse reissue(TokenRequest tokenRequestDto){
        if (!tokenFactory.validateToken(tokenRequestDto.getRefreshToken())){
            throw new JwtException();
        }
        Authentication authentication = tokenFactory.getAuthentication(tokenRequestDto.getRefreshToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(JwtException::new);
        reissueRefreshExceptionCheck(refreshToken.getValue(), tokenRequestDto);
        TokenDto tokenDto = tokenFactory.generateTokenDto(authentication);
        return tokenDto.toTokenDto();
    }

    private void reissueRefreshExceptionCheck(String refreshToken, TokenRequest tokenRequestDto){
        if (refreshToken == null){
            throw new JwtException();
        }
        if (!refreshToken.equals(tokenRequestDto.getRefreshToken())){
            throw new JwtException();
        }
    }

    public TokenResponse adminLogin(AccountDto.LoginRequest loginRequestDto) {
        TokenDto tokenDto = loginAdmin(loginRequestDto);
        return tokenDto.toTokenDto();
    }

}