package kr.co.bookand.backend.bookstore.domain;

import kr.co.bookand.backend.common.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookStoreVersion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookstore_version_id")
    private Long id;

    @OneToMany(mappedBy = "bookStoreVersion", cascade = CascadeType.ALL)
    private List<BookStore> bookStoreList;

}
