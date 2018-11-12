package com.oasys.repository;

import com.oasys.entities.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRecordRepository extends JpaRepository<StudyRecord, StudyRecord.StudyRecordId> {
}
