package com.oasys.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "follows")
@IdClass(FollowRecord.FollowId.class)
public class FollowRecord {
    @Id
    @ManyToOne
    @JoinColumn(name = "uid")
    private Person follower;

    @Id
    @ManyToOne
    @JoinColumn(name = "gid")
    private Flock flock;

    public FollowRecord() {}

    public FollowRecord(Person follower, Flock flock) {
        this.follower = follower;
        this.flock = flock;
    }

    public Person getFollower() { return follower; }

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

        FollowRecord that = (FollowRecord) o;

        if (!follower.equals(that.follower)) return false;
        return flock.equals(that.flock);
    }

    @Override
    public int hashCode() {
        int result = follower.hashCode();
        result = 31 * result + flock.hashCode();
        return result;
    }

    @Embeddable
    public static class FollowId implements Serializable {
        private Long follower;
        private Long flock;

        public FollowId() {}

        public FollowId(Long follower, Long flock) {
            this.follower = follower;
            this.flock = flock;
        }

        public Long getFollower() {
            return follower;
        }

        public Long getFlock() {
            return flock;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FollowId followerId = (FollowId) o;

            if (follower != null ? !follower.equals(followerId.follower) : followerId.follower != null) return false;
            return flock != null ? flock.equals(followerId.flock) : followerId.flock == null;
        }

        @Override
        public int hashCode() {
            int result = follower.hashCode();
            result = 31 * result + flock.hashCode();
            return result;
        }
    }
}
