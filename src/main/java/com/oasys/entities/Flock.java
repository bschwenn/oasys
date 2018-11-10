package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Getter
@Setter
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

    @ManyToMany()
    @JoinTable(
            name = "Moderates",
            joinColumns = @JoinColumn(name = "gid", referencedColumnName = "gid"),
            inverseJoinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid")
    )
    private List<Person> admins;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Member",
            joinColumns = @JoinColumn(name = "gid", referencedColumnName = "gid"),
            inverseJoinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid")
    )
    @JsonIgnore
    private List<Person> members;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Follows",
            joinColumns = @JoinColumn(name = "gid", referencedColumnName = "gid"),
            inverseJoinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid")
    )
    @JsonIgnore
    private List<Person> followers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Related",
            joinColumns = @JoinColumn(name = "gid", referencedColumnName = "gid"),
            inverseJoinColumns = @JoinColumn(name = "iid", referencedColumnName = "iid")
    )
    private List<Interest> interests;

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

    public List<Person> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Person> admins) {
        this.admins = admins;
    }

    public List<Person> getMembers() {
        return members;
    }

    public void setMembers(List<Person> members) {
        this.members = members;
    }

    public List<Person> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Person> followers) {
        this.followers = followers;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

}
