package com.oasys.repository;

import com.oasys.entities.FollowRecord;
import com.oasys.entities.MemberRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRecordRepository extends JpaRepository<FollowRecord, FollowRecord.FollowId> {
}
