package com.stvpng.tool.example.querydsl.pg;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.stvpng.tool.example.domain.QThing;
import com.stvpng.tool.example.domain.Thing;
import com.stvpng.tool.example.querydsl.Configuration;
import com.stvpng.tool.example.querydsl.ThingRepository;
import com.stvpng.tool.querydsl.extension.pg.Jsonb;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Configuration.class)
public class TestExtendedQueryDslJpaRepository {

    @Autowired
    ThingRepository userRepository;

    @Test
    public void testJsonQuery() {
        BooleanExpression expression =
                QThing.thing.name.endsWith("man")
                        .and(Jsonb.of(QThing.thing.content).eq("age", 20));

        Iterable<Thing> all = userRepository.findAll(expression);

        assertTrue(all.iterator().hasNext());
    }

    @Before
    public void initData() {
        userRepository.save(Thing.builder()
                .name("human")
                .content("{\"age\": 20, \"name\": \"john\"}")
                .build());
    }
}
