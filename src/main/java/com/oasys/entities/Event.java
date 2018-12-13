package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="event_eid_seq")
    @SequenceGenerator(name="event_eid_seq", sequenceName="event_eid_seq", initialValue = 10000, allocationSize = 1)
    @Column(name = "eid", updatable = false, nullable = false)
    private Long eid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "time", insertable = true, updatable = false)
    private Date time;

    @Column(name = "location")
    private String location;

    @Column(name = "creator_uid", nullable = false)
    private Long creatorUid;

    @Column(name = "summary")
    private String summary;

    @Column(name = "gid")
    private Long groupId;

    @Column(name = "public")
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "creator_uid", referencedColumnName = "uid", updatable = false, insertable = false)
    public Person creator;

    @ManyToOne
    @JoinColumn(name = "gid", referencedColumnName = "gid", updatable = false, insertable = false)
    public Flock flock;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<GoingRecord> goingRecords;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Tags",
            joinColumns = @JoinColumn(name = "eid", referencedColumnName = "eid"),
            inverseJoinColumns = @JoinColumn(name = "iid", referencedColumnName = "iid")
    )
    @JsonIgnore
    private List<Interest> relatedInterests;

    public Long getEid() {
        return eid;
    }

    public void setEid(Long eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @JsonIgnore
    public List<Interest> getRelatedInterests() {
        return relatedInterests;
    }

    public void setRelatedInterests(List<Interest> relatedInterests) {
        this.relatedInterests = relatedInterests;
    }

    public Person getCreator() {
        return creator;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isGroupEvent() {
        return groupId == null;
    }

    public Flock getFlock() {
        return flock;
    }

    @JsonIgnore
    public Set<GoingRecord> getGoingRecords() {
        return goingRecords;
    }

    public void addGoingRecord(GoingRecord record) {
        goingRecords.add(record);
    }

    public void removeGoingRecord(GoingRecord toRemove) {
        goingRecords.remove(toRemove);
    }

    public int getNumberGoing() {
        return goingRecords.size();
    }
}
