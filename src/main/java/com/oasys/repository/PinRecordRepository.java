package com.oasys.repository;

import com.oasys.entities.LikeRecord;
import com.oasys.entities.MemberRecord;
import com.oasys.entities.PinRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRecordRepository extends JpaRepository<PinRecord, PinRecord.PinId> {
}
