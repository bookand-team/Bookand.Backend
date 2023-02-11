package kr.co.bookand.backend.common.service;

import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.BookstoreTheme;
import kr.co.bookand.backend.common.domain.Status;
import kr.co.bookand.backend.policy.domain.Policy;
import kr.co.bookand.backend.policy.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class PolicyDummyData {

    private final PolicyRepository policyRepository;

    @PostConstruct
    public void dummyData() {
        Policy terms = Policy.builder()
                .title("terms")
                .content("content")
                .build();

        Policy personalInfoAgree = Policy.builder()
                .title("personal-info-agree")
                .content("content")
                .build();

        Policy locationBaseTerms = Policy.builder()
                .title("location-base-terms")
                .content("content")
                .build();

        Policy privacy = Policy.builder()
                .title("privacy")
                .content("content")
                .build();

        Policy operation = Policy.builder()
                .title("operation")
                .content("content")
                .build();

        policyRepository.save(terms);
        policyRepository.save(personalInfoAgree);
        policyRepository.save(locationBaseTerms);
        policyRepository.save(privacy);
        policyRepository.save(operation);

    }
}
