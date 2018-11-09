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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Getter
@Setter
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
}
