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
@Table(name = "tags")
@IdClass(Tags.TagsId.class)
public class Tags {
    @Id
    @Column(name = "eid", nullable = false)
    private Long eid;
    @Id
    @Column(name = "iid", nullable = false)
    private Long iid;

    @Embeddable
    public static class TagsId implements Serializable {
        private Long eid;
        private Long iid;

        public TagsId(long eid, long iid) {
            this.eid = eid;
            this.iid = iid;
        }
    }
}
