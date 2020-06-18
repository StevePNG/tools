package com.stvpng.tool.querydsl.extension.pg;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.EclipseLinkTemplates;
import com.querydsl.jpa.impl.AbstractJPAQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;

public class ExtendedQuerydsl extends Querydsl {

    private final EntityManager em;
    private final PersistenceProvider provider;
    private final PathBuilder<?> builder;

    /**
     * Creates a new {@link Querydsl} for the given {@link EntityManager} and {@link PathBuilder}.
     *
     * @param em      must not be {@literal null}.
     * @param builder must not be {@literal null}.
     */
    public ExtendedQuerydsl(EntityManager em, PathBuilder<?> builder) {
        super(em, builder);

        Assert.notNull(em, "EntityManager must not be null!");
        Assert.notNull(builder, "PathBuilder must not be null!");

        this.em = em;
        this.provider = PersistenceProvider.fromEntityManager(em);
        this.builder = builder;
    }

    @Override
    public <T> AbstractJPAQuery<T, JPAQuery<T>> createQuery() {

        switch (provider) {
            case ECLIPSELINK:
                return new JPAQuery<T>(em, EclipseLinkTemplates.DEFAULT);
            case HIBERNATE:
                return new JPAQuery<T>(em, ExtendedHQLTemplates.DEFAULT);
            case GENERIC_JPA:
            default:
                return new JPAQuery<T>(em);
        }
    }
}
