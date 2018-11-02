package com.oasys.tables;

import com.oasys.util.RelationshipId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Getter
@Setter
@Table(name = "interested")
@IdClass(Interested.InterestedId.class)
public class Interested {
    @Embeddable
    public static class InterestedId implements Serializable {
        private Long uid;
        private Long iid;

        public InterestedId(long uid, long iid) {
            this.uid = uid;
            this.iid = iid;
        }
    }

    @Id
    @Column(name = "uid", nullable = false)
    private Long uid;

    @Id
    @Column(name = "iid", nullable = false)
    private Long iid;
}
