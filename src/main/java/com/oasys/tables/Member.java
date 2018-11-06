package com.oasys.tables;

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
@Table(name = "member")
@IdClass(Member.MemberId.class)
public class Member {
    @Id
    @Column(name = "uid", nullable = false)
    private Long uid;
    @Id
    @Column(name = "gid", nullable = false)
    private Long gid;
    @Column(name = "initiator_uid", nullable = false)
    private Long initiatorUid;

    @Embeddable
    public class MemberId implements Serializable {
        private Long uid;
        private Long gid;

        public MemberId(long uid, long gid) {
            this.uid = uid;
            this.gid = gid;
        }
    }
}
