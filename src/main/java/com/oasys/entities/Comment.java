package com.oasys.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "cid")
    private Long cid;

    @Column(name = "pid", nullable = false)
    private Long pid;

    @Column(name = "creator_uid", nullable = false)
    private Long creatorUid;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "body", nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name="pid", insertable=false, updatable=false)
    private Post post;

    public Comment() {}
}
