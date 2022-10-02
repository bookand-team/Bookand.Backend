package kr.co.bookand.backend.account.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.Role;
import kr.co.bookand.backend.account.domain.SocialType;
import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.account.domain.dto.TokenDto;
import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.account.exception.NotFoundUserInformationException;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.common.ApiService;
import kr.co.bookand.backend.common.Message;
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


import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static kr.co.bookand.backend.account.domain.dto.AuthDto.*;

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
    ParameterizedTypeReference<Map<String, Object>> RESPONSE_TYPE  =  new ParameterizedTypeReference<>(){};

    private String suffix;

    @Transactional
    public TokenDto socialAccess(AuthRequest authRequestDto) {
        String userId = getSocialIdWithAccessToken(authRequestDto);
        authRequestDto.insertId(userId);
        String email = authRequestDto.extraEmail();
        Optional<Account> account = accountRepository.findByEmail(email);
        SocialType socialType = authRequestDto.getSocialType();

        if (account.isPresent()) {
            // 로그인
            TokenDto tokenDto = login(account.get().toAccountRequestDto(suffix).toLoginRequest());
            log.info("로그인", tokenDto);
            return tokenDto;
        }else {
            MiddleAccount middleAccount = MiddleAccount.builder()
                    .email(email)
                    .socialType(socialType)
                    .build();

            // 회원가입
            TokenDto tokenMessage = socialSignUp(middleAccount);
            return tokenMessage;
        }
    }

    public TokenDto login(AccountDto.LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        accountRepository.findByEmail(email).orElseThrow(NotFoundUserInformationException::new);
        return getTokenDto(loginRequest);
    }

    public TokenDto loginAdmin(AccountDto.LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        Account admin = accountRepository.findByEmail(email).orElseThrow(NotFoundUserInformationException::new);
        if (admin.getRole().equals(Role.ADMIN)) {
            // 예외처리
            throw new RuntimeException();
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
        Account account = middleAccount.toAccount(passwordEncoder,suffix, nickname);
        accountRepository.save(account);
        TokenDto tokenDto = login(account.toAccountRequestDto(suffix).toLoginRequest());
        return tokenDto;

    }

    private String nicknameRandom() {
        String url = "https://nickname.hwanmoo.kr/?format=json&count=1&max_length=10";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = apiService.httpEntityPost(url, HttpMethod.GET, request, RESPONSE_TYPE);
        Map<String, Object> stringObjectMap = Objects.requireNonNull(response.getBody());
        String words = stringObjectMap.get("words").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        return words ;
    }

    private void duplicateEmailAndNickName(String email, String nickname) {
        if (accountRepository.existsByEmail(email)) {
            throw new AccountException("email 중복");
        }
        if (accountRepository.existsByNickname(nickname)) {
            throw new AccountException("nickname 중복");
        }
    }


    private String getSocialIdWithAccessToken(AuthRequest data) {
        SocialType socialType = data.getSocialType();
        if (socialType.equals(SocialType.GOOGLE)) {
            return getGoogleId(data);
        } else {
//            return getAppleId(data);
            return "test";
        }
    }

    private String getGoogleId(AuthRequest data) {
        String url = data.getSocialType().getUserInfoUrl();
        HttpMethod method = data.getSocialType().getMethod();
        HttpHeaders headers = setHeaders(data.getAccessToken());
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = apiService.httpEntityPost(url, method, request, RESPONSE_TYPE);
        Map<String, Object> response1 = (Map<String, Object>) response.getBody();
        String id = (String) response1.get("sub");
        return id;
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
        if (accountRepository.findByEmail(loginAccount).isPresent()){
            // 리프레시 토큰 삭제
            RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName()).get();
            refreshTokenRepository.delete(refreshToken);
        }else{
            throw new NotFoundUserInformationException();
        }
        return Message.of("로그아웃 성공");
    }

    @Transactional
    public TokenDto reissue(TokenDto.TokenRequestDto tokenRequestDto){

        if (!tokenFactory.validateToken(tokenRequestDto.getRefreshToken())){
            // 예외처리
            throw new RuntimeException();
        }
        Authentication authentication = tokenFactory.getAuthentication(tokenRequestDto.getRefreshToken());
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(
                        // 예외처리
                );
        reissueRefreshExceptionCheck(refreshToken.getValue(), tokenRequestDto);
        TokenDto tokenDto = tokenFactory.generateTokenDto(authentication);
        return tokenDto;
    }

    private void reissueRefreshExceptionCheck(String refreshToken, TokenDto.TokenRequestDto tokenRequestDto){
        if (refreshToken == null){
            // 예외처리
            throw new RuntimeException();
        }
        if (!refreshToken.equals(tokenRequestDto.getRefreshToken())){
            // 예외처리
            throw new RuntimeException();
        }
    }

    public TokenDto adminLogin(AccountDto.LoginRequest loginRequestDto) {
        TokenDto tokenDto = loginAdmin(loginRequestDto);
        return tokenDto;
    }

}