package com.stvpng.tool.querydsl.extension.pg;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;

public class Jsonb extends StringPath {

    public Jsonb(PathMetadata metadata) {
        super(metadata);
    }

    public static Jsonb of(Path<?> path) {
        return new Jsonb(path.getMetadata());
    }

    public <T> BooleanExpression eq(String left, T right) {
        return Expressions.booleanOperation(JsonbOps.getByParamType(right.getClass()), mixin, ConstantImpl.create(left), ConstantImpl.create(right));
    }

}
