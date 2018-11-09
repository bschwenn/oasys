package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.oasys.util.JsonNodeBinaryType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

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
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PERSON_SEQ")
    @SequenceGenerator(name="PERSON_SEQ", sequenceName="PERSON_SEQ", allocationSize=100)
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

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PersonRole",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "rid", referencedColumnName = "rid")
    )
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Member",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "gid", referencedColumnName = "gid")
    )
    private List<Flock> flocks;

    // TODO(Ben S:) Interests
    // Moderates
    // Participates
    // Studies
    // Follows
    // Groups etc.

    public Person() { }

    public Person(String email, String name, int graduationYear, String photoPath, JsonNode links) {
        this.email = email;
        this.name = name;
        this.graduationYear = graduationYear;
        this.photoPath = photoPath;
        this.links = links;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public JsonNode getLinks() {
        return links;
    }

    public void setLinks(JsonNode links) {
        this.links = links;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}