package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.oasys.util.JsonNodeBinaryType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;

import javax.persistence.Access;
import javax.persistence.CascadeType;
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
    private JsonNode externalLinks;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "PersonRole",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "rid", referencedColumnName = "rid")
    )
    @JsonIgnore
    private List<Role> roles;

    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "Member",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "gid", referencedColumnName = "gid")
    )
    @JsonIgnore
    private List<Flock> flocks;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Follows",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "gid", referencedColumnName = "gid")
    )
    @JsonIgnore

    private List<Flock> followedFlocks;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Interested",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "iid", referencedColumnName = "iid")
    )
    @JsonIgnore
    private List<Interest> interests;

    @ManyToMany
    @JoinTable(
            name = "Studies",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "iid", referencedColumnName = "iid")

    )
    @WhereJoinTable(clause = "kind = 'major'")
    private List<Interest> majors;

    @ManyToMany
    @JoinTable(
            name = "Studies",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "iid", referencedColumnName = "iid")

    )
    @WhereJoinTable(clause = "kind = 'minor'")
    private List<Interest> minor;

    public Person() { }

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

    public JsonNode getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(JsonNode links) {
        this.externalLinks = externalLinks;
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

    public List<Flock> getFlocks() {
        return flocks;
    }

    public void setFlocks(List<Flock> flocks) {
        this.flocks = flocks;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public List<Interest> getMajors() {
        return majors;
    }

    public void setMajors(List<Interest> majors) {
        this.majors = majors;
    }

    public List<Interest> getMinor() {
        return minor;
    }

    public void setMinor(List<Interest> minor) {
        this.minor = minor;
    }

    public List<Flock> getFollowedFlocks() {
        return followedFlocks;
    }

    public void setFollowedFlocks(List<Flock> followedFlocks) {
        this.followedFlocks = followedFlocks;
    }
}