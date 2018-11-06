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
@Table(name = "participates")
@IdClass(Participates.ParticipatesId.class)
public class Participates {
    @Id
    @Column(name = "uid", nullable = false)
    private Long uid;
    @Id
    @Column(name = "tid", nullable = false)
    private Long tid;

    @Embeddable
    public class ParticipatesId implements Serializable {
        private Long uid;
        private Long tid;

        public ParticipatesId(long uid, long tid) {
            this.uid = uid;
            this.tid = tid;
        }
    }
}
