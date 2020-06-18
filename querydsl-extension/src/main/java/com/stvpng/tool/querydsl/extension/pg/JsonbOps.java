package com.stvpng.tool.querydsl.extension.pg;

import com.querydsl.core.types.Operator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

@Getter
@AllArgsConstructor
public enum JsonbOps implements Operator {

    EQ_STR(Boolean.class,
            String.class,
            "transform_jsonb_str({0}, {1}) = {2}",
            "transform_jsonb_str",
            new SQLFunctionTemplate(StandardBasicTypes.STRING, "?1 ->> ?2")),
    EQ_INT(Boolean.class,
            Integer.class,
            "transform_jsonb_int({0}, {1}) = {2}",
            "transform_jsonb_int",
            new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "(?1 ->> ?2) :: int"));

    private final Class<?> type;
    private final Class<?> paramType;
    private final String queryDslPattern;
    private final String functionName;
    private final SQLFunction sqlFunction;


    static JsonbOps getByParamType(Class<?> clazz) {
        for (JsonbOps value : JsonbOps.values()) {
            if (value.paramType.equals(clazz))
                return value;
        }
        throw new IllegalArgumentException(String.format("clazz %s is not supported", clazz));
    }

    @Override
    public Class<?> getType() {
        return type;
    }


}