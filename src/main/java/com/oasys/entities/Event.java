package com.oasys.entities;

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
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "eid")
    private Long eid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "time", nullable = false)
    private Timestamp time;

    @Column(name = "location")
    private String location;

    @Column(name = "creater_uid", nullable = false)
    private Long createrUid;

    @Column(name = "body")
    private String summary;
}
