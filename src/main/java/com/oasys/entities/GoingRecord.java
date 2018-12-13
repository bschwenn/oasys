package com.oasys.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "going")
@IdClass(GoingRecord.GoingId.class)
public class GoingRecord {
    @Id
    @ManyToOne
    @JoinColumn(name = "uid")
    private Person person;

    @Id
    @ManyToOne
    @JoinColumn(name = "eid")
    private Event event;

    public GoingRecord() {}

    public GoingRecord(Person person, Event event) {
        this.person = person;
        this.event = event;
    }

    public Person getPerson() { return person; }

    public Event getEvent() {
        return event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoingRecord that = (GoingRecord) o;

        if (!person.equals(that.person)) return false;
        return event.equals(that.event);
    }

    @Override
    public int hashCode() {
        int result = person.hashCode();
        result = 31 * result + event.hashCode();
        return result;
    }

    @Embeddable
    public static class GoingId implements Serializable {
        private Long person;
        private Long event;

        public GoingId() {}

        public GoingId(Long person, Long event) {
            this.person = person;
            this.event = event;
        }

        public Long getperson() {
            return person;
        }

        public Long getevent() {
            return event;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GoingId GoingId = (GoingId) o;

            if (person != null ? !person.equals(GoingId.person) : GoingId.person != null) return false;
            return event != null ? event.equals(GoingId.event) : GoingId.event == null;
        }

        @Override
        public int hashCode() {
            int result = person.hashCode();
            result = 31 * result + event.hashCode();
            return result;
        }
    }
}
