package com.oasys.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Entity
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

    @Column(name = "gid")
    private Long groupId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Tags",
            joinColumns = @JoinColumn(name = "eid", referencedColumnName = "eid"),
            inverseJoinColumns = @JoinColumn(name = "iid", referencedColumnName = "iid")
    )
    @JsonIgnore
    private List<Interest> relatedInterests;

    public boolean isGroupEvent() {
        return groupId == null;
    }
}
