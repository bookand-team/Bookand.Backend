package kr.co.bookand.backend.config.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager

@Configuration
open class QueryDslConfig(
    private val em: EntityManager
) {
    @Bean
    open fun jpaQueryFactory(): JPAQueryFactory = JPAQueryFactory(em)
}