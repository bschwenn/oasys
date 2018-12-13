package com.oasys.repository;

import com.oasys.entities.GoingRecord;
import com.oasys.entities.LikeRecord;
import com.oasys.entities.MemberRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoingRecordRepository extends JpaRepository<GoingRecord, GoingRecord.GoingId> {
}
