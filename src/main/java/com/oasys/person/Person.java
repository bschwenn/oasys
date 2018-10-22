package com.oasys.person;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Getter
@Setter
@Table(name = "person")
public class Person {

    private @Id @GeneratedValue Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    private int graduationYear;
    private String photoPath;
    // links will be JSON
    private String links;

    public Person() {}

    public Person(String email, String name, int graduationYear, String photoPath, String links) {
        this.email = email;
        this.name = name;
        this.graduationYear = graduationYear;
        this.photoPath = photoPath;
        this.links = links;
    }
}