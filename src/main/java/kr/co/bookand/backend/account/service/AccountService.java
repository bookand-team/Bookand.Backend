package kr.co.bookand.backend.account.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.Role;
import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.account.util.SecurityUtil;
import kr.co.bookand.backend.common.domain.Message;
import kr.co.bookand.backend.common.domain.dto.PageResponse;
import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.co.bookand.backend.account.domain.dto.AccountDto.*;
import static kr.co.bookand.backend.config.security.SecurityUtils.getCurrentAccountEmail;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthService authService;

    public Account getCurrentAccount() {
        return accountRepository.findByEmail(getCurrentAccountEmail()).orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, null));
    }

    public void isAccountAdmin() {
        Role role = getCurrentAccount().getRole();
        if (!role.toString().equals("ADMIN")) {
            throw new AccountException(ErrorCode.ROLE_ACCESS_ERROR, role);
        }
    }

    public Account checkAccountUser() {
        Role role = getCurrentAccount().getRole();
        if (!role.toString().equals("USER"))
            throw new AccountException(ErrorCode.ROLE_ACCESS_ERROR, role);
        return getCurrentAccount();
    }

    // 내 정보 조회
    public MemberInfo getAccount() {
        return accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                .map(MemberInfo::of)
                .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, SecurityUtil.getCurrentAccountEmail()));
    }

    // 회원 정보 조회 (id)
    public MemberInfo getAccount(Long memberId) {
        return accountRepository.findById(memberId)
                .map(MemberInfo::of)
                .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, memberId));
    }

    // 회원 정보 조회 (닉네임)
    public MemberInfo getAccount(String nickname) {
        return accountRepository.findByNickname(nickname)
                .map(MemberInfo::of)
                .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, nickname));
    }

    // 전체 회원 조회
    public MemberListResponse getAccountList(Pageable pageable) {
        Page<MemberInfo> accounts = accountRepository.findAll(pageable)
                .map(MemberInfo::of);
        return MemberListResponse.of(accounts);
    }

    // 회원 수정
    @Transactional
    public MemberInfo updateNickname(MemberUpdateRequest request) {
        Account dbAccount = accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, SecurityUtil.getCurrentAccountEmail()));
        boolean nicknameBoolean = checkNicknameBoolean(request.nickname(), dbAccount.getNickname());
        if (nicknameBoolean){
            throw new AccountException(ErrorCode.NICKNAME_DUPLICATION, request.nickname());
        }
        dbAccount.updateProfileImage(request.profileImage());
        dbAccount.updateNickname(request.nickname());
        return MemberInfo.of(dbAccount);
    }

    public Message checkNickname(String nickname) {
        boolean exists = accountRepository.existsByNickname(nickname);
        return exists ? Message.of("CONFLICT", 409) : Message.of("OK", 200);
    }

    public boolean checkNicknameBoolean(String nickname, String currentNickname) {
        if(currentNickname.equals(nickname)) {
            return false;
        }
        return accountRepository.existsByNickname(nickname);
    }

    public boolean checkNicknameBoolean(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

    @Transactional
    public void removeAccount(Account account) {
        account.softDelete();
    }

    public NicknameResponse getRandomNickname() {
        String nicknameRandom = authService.nicknameRandom();
        while(checkNicknameBoolean(nicknameRandom)) {
            nicknameRandom = authService.nicknameRandom();
        }
        return NicknameResponse.of(nicknameRandom);
    }
}
