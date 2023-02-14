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
import kr.co.bookand.backend.account.domain.dto.AuthDto;
import kr.co.bookand.backend.account.domain.dto.TokenDto;
import kr.co.bookand.backend.account.exception.*;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.account.util.SecurityUtil;
import kr.co.bookand.backend.bookmark.domain.Bookmark;
import kr.co.bookand.backend.bookmark.domain.BookmarkType;
import kr.co.bookand.backend.bookmark.repository.BookmarkRepository;
import kr.co.bookand.backend.common.service.RestTemplateService;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.common.domain.Message;
import kr.co.bookand.backend.config.jwt.exception.JwtException;
import kr.co.bookand.backend.config.jwt.RefreshToken;
import kr.co.bookand.backend.config.jwt.RefreshTokenRepository;
import kr.co.bookand.backend.config.jwt.TokenFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;


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
    private final BookmarkRepository bookmarkRepository;
    private final RestTemplateService<MultiValueMap<String, String>> apiService;
    private final PasswordEncoder passwordEncoder;

    private static final String INIT_BOOKMARK_FOLDER_NAME = "모아보기";

    ParameterizedTypeReference<Map<String, Object>> RESPONSE_TYPE = new ParameterizedTypeReference<>() {
    };

    @Value("${bookand.suffix}")
    private String suffix;

    @Transactional
    public LoginResponse socialAccess(AuthRequest authRequestDto) {
        String userId = getSocialIdWithAccessToken(authRequestDto).getUserId();
        String providerEmail = getSocialIdWithAccessToken(authRequestDto).getEmail();
        authRequestDto.insertId(userId);
        String email = authRequestDto.extraEmail();
        Optional<Account> account = accountRepository.findByEmail(email);
        SocialType socialType = authRequestDto.getSocialType();

        if (account.isPresent()) {
            // 로그인
            TokenDto tokenDto = login(account.get().toAccountRequestDto(suffix).toLoginRequest());
            TokenResponse tokenResponse = tokenDto.toTokenDto();
            return LoginResponse.builder().tokenResponse(tokenResponse).httpStatus(HttpStatus.OK).build();

        } else {
            MiddleAccount middleAccount = MiddleAccount.builder()
                    .email(email)
                    .socialType(socialType)
                    .providerEmail(providerEmail)
                    .build();
            // 회원가입
            SignDto signTokenDto = tokenFactory.createSignTokenDto(middleAccount);
            return LoginResponse.builder().tokenResponse(signTokenDto).httpStatus(HttpStatus.NOT_FOUND).build();
        }
    }


    public TokenDto login(AccountDto.LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        accountRepository.findByEmail(email).orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, email));
        return getTokenDto(loginRequest);
    }

    public TokenDto loginAdmin(AccountDto.LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            checkRole(Role.ADMIN.name(), authority);
        }

        return getTokenDto(loginRequest);
    }

    public TokenDto loginManager(AccountDto.LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
            checkRole(Role.MANAGER.name(), authority);
        }
        return getTokenDto(loginRequest);
    }

    private void checkRole(String role_manager, String authority) {
        if (!role_manager.equals(authority)) {
            throw new AccountException(ErrorCode.NOT_ROLE_MEMBER, authority);
        }
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
    public TokenResponse socialSignUp(SignDto signTokenDto) {
        String signToken = signTokenDto.signToken();
        SigningAccount signKey = tokenFactory.getSignKey(signToken);
        checkSignUp(signKey);
        String nickname = nicknameRandom();
        String email = signKey.email();
        String socialType = signKey.socialType();
        String providerEmail = signKey.providerEmail();
        duplicateEmailAndNickName(email, nickname);
        Account account = signTokenDto.toAccount(email, socialType, providerEmail, passwordEncoder, suffix, nickname);
        Account saveAccount = accountRepository.save(account);

        List<Bookmark> initBookmark = createInitBookmark(saveAccount);
        saveAccount.updateBookmarkList(initBookmark);

        TokenDto tokenDto = login(account.toAccountRequestDto(suffix).toLoginRequest());
        return tokenDto.toTokenDto();
    }

    private List<Bookmark> createInitBookmark(Account saveAccount) {
        Bookmark initBookmarkArticle = Bookmark.builder()
                .account(saveAccount)
                .folderName(INIT_BOOKMARK_FOLDER_NAME)
                .bookmarkType(BookmarkType.ARTICLE)
                .build();
        Bookmark initBookmarkBookStore = Bookmark.builder()
                .account(saveAccount)
                .folderName(INIT_BOOKMARK_FOLDER_NAME)
                .bookmarkType(BookmarkType.BOOKSTORE)
                .build();

        bookmarkRepository.save(initBookmarkArticle);
        bookmarkRepository.save(initBookmarkBookStore);
        return Arrays.asList(initBookmarkArticle, initBookmarkBookStore);
    }

    public void checkSignUp(SigningAccount signKey) {
        if (signKey.email() == null || signKey.socialType() == null || signKey.providerEmail() == null) {
            throw new AccountException(ErrorCode.INVALID_SIGN_TOKEN, signKey);
        }
    }

    private String nicknameRandom() {
        String url = "https://nickname.hwanmoo.kr/?format=json&count=1&max_length=10";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = apiService.httpEntityPost(url, HttpMethod.GET, request, RESPONSE_TYPE);
        Map<String, Object> stringObjectMap = Objects.requireNonNull(response.getBody());
        return stringObjectMap.get("words").toString().replaceAll("\\[", "").replaceAll("]", "");
    }

    private void duplicateEmailAndNickName(String email, String nickname) {
        if (accountRepository.existsByEmail(email)) {
            throw new AccountException(ErrorCode.EMAIL_DUPLICATION, email);
        }
        if (accountRepository.existsByNickname(nickname)) {
            throw new AccountException(ErrorCode.NICKNAME_DUPLICATION, nickname);
        }
    }


    private ProviderIdAndEmail getSocialIdWithAccessToken(AuthRequest data) {
        SocialType socialType = data.getSocialType();
        if (socialType.equals(SocialType.GOOGLE)) {
            return getGoogleIdAndEmail(data);
        } else if (socialType.equals(SocialType.APPLE)) {
            return getAppleId(data.getAccessToken());
        } else {
            throw new RuntimeException();
        }
    }

    private ProviderIdAndEmail getGoogleIdAndEmail(AuthRequest data) {
        String url = data.getSocialType().getUserInfoUrl();
        HttpMethod method = data.getSocialType().getMethod();
        HttpHeaders headers = setHeaders(data.getAccessToken());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = apiService.httpEntityPost(url, method, request, RESPONSE_TYPE);
        String userId = (String) response.getBody().get("sub");
        String providerEmail = (String) response.getBody().get("email");
        return ProviderIdAndEmail.toProviderDto(userId, providerEmail);
    }

    private AppleDto getAppleAuthPublicKey() {
        String url = SocialType.APPLE.getUserInfoUrl();
        HttpMethod method = SocialType.APPLE.getMethod();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<AppleDto> response = apiService.getAppleKeys(url, method, request, AppleDto.class);
        return response.getBody();
    }

    private ProviderIdAndEmail getAppleId(String identityToken) {
        AppleDto appleKeyStorage = getAppleAuthPublicKey();
        try {
            String headerToken = identityToken.substring(0, identityToken.indexOf("."));
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
            String email = (String) claims.get("email");
            return ProviderIdAndEmail.toProviderDto(subject, email);

        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException |
                MalformedJwtException | ExpiredJwtException | IllegalArgumentException e) {
            throw new AccountException(ErrorCode.APPLE_LOGIN_ERROR, e);
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
                refreshTokenRepository.findByKey(authentication.getName()).isPresent()) {
            // 리프레시 토큰 삭제
            RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName()).get();
            refreshTokenRepository.delete(refreshToken);
        } else {
            throw new AccountException(ErrorCode.NOT_FOUND_MEMBER, loginAccount);
        }
        return Message.of("로그아웃 성공");
    }

    @Transactional
    public TokenResponse reissue(TokenRequest tokenRequestDto) {
        if (!tokenFactory.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new JwtException(ErrorCode.JWT_ERROR, "토큰이 유효하지 않습니다.");
        }
        Authentication authentication = tokenFactory.getAuthentication(tokenRequestDto.getRefreshToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new JwtException(ErrorCode.NOT_FOUND_REFRESH_TOKEN, ErrorCode.NOT_FOUND_REFRESH_TOKEN.getMessage()));
        reissueRefreshExceptionCheck(refreshToken.getValue(), tokenRequestDto);
        TokenDto tokenDto = tokenFactory.generateTokenDto(authentication);

        // refresh token 저장
        RefreshToken newRefreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto.toTokenDto();
    }

    private void reissueRefreshExceptionCheck(String refreshToken, TokenRequest tokenRequestDto) {
        if (refreshToken == null) {
            throw new JwtException(ErrorCode.NOT_FOUND_REFRESH_TOKEN, ErrorCode.NOT_FOUND_REFRESH_TOKEN.getMessage());
        }
        if (!refreshToken.equals(tokenRequestDto.getRefreshToken())) {
            throw new JwtException(ErrorCode.NOT_MATCH_REFRESH_TOKEN, ErrorCode.NOT_MATCH_REFRESH_TOKEN.getMessage());
        }
    }

    public TokenResponse adminLogin(AccountDto.LoginRequest loginRequestDto) {
        TokenDto tokenDto = loginAdmin(loginRequestDto);
        return tokenDto.toTokenDto();
    }

    public TokenResponse managerLogin(AccountDto.LoginRequest loginRequestDto) {
        TokenDto tokenDto = loginManager(loginRequestDto);
        return tokenDto.toTokenDto();
    }

    public Message createManager(AccountDto.ManagerInfo createRequestDto) {
        Account admin = accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, SecurityUtil.getCurrentAccountEmail()));
        admin.getRole().checkAdmin();
        duplicateEmailAndNickName(createRequestDto.email(), createRequestDto.nickname());

        AuthDto.MiddleAccount middleAccount = AuthDto.MiddleAccount.builder()
                .email(createRequestDto.email())
                .socialType(SocialType.GOOGLE)
                .providerEmail("providerEmail")
                .build();
        Account account = middleAccount.toManager(passwordEncoder, createRequestDto.email(), createRequestDto.password(), createRequestDto.nickname());
        Account saveAccount = accountRepository.save(account);

        List<Bookmark> initBookmark = createInitBookmark(saveAccount);
        saveAccount.updateBookmarkList(initBookmark);
        return Message.of("생성 완료");
    }

}