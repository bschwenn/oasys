package com.oasys.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "member")
@IdClass(MemberRecord.MemberId.class)
public class MemberRecord {
    @Id
    @ManyToOne
    @JoinColumn(name = "uid")
    private Person member;

    @Id
    @ManyToOne
    @JoinColumn(name = "gid")
    private Flock flock;

    @Column(name = "initiator_uid", nullable = false)
    private Long initiatorUid;

    public MemberRecord() {}

    public MemberRecord(Person member, Flock flock, Long initiatorUid) {
        this.member = member;
        this.flock = flock;
        this.initiatorUid = initiatorUid;
    }

    public Person getMember() { return member; }

    public Flock getFlock() {
        return flock;
    }

    public Long getInitiatorUid() {
        return initiatorUid;
    }

    public void setFlock(Flock flock) {
        this.flock = flock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberRecord that = (MemberRecord) o;

        if (!member.equals(that.member)) return false;
        if (!flock.equals(that.flock)) return false;
        return initiatorUid.equals(that.initiatorUid);
    }

    @Override
    public int hashCode() {
        int result = member.hashCode();
        result = 31 * result + flock.hashCode();
        result = 31 * result + initiatorUid.hashCode();
        return result;
    }

    @Embeddable
    public static class MemberId implements Serializable {
        private Long member;
        private Long flock;

        public MemberId() {}

        public MemberId(Long member, Long flock) {
            this.member = member;
            this.flock = flock;
        }

        public Long getMember() {
            return member;
        }

        public Long getFlock() {
            return flock;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MemberId memberId = (MemberId) o;

            if (member != null ? !member.equals(memberId.member) : memberId.member != null) return false;
            return flock != null ? flock.equals(memberId.flock) : memberId.flock == null;
        }

        @Override
        public int hashCode() {
            int result = member.hashCode();
            result = 31 * result + flock.hashCode();
            return result;
        }
    }
}
