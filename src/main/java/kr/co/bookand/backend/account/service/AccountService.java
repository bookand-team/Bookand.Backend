package kr.co.bookand.backend.account.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.account.exception.NotFoundUserInformationException;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.account.util.SecurityUtil;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    // 회원 정보 조회
    @Transactional(readOnly = true)
    public AccountDto.MemberInfo getAccount() {
        return accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                .map(AccountDto.MemberInfo::of)
                .orElseThrow(NotFoundUserInformationException::new);
    }

    // 회원 수정
    @Transactional
    public AccountDto.MemberInfo updateNickname(AccountDto.MemberRequestUpdate memberRequestUpdateDto){
        Optional<Account> checkNicknameAccount = accountRepository.findByNickname(memberRequestUpdateDto.getNickname());
        if (checkNicknameAccount.isPresent()) {
            throw new RuntimeException();
        } else {
            Account dbAccount = accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                    .orElseThrow(NotFoundUserInformationException::new);
            dbAccount.updateNickname(memberRequestUpdateDto.getNickname());
            return AccountDto.MemberInfo.of(dbAccount);
        }
    }

    @Transactional
    public void removeAccount(Account account) {
        accountRepository.delete(account);
    }

}
