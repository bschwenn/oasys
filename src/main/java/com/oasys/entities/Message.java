package com.oasys.entities;

import javax.persistence.*;
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

    @Column(name = "timestamp", nullable = false, updatable = false, insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private Timestamp timestamp;

    @Column(name = "body", nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name="tid", insertable=false, updatable=false)
    private Thread thread;
}
