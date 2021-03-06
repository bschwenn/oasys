package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="post_pid_seq")
    @SequenceGenerator(name="post_pid_seq", sequenceName="post_pid_seq", initialValue = 10000, allocationSize = 1)
    @Column(name = "pid", updatable = false, nullable = false)
    private Long pid;

    @Column(name = "gid", nullable = false)
    private Long gid;

    @Column(name = "creator_uid", nullable = false)
    private Long creatorUid;

    @Column(name = "timestamp", nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private Timestamp timestamp;

    @Column(name = "kind", nullable = false)
    private String kind;

    @Column(name = "body", nullable = false)
    private String body;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    @JsonIgnore
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn(name = "creator_uid", referencedColumnName = "uid", updatable = false, insertable = false)
    public Person creator;

    @ManyToOne
    @JoinColumn(name = "gid", referencedColumnName = "gid", updatable = false, insertable = false)
    public Flock flock;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<LikeRecord> likeRecords;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<PinRecord> pinRecords;

    public Person getCreator() {
        return creator;
    }

    public Flock getFlock() {
        return flock;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getGid() {
        return gid;
    }

    public int getNumberOfComments() { return comments.size(); }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @JsonIgnore
    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) { comments.add(comment); }

    public void removeLike(LikeRecord toRemove) {
        likeRecords.remove(toRemove);
    }

    public void addPinRecord(PinRecord record) {
        pinRecords.add(record);
    }

    public void addLikeRecord(LikeRecord record) {
        likeRecords.remove(record);
    }

    public void removePinRecord(PinRecord toRemove) {
        pinRecords.remove(toRemove);
    }


    public int getNumberOfLikes() {
        return likeRecords.size();
    }

    public int getNumberOfPins() {
        return pinRecords.size();
    }

    @JsonIgnore
    public Set<LikeRecord> getLikeRecords() {
        return likeRecords;
    }

    @JsonIgnore
    public Set<PinRecord> getPinRecords() {
        return pinRecords;
    }
}
