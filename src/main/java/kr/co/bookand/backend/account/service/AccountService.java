package kr.co.bookand.backend.account.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.Role;
import kr.co.bookand.backend.account.exception.AccountException;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.account.util.SecurityUtil;
import kr.co.bookand.backend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.co.bookand.backend.account.domain.dto.AccountDto.*;
import static kr.co.bookand.backend.config.security.SecurityUtils.getCurrentAccountEmail;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account getCurrentAccount() {
        return accountRepository.findByEmail(getCurrentAccountEmail()).orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, null));
    }

    public void isAccountAdmin() {
        Role role = getCurrentAccount().getRole();
        if (!role.toString().equals("ADMIN")) {
            throw new AccountException(ErrorCode.ROLE_ACCESS_ERROR, role);
        }
    }

    // 회원 정보 조회
    @Transactional(readOnly = true)
    public MemberInfo getAccount() {
        return accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                .map(MemberInfo::of)
                .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, SecurityUtil.getCurrentAccountEmail()));
    }

    // 회원 수정
    @Transactional
    public MemberInfo updateNickname(MemberUpdateRequest memberRequestUpdateDto) {
        if (validNickname(memberRequestUpdateDto.getNickname())) {
            throw new AccountException(ErrorCode.NICKNAME_DUPLICATION, memberRequestUpdateDto.getNickname());
        } else {
            Account dbAccount = accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                    .orElseThrow(() -> new AccountException(ErrorCode.NOT_FOUND_MEMBER, SecurityUtil.getCurrentAccountEmail()));
            dbAccount.updateNickname(memberRequestUpdateDto.getNickname());
            return MemberInfo.of(dbAccount);
        }
    }

    public boolean validNickname(String nickname) {
        return accountRepository.findByNickname(nickname).isPresent();
    }

    @Transactional
    public void removeAccount(Account account) {
        accountRepository.delete(account);
    }

}
