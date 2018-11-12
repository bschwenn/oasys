package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.oasys.repository.MemberRecordRepository;
import com.oasys.repository.StudyRecordRepository;
import com.oasys.util.JsonNodeBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.WhereJoinTable;
import org.springframework.data.jpa.repository.Query;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@TypeDef(
        name = "json-node",
        typeClass = JsonNodeBinaryType.class
)

@Entity
@Table(name = "person")
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private Set<Role> roles;

    @OneToMany(mappedBy = "member"/*, cascade = CascadeType.ALL*/, orphanRemoval = true)
    private Set<MemberRecord> memberRecords;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "Follows",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "gid", referencedColumnName = "gid")
    )
    @JsonIgnore
    private Set<Flock> followedFlocks;

    @ManyToMany(mappedBy = "admins")
    @JsonIgnore
    private Set<Flock> adminForFlocks;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "Interested",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "iid", referencedColumnName = "iid")
    )
    @JsonIgnore
    private Set<Interest> interests;

    @OneToMany(mappedBy = "person", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonIgnore
    private Set<StudyRecord> studyRecords;

    public Person() { }

    public Long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public Set<Role> getRoles() {
        return roles;
    }

    @JsonIgnore
    public Set<Interest> getInterests() {
        return interests;
    }

    public void addInterest(Interest interest) {
        interests.add(interest);
        interest.addInterested(this);
    }

    @JsonIgnore
    public Set<Flock> getFlocks() {
        return memberRecords.stream().map(MemberRecord::getFlock).collect(Collectors.toSet());
    }

    public void addFlock(Flock flock, Long initiatorUid, MemberRecordRepository repository) {
        MemberRecord memberRecord = new MemberRecord(this, flock, initiatorUid);
        repository.save(memberRecord);
        memberRecords.add(memberRecord);
        flock.addMemberRecord(memberRecord);
    }

    public void removeFlock(Flock flock) {
        MemberRecord toRemove = null;
        for (MemberRecord memberRecord : memberRecords) {
            if (memberRecord.getFlock().equals(flock)) {
                toRemove = memberRecord;
                break;
            }
        }
        if (toRemove != null) {
            memberRecords.remove(toRemove);
            flock.removeMemberRecord(toRemove);
        }
    }

    @JsonIgnore
    public Set<Flock> getFollowedFlocks() {
        return followedFlocks;
    }

    public void addFollowedFlock(Flock flock) {
        followedFlocks.add(flock);
    }

    public void removeFollowedFlock(Flock flock) {
        followedFlocks.remove(flock);
    }

    @JsonIgnore
    public Set<Interest> getMajors() {
        Set<Interest> majors = new HashSet<>();
        majors.addAll(getStudyKind("major"));
        return majors;
    }

    @JsonIgnore
    public Set<Interest> getMinors() {
        Set<Interest> minors = new HashSet<>();
        minors.addAll(getStudyKind("minor"));
        return minors;
    }

    private Set<Interest> getStudyKind(String kind) {
        Set<Interest> studies = studyRecords.stream().
                filter(studyRecord -> studyRecord.getKind().equals(kind)).
                map(StudyRecord::getInterest).
                collect(Collectors.toSet());
        return studies == null ? new HashSet<>() : studies;
    }

    public void addStudy(Interest interest, String kind, StudyRecordRepository studyRecordRepository) {
        StudyRecord studyRecord = new StudyRecord(this, interest, kind);
        studyRecordRepository.save(studyRecord);
        studyRecords.add(studyRecord);
        interest.addStudyRecord(studyRecord);
    }

    public void removeStudy(Interest interest) {
        StudyRecord toRemove = null;
        for (StudyRecord studyRecord : studyRecords) {
            if (studyRecord.getInterest().equals(interest)) {
                toRemove = studyRecord;
                break;
            }
        }
        if (toRemove != null) {
            memberRecords.remove(toRemove);
            interest.removeStudyRecord(toRemove);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!uid.equals(person.uid)) return false;
        if (!email.equals(person.email)) return false;
        if (!name.equals(person.name)) return false;
        return username.equals(person.username);
    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }
}