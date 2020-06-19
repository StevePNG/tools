package com.stvpng.tool.example.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Thing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String content;

}
