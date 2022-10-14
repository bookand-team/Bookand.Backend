package kr.co.bookand.backend.account.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.account.exception.NotFoundUserInformationException;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.account.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static kr.co.bookand.backend.account.domain.dto.AccountDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    // 회원 정보 조회
    @Transactional(readOnly = true)
    public MemberInfo getAccount() {
        return accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                .map(MemberInfo::of)
                .orElseThrow(()-> new NotFoundUserInformationException(SecurityUtil.getCurrentAccountEmail()));
    }

    // 회원 수정
    @Transactional
    public MemberInfo updateNickname(MemberUpdateRequest memberRequestUpdateDto){
        Optional<Account> checkNicknameAccount = accountRepository.findByNickname(memberRequestUpdateDto.getNickname());
        if (checkNicknameAccount.isPresent()) {
            throw new NotFoundUserInformationException(checkNicknameAccount);
        } else {
            Account dbAccount = accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                    .orElseThrow(()-> new NotFoundUserInformationException(SecurityUtil.getCurrentAccountEmail()));
            dbAccount.updateNickname(memberRequestUpdateDto.getNickname());
            return MemberInfo.of(dbAccount);
        }
    }

    @Transactional
    public void removeAccount(Account account) {
        accountRepository.delete(account);
    }

}
