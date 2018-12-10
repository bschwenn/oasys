package com.oasys.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="comment_cid_seq")
    @SequenceGenerator(name="comment_cid_seq", sequenceName="comment_cid_seq", initialValue = 10000, allocationSize = 1)
    @Column(name = "cid", updatable = false, nullable = false)
    private Long cid;

    @Column(name = "pid", nullable = false)
    private Long pid;

    @Column(name = "creator_uid", nullable = false)
    private Long creatorUid;

    @Column(name = "timestamp", nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private Timestamp timestamp;

    @Column(name = "body", nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name="pid", insertable=false, updatable=false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "creator_uid", referencedColumnName = "uid", updatable = false, insertable = false)
    public Person creator;

    public Comment() {}

    public Comment(String body, Long creatorUid, Long pid) {
        this.body = body;
        this.creatorUid = creatorUid;
        this.pid = pid;
    }

    public Long getCid() { return cid; }
    public String getBody() { return body; }
    public Timestamp getTimestamp() { return timestamp; }
    public Person getCreator() {
        return creator;
    }
}
