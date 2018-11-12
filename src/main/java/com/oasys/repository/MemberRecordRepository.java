package com.oasys.repository;

import com.oasys.entities.MemberRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRecordRepository extends JpaRepository<MemberRecord, MemberRecord.MemberId> {
}
