package com.oasys.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "interest")
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "iid")
    private Long iid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_study", nullable = false)
    private boolean isStudy;

    @OneToMany(mappedBy = "interest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudyRecord> studyRecords;

    @ManyToMany(mappedBy = "interests")
    private Set<Person> interested;

    @ManyToMany(mappedBy = "relatedInterests")
    private Set<Flock> related;

    public void addInterested(Person p) {
        interested.add(p);
    }



    public void addStudyRecord(StudyRecord studyRecord) {
        studyRecords.add(studyRecord);
    }

    public void removeStudyRecord(StudyRecord studyRecord) {
        studyRecords.remove(studyRecord);
    }

    public Long getIid() {
        return iid;
    }

    public String getName() {
        return name;
    }

    public boolean isStudy() {
        return isStudy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Interest interest = (Interest) o;

        if (isStudy != interest.isStudy) return false;
        if (!iid.equals(interest.iid)) return false;
        return name.equals(interest.name);
    }

    @Override
    public int hashCode() {
        int result = iid.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (isStudy ? 1 : 0);
        return result;
    }
}
