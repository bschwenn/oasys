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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "thread")
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "tid", nullable = false)
    private Long tid;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Participates",
            joinColumns = @JoinColumn(name = "tid", referencedColumnName = "tid"),
            inverseJoinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid")
    )
    private List<Person> participants;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "tid")
    @JsonIgnore
    private List<Message> messages;
}
