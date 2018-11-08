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

@Data
@Entity
@Getter
@Setter
@Table(name = "flock")
public class Flock {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "gid")
    private Long gid;

    @Column(name = "name")
    private String name;

    @Column(name = "photo_path")
    private String photoPath;
}
