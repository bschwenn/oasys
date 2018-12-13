package com.oasys.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "pin")
@IdClass(PinRecord.PinId.class)
public class PinRecord {
    @Id
    @ManyToOne
    @JoinColumn(name = "uid")
    private Person person;

    @Id
    @ManyToOne
    @JoinColumn(name = "pid")
    private Post post;

    public PinRecord() {}

    public PinRecord(Person person, Post post) {
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

        PinRecord that = (PinRecord) o;

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
    public static class PinId implements Serializable {
        private Long person;
        private Long post;

        public PinId() {}

        public PinId(Long person, Long post) {
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

            PinId PinId = (PinId) o;

            if (person != null ? !person.equals(PinId.person) : PinId.person != null) return false;
            return post != null ? post.equals(PinId.post) : PinId.post == null;
        }

        @Override
        public int hashCode() {
            int result = person.hashCode();
            result = 31 * result + post.hashCode();
            return result;
        }
    }
}
