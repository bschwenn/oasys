package com.oasys.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Getter
@Setter
@Table(name = "moderates")
@IdClass(Moderates.ModeratesId.class)
public class Moderates {
    @Id
    @Column(name = "uid", nullable = false)
    private Long uid;
    @Id
    @Column(name = "gid", nullable = false)
    private Long gid;

    @Embeddable
    public class ModeratesId implements Serializable {
        private Long uid;
        private Long gid;

        public ModeratesId(long uid, long gid) {
            this.uid = uid;
            this.gid = gid;
        }
    }
}
