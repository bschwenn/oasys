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

    public MemberRequest() {}

    public MemberRequest(Person member, Flock flock, Long initiatorUid) {
        this.member = member;
        this.flock = flock;
    }

    public Person getMember() { return member; }

    public Flock getFlock() {
        return flock;
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
        return flock.equals(that.flock);
    }

    @Override
    public int hashCode() {
        int result = member.hashCode();
        result = 31 * result + flock.hashCode();
        return result;
    }
}
