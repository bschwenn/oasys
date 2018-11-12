package com.oasys.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "mid")
    private Long mid;

    @Column(name = "tid", nullable = false)
    private Long tid;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "body", nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name="tid", insertable=false, updatable=false)
    private Thread thread;
}
