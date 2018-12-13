package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.oasys.repository.*;
import com.oasys.util.JacksonUtil;
import com.oasys.util.JsonNodeBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
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
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="person_uid_seq")
    @SequenceGenerator(name="person_uid_seq", sequenceName="person_uid_seq", initialValue = 10000, allocationSize = 1)
    @Column(name = "uid", updatable = false, nullable = false)
    private Long uid;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(name = "graduation_year")
    private int graduationYear;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "links")
    @Type(type = "json-node")
    private JsonNode externalLinks;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "bio")
    private String bio;

    @ManyToMany(cascade= CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "PersonRole",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "rid", referencedColumnName = "rid")
    )
    @JsonIgnore
    private Set<Role> roles;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberRecord> memberRecords;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberRequest> memberRequests;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeRecord> likeRecords;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PinRecord> pinRecords;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GoingRecord> goingRecords;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FollowRecord> followerRecords;

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

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<StudyRecord> studyRecords;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> createdEvents;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public void setPassword(String password) {
        this.password = password;
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

    public void removeInterest(Interest interest) {
        interests.remove(interest);
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

    public void removeFlock(Flock flock, MemberRecordRepository repository) {
        MemberRecord toRemove = null;
        for (MemberRecord memberRecord : memberRecords) {
            if (memberRecord.getFlock().equals(flock)) {
                toRemove = memberRecord;
                break;
            }
        }
        if (toRemove != null) {
            memberRecords.remove(toRemove);
            repository.delete(toRemove);
            flock.removeMemberRecord(toRemove);
        }
    }

    public void follow(Flock flock, FollowRecordRepository repository) {
        FollowRecord followRecord = new FollowRecord(this, flock);
        repository.save(followRecord);
        followerRecords.add(followRecord);
        flock.addFollowerRecord(followRecord);
    }

    public void unfollow(Flock flock, FollowRecordRepository repository) {
        FollowRecord toRemove = null;
        for (FollowRecord followRecord : followerRecords) {
            if (followRecord.getFlock().equals(flock)) {
                toRemove = followRecord;
                break;
            }
        }
        if (toRemove != null) {
            followerRecords.remove(toRemove);
            repository.delete(toRemove);
            flock.removeFollowerRecord(toRemove);
        }
    }

    @JsonIgnore
    public Set<Flock> getFollowedFlocks() {
        Set<Flock> followed = new HashSet<>();
        for (FollowRecord r : followerRecords) {
            followed.add(r.getFlock());
        }
        return followed;
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

    public void removeStudy(Interest interest, StudyRecordRepository repository) {
        StudyRecord toRemove = null;
        for (StudyRecord studyRecord : studyRecords) {
            if (studyRecord.getInterest().equals(interest)) {
                toRemove = studyRecord;
                break;
            }
        }
        if (toRemove != null) {
            memberRecords.remove(toRemove);
            repository.delete(toRemove);
            interest.removeStudyRecord(toRemove);
        }
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public JsonNode getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(String externalLinks) {
        this.externalLinks = JacksonUtil.toJsonNode(externalLinks);
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Set<Flock> getAdminForFlocks() {
        return adminForFlocks;
    }

    @JsonIgnore
    public Set<LikeRecord> getLikeRecords() {
        return likeRecords;
    }


    public void like(Post post, LikeRecordRepository likeRecordRepository) {
        LikeRecord record = new LikeRecord(this, post);
        likeRecordRepository.save(record);
        likeRecords.add(record);
        post.addLikeRecord(record);
    }

    public void removeLike(Post post, LikeRecordRepository likeRecordRepository) {
        LikeRecord toRemove = null;
        for (LikeRecord likeRecord : likeRecords) {
            if (likeRecord.getPost().equals(post)) {
                toRemove = likeRecord;
                break;
            }
        }
        if (toRemove != null) {
            memberRecords.remove(toRemove);
            likeRecordRepository.delete(toRemove);
            post.removeLike(toRemove);
        }
    }

    public void pin(Post post, PinRecordRepository pinRecordRepository) {
        PinRecord record = new PinRecord(this, post);
        pinRecordRepository.save(record);
        pinRecords.add(record);
        post.addPinRecord(record);
    }

    public void removePin(Post post, PinRecordRepository pinRecordRepository) {
        PinRecord toRemove = null;
        for (PinRecord pinRecord : pinRecords) {
            if (pinRecord.getPost().equals(post)) {
                toRemove = pinRecord;
                break;
            }
        }
        if (toRemove != null) {
            memberRecords.remove(toRemove);
            pinRecordRepository.delete(toRemove);
            post.removePinRecord(toRemove);
        }
    }

    public void addGoing(Event event, GoingRecordRepository goingRecordRepository) {
        GoingRecord record = new GoingRecord(this, event);
        goingRecordRepository.save(record);
        goingRecords.add(record);
        event.addGoingRecord(record);
    }

    public void removeGoing(Event event, GoingRecordRepository goingRecordRepository) {
        GoingRecord toRemove = null;
        for (GoingRecord goingRecord : goingRecords) {
            if (goingRecord.getEvent().equals(event)) {
                toRemove = goingRecord;
                break;
            }
        }
        if (toRemove != null) {
            goingRecords.remove(toRemove);
            goingRecordRepository.delete(toRemove);
            event.removeGoingRecord(toRemove);
        }
    }


    @JsonIgnore
    public Set<PinRecord> getPinRecords() {
        return pinRecords;
    }

    @JsonIgnore
    public Set<GoingRecord> getGoingRecords() {
        return goingRecords;
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