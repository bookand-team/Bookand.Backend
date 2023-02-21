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

    private String name;
    private String title;

    @Column(columnDefinition = "TEXT", length = 30000)
    private String content;

    public void updateContent(String content) {
        this.content = content;
    }
}
