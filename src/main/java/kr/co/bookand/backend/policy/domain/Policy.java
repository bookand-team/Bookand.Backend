package kr.co.bookand.backend.policy.domain;

import kr.co.bookand.backend.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Policy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long id;

    private String title;
    private String context;

    public void updateContext(String context) {
        this.context = context;
    }
}
