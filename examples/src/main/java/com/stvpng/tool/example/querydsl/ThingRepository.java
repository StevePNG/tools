package com.stvpng.tool.example.querydsl;

import com.stvpng.tool.example.domain.Thing;
import com.stvpng.tool.querydsl.extension.ExtendedQueryDslJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThingRepository extends ExtendedQueryDslJpaRepository<Thing>, JpaRepository<Thing, String> {
}
