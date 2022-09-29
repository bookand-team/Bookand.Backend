package kr.co.bookand.backend.account.service;

import kr.co.bookand.backend.account.domain.Account;
import kr.co.bookand.backend.account.domain.dto.AccountDto;
import kr.co.bookand.backend.account.exception.NotFoundUserInformationException;
import kr.co.bookand.backend.account.repository.AccountRepository;
import kr.co.bookand.backend.account.util.SecurityUtil;
import kr.co.bookand.backend.common.CodeStatus;
import kr.co.bookand.backend.common.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Message updateNickname(String nickname){
        Optional<Account> checkNicknameAccount = accountRepository.findByNickname(nickname);
        if (checkNicknameAccount.isPresent()) {
            return Message.of(CodeStatus.FAIL, "닉네임 중복");
        } else {
            Account dbAccount = accountRepository.findByEmail(SecurityUtil.getCurrentAccountEmail())
                    .orElseThrow(NotFoundUserInformationException::new);
            dbAccount.updateNickname(nickname);
            return Message.of(CodeStatus.SUCCESS, "닉네임 변경됨.");
        }
    }
}
