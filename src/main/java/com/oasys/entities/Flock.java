package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "flock")
public class Flock {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "gid")
    private Long gid;

    @Column(name = "name")
    private String name;

    @Column(name = "photo_path")
    private String photoPath;

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

    @ManyToMany(mappedBy = "followedFlocks")
    @JsonIgnore
    private Set<Person> followers;

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

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void removeMemberRecord(MemberRecord r) {
        memberRecords.remove(r);
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
