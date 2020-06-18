package com.stvpng.tool.querydsl.extension;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.AbstractJPAQuery;
import com.stvpng.tool.querydsl.extension.pg.ExtendedQuerydsl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
public class ExtendedQueryDslJpaRepositoryImpl<T>
        extends QuerydslJpaPredicateExecutor<T> implements ExtendedQueryDslJpaRepository<T> {

    private static final EntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;

    private final EntityPath<T> path;
    private final PathBuilder<T> builder;
    private final Querydsl querydsl;

    private EntityManager entityManager;

    public ExtendedQueryDslJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager, resolver, null);
        this.path = resolver.createPath(entityInformation.getJavaType());
        this.builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new ExtendedQuerydsl(entityManager, builder);
        this.entityManager = entityManager;

    }

    @Override
    public <T1> Page<T1> findAll(JPQLQuery<T1> jpqlQuery, Pageable pageable) {

        JPQLQuery<T1> query = querydsl.applyPagination(pageable, jpqlQuery);

        return PageableExecutionUtils.getPage(query.fetch(), pageable, jpqlQuery::fetchCount);
    }


    @Override
    protected JPQLQuery<?> createQuery(Predicate... predicate) {

        Assert.notNull(predicate, "Predicate must not be null!");

        return doCreateQuery(predicate);
    }

    private AbstractJPAQuery<?, ?> doCreateQuery(@Nullable Predicate... predicate) {

        AbstractJPAQuery<?, ?> query = querydsl.createQuery(path);

        if (predicate != null) {
            query = query.where(predicate);
        }

        return query;
    }

}