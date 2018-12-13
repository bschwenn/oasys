package com.oasys.repository;

import com.oasys.entities.MemberRecord;
import com.oasys.entities.MemberRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRequestRepository extends JpaRepository<MemberRequest, MemberRecord.MemberId> {
}
