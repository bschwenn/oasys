package com.oasys.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "likes")
@IdClass(LikeRecord.LikeId.class)
public class LikeRecord {
    @Id
    @ManyToOne
    @JoinColumn(name = "uid")
    private Person person;

    @Id
    @ManyToOne
    @JoinColumn(name = "pid")
    private Post post;

    public LikeRecord() {}

    public LikeRecord(Person person, Post post) {
        this.person = person;
        this.post = post;
    }

    public Person getPerson() { return person; }

    public Post getPost() {
        return post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LikeRecord that = (LikeRecord) o;

        if (!person.equals(that.person)) return false;
        return post.equals(that.post);
    }

    @Override
    public int hashCode() {
        int result = person.hashCode();
        result = 31 * result + post.hashCode();
        return result;
    }

    @Embeddable
    public static class LikeId implements Serializable {
        private Long person;
        private Long post;

        public LikeId() {}

        public LikeId(Long person, Long post) {
            this.person = person;
            this.post = post;
        }

        public Long getperson() {
            return person;
        }

        public Long getpost() {
            return post;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LikeId LikeId = (LikeId) o;

            if (person != null ? !person.equals(LikeId.person) : LikeId.person != null) return false;
            return post != null ? post.equals(LikeId.post) : LikeId.post == null;
        }

        @Override
        public int hashCode() {
            int result = person.hashCode();
            result = 31 * result + post.hashCode();
            return result;
        }
    }
}
