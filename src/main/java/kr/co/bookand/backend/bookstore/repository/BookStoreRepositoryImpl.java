package kr.co.bookand.backend.bookstore.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.bookand.backend.bookstore.domain.BookStore;
import kr.co.bookand.backend.bookstore.domain.BookStoreTheme;
import kr.co.bookand.backend.bookstore.domain.BookStoreType;
import kr.co.bookand.backend.bookstore.domain.QBookStore;
import kr.co.bookand.backend.common.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static kr.co.bookand.backend.bookstore.domain.QBookStore.*;

@RequiredArgsConstructor
public class BookStoreRepositoryImpl implements BookStoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BookStore> findAllBySearch(String search, String theme, String status, Pageable pageable) {
    JPQLQuery<BookStore> query = queryFactory.selectFrom(bookStore)
                .where(containSearch(search))
                .where(eqTheme(theme))
                .where(eqStatus(status))
                .orderBy(bookStore.id.desc());

        return PageableExecutionUtils.getPage(
                query.offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                query::fetchCount);
    }

    private BooleanExpression containSearch(String search) {
        if(search == null || search.isEmpty()) {
            return null;
        }
        return bookStore.name.contains(search);
    }

    private BooleanExpression eqTheme(String theme) {
        if(theme == null || theme.isEmpty()) {
            return null;
        }
        BookStoreType bookStoreType = BookStoreType.valueOf(theme);
        return bookStore.themeList.any().theme.eq(bookStoreType);
    }

    private BooleanExpression eqStatus(String status) {
        if(status == null || status.isEmpty()) {
            return null;
        }
        Status statusType = Status.valueOf(status);
        return bookStore.status.eq(statusType);
    }
}
