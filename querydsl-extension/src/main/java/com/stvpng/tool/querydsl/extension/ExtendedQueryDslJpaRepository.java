package com.stvpng.tool.querydsl.extension;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExtendedQueryDslJpaRepository<T> extends QuerydslPredicateExecutor<T> {
    <T1> Page<T1> findAll(JPQLQuery<T1> jpqlQuery, Pageable pageable);
}