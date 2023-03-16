package kr.co.bookand.backend.account.service;

import kr.co.bookand.backend.account.domain.*;
import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.account.repository.RevokeAccountRepository;
import kr.co.bookand.backend.account.repository.SuspendedAccountRepository;
import kr.co.bookand.backend.account.util.AccountUtil;
import kr.co.bookand.backend.account.util.SecurityUtil;
import kr.co.bookand.backend.common.domain.Message;
import kr.co.bookand.backend.common.exception.ErrorCode;
import kr.co.bookand.backend.config.jwt.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static kr.co.bookand.backend.account.domain.dto.AccountDto.*;
import static kr.co.bookand.backend.account.domain.dto.AuthDto.*;
import static kr.co.bookand.backend.account.domain.dto.RevokeDto.*;
import static kr.co.bookand.backend.config.security.SecurityUtils.getCurrentAccountEmail;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthService authService;
    private final RevokeAccountRepository revokeAccountRepository;
    private final SuspendedAccountRepository suspendedAccountRepository;

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
        if (nicknameBoolean) {
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
        if (currentNickname.equals(nickname)) {
            return false;
        }
        return accountRepository.existsByNickname(nickname);
    }

    public boolean checkNicknameBoolean(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

    @Transactional
    public boolean revokeAccount(Account account, RevokeReasonRequest request) {

        Account loginAccount = accountRepository.findByEmail(account.getEmail())
                .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, account.getEmail()));
        RevokeType revokeType = RevokeType.of(request.revokeType());
        AuthRequest authRequest = AuthRequest.builder()
                .socialType(account.getProvider())
                .accessToken(request.socialAccessToken())
                .build();

        checkLoginMember(account, authRequest);

        RevokeAccount revokeAccount = RevokeAccount.builder()
                .reason(request.reason())
                .revokeType(revokeType)
                .accountId(account.getId())
                .build();
        revokeAccountRepository.save(revokeAccount);
        accountRepository.delete(loginAccount);
        return loginAccount.isVisibility();
    }

    public void checkLoginMember(Account account, AuthRequest authRequest) {
        ProviderIdAndEmail socialIdWithAccessToken = authService.getSocialIdWithAccessToken(authRequest);
        String socialEmail = socialIdWithAccessToken.getUserId() + "@" + account.getProvider().toLowerCase() + ".com";
        if (!account.getEmail().equals(socialEmail)) {
            throw new AccountException(ErrorCode.NOT_MATCH_MEMBER, socialEmail);
        }
    }

    public NicknameResponse getRandomNickname() {
        String nicknameRandom = authService.nicknameRandom();
        while (checkNicknameBoolean(nicknameRandom)) {
            nicknameRandom = authService.nicknameRandom();
        }
        return NicknameResponse.of(nicknameRandom);
    }

    @Transactional
    public AccountStatus suspendAccount(Long accountId) {
        Account loginAccount = AccountUtil.getAccount();
        if (!loginAccount.getRole().equals(Role.ADMIN)) {
            throw new AccountException(ErrorCode.ROLE_ACCESS_ERROR, loginAccount.getRole());
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, accountId));

        Optional<SuspendedAccount> response = suspendedAccountRepository.findById(accountId);
        if (response.isPresent()) {
            SuspendedAccount suspend = response.get();
            return setSuspendedAccount(account, suspend);
        } else {
            SuspendedAccount suspend = SuspendedAccount.builder()
                    .account(account)
                    .build();
            suspendedAccountRepository.save(suspend);
            return setSuspendedAccount(account, suspend);
        }
    }

    private AccountStatus setSuspendedAccount(Account account, SuspendedAccount suspend) {
        int suspendedCount = suspend.getSuspendedCount();

        LocalDateTime suspendedDate = suspendedCount == 0
                ? LocalDateTime.now().plusDays(7)
                : LocalDateTime.now().plusMonths(6);

        AccountStatus newStatus = suspendedCount == 0
                ? AccountStatus.SUSPENDED
                : AccountStatus.DELETED;

        suspend.setSuspendedDate(suspendedDate);
        suspend.addSuspendedCount();
        account.updateAccountStatus(newStatus);

        //TODO : 스케줄러에 등록하기

        return newStatus;
    }
}
