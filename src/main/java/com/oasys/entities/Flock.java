package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oasys.repository.MemberRecordRepository;
import com.oasys.repository.MemberRequestRepository;

import javax.persistence.*;
import java.lang.reflect.Member;
import java.util.Set;

@Entity
@Table(name = "flock")
public class Flock {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="flock_gid_seq")
    @SequenceGenerator(name="flock_gid_seq", sequenceName="flock_gid_seq", allocationSize = 1)
    @Column(name = "gid")
    private Long gid;

    @Column(name = "name")
    private String name;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "Moderates",
            joinColumns = @JoinColumn(name = "gid", referencedColumnName = "gid"),
            inverseJoinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid")
    )
    private Set<Person> admins;

    @OneToMany(mappedBy = "flock", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<MemberRecord> memberRecords;

    @OneToMany(mappedBy = "flock", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<MemberRequest> memberRequests;

    @OneToMany(mappedBy = "flock", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<FollowRecord> followerRecords;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "Related",
            joinColumns = @JoinColumn(name = "gid", referencedColumnName = "gid"),
            inverseJoinColumns = @JoinColumn(name = "iid", referencedColumnName = "iid")
    )
    @JsonIgnore
    private Set<Interest> relatedInterests;

    @OneToMany(mappedBy = "flock", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts;

    @OneToMany(mappedBy = "flock", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Event> events;

    public Long getGid() {
        return gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @JsonIgnore
    public Set<Person> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Person> admins) {
        this.admins = admins;
    }

    public void addMemberRecord(MemberRecord r) {
        memberRecords.add(r);
    }

    public void addFollowerRecord(FollowRecord r) {
        followerRecords.add(r);
    }

    public void removeFollowerRecord(FollowRecord r) { followerRecords.remove(r); }

    public void addRelatedInterest(Interest i) { relatedInterests.add(i); }

    public void removeMemberRecord(MemberRecord r) {
        memberRecords.remove(r);
    }

    public Set<MemberRequest> getMemberRequests() {
        return memberRequests;
    }

    public void addMemberRequest(Person user, long initiatorUid, MemberRequestRepository repository) {
        MemberRequest memberRequest = new MemberRequest(user, this, initiatorUid);
        repository.save(memberRequest);
        memberRequests.add(memberRequest);
        user.addMemberRequest(memberRequest);
    }

    public void removeMemberRequest(Person user, MemberRequestRepository repository) {
        MemberRequest toRemove = null;
        for (MemberRequest memberRequest : memberRequests) {
            if (memberRequest.getMember().equals(user)) {
                toRemove = memberRequest;
                break;
            }
        }
        if (toRemove != null) {
            memberRequests.remove(toRemove);
            repository.delete(toRemove);
            user.removeMemberRequest(toRemove);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flock flock = (Flock) o;

        if (!gid.equals(flock.gid)) return false;
        return name.equals(flock.name);
    }

    @Override
    public int hashCode() {
        int result = gid.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
