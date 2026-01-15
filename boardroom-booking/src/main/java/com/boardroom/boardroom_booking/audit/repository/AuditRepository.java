package com.boardroom.boardroom_booking.audit.repository;

import com.boardroom.boardroom_booking.audit.entity.AuditTrail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface AuditRepository extends JpaRepository<AuditTrail, Long> {
    @Query("SELECT DISTINCT adt FROM AuditTrail adt  WHERE  adt.active = true")
    Page<AuditTrail> findAllByInstitutionId(Pageable pageable, Long institutionId);
}
