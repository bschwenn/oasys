package com.oasys.person;

import com.fasterxml.jackson.databind.JsonNode;
import com.oasys.hibernate.JsonNodeBinaryType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@TypeDef(
        name = "json-node",
        typeClass = JsonNodeBinaryType.class
)

@Data
@Entity
@Getter
@Setter
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "uid")
    private Long uid;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(name = "graduation_year")
    private int graduationYear;

    @Column(name = "photo_path")
    private String photoPath;

    @Type(type = "json-node")
    private JsonNode links;

    public Person() {}

    public Person(String email, String name, int graduationYear, String photoPath, JsonNode links) {
        this.email = email;
        this.name = name;
        this.graduationYear = graduationYear;
        this.photoPath = photoPath;
        this.links = links;
    }
}