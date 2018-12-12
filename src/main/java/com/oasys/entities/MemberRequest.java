package com.oasys.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "memberrequest")
@IdClass(MemberRecord.MemberId.class)
public class MemberRequest {
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

    public MemberRequest() {}

    public MemberRequest(Person member, Flock flock, Long initiatorUid) {
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

        MemberRequest that = (MemberRequest) o;

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
}
