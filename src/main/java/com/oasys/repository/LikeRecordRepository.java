package com.oasys.repository;

import com.oasys.entities.LikeRecord;
import com.oasys.entities.MemberRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRecordRepository extends JpaRepository<LikeRecord, LikeRecord.LikeId> {
}
