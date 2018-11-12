package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "pid")
    private Long pid;

    @Column(name = "gid", nullable = false)
    private Long gid;

    @Column(name = "creater_uid", nullable = false)
    private Long createrUid;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "kind", nullable = false)
    private String kind;

    @Column(name = "body", nullable = false)
    private String body;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid")
    @JsonIgnore
    private List<Comment> comments;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getCreaterUid() {
        return createrUid;
    }

    public void setCreaterUid(Long createrUid) {
        this.createrUid = createrUid;
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

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
