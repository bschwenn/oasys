package com.oasys.tables;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@Getter
@Setter
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "cid")
    private Long cid;

    @Column(name = "pid", nullable = false)
    private Long pid;

    @Column(name = "creater_uid", nullable = false)
    private Long createrUid;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "body", nullable = false)
    private String body;
}
