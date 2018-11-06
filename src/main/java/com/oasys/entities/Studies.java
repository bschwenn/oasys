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
@Table(name = "studies")
@IdClass(Studies.StudiesId.class)
public class Studies {
    @Id
    @Column(name = "uid", nullable = false)
    private Long uid;
    @Id
    @Column(name = "iid", nullable = false)
    private Long iid;
    @Column(name = "kind")
    private String kind;

    @Embeddable
    public static class StudiesId implements Serializable {
        private Long uid;
        private Long iid;

        public StudiesId(long uid, long iid) {
            this.uid = uid;
            this.iid = iid;
        }
    }
}
