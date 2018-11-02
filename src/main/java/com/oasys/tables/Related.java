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
@Table(name = "related")
@IdClass(Related.RelatedId.class)
public class Related {
    @Embeddable
    public static class RelatedId implements Serializable {
        private Long gid;
        private Long iid;

        public RelatedId(long gid, long iid) {
            this.gid = gid;
            this.iid = iid;
        }
    }

    @Id
    @Column(name = "gid")
    private Long gid;

    @Id
    @Column(name = "iid")
    private Long iid;
}
