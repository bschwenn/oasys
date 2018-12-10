package com.oasys.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "studies")
@IdClass(StudyRecord.StudyRecordId.class)
public class StudyRecord {
    @Id
    @ManyToOne
    @JoinColumn(name = "uid", referencedColumnName = "uid")
    private Person person;

    @Id
    @ManyToOne
    @JoinColumn(name = "iid", referencedColumnName = "iid")
    private Interest interest;

    @Column(name = "kind", nullable = false)
    private String kind;

    public Interest getInterest() {
        return interest;
    }

    public String getKind() {
        return kind;
    }

    public StudyRecord() {}

    public StudyRecord(Person member, Interest interest, String kind) {
        this.person = member;
        this.interest = interest;
        this.kind = kind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudyRecord that = (StudyRecord) o;

        if (!person.equals(that.person)) return false;
        if (!interest.equals(that.interest)) return false;
        return kind.equals(that.kind);
    }

    @Override
    public int hashCode() {
        int result = person.hashCode();
        result = 31 * result + interest.hashCode();
        result = 31 * result + kind.hashCode();
        return result;
    }

    @Embeddable
    public static class StudyRecordId implements Serializable {
        private Long person;
        private Long interest;

        public StudyRecordId() {}

        public StudyRecordId(Long uid, Long iid) {
            this.person = uid;
            this.interest = iid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StudyRecordId studyRecordId = (StudyRecordId) o;

            if (!person.equals(studyRecordId.person)) return false;
            return interest.equals(studyRecordId.interest);
        }

        @Override
        public int hashCode() {
            int result = person.hashCode();
            result = 31 * result + interest.hashCode();
            return result;
        }
    }
}
