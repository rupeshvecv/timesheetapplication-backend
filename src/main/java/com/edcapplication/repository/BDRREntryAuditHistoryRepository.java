package com.edcapplication.repository;

import com.edcapplication.model.BDRREntryAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BDRREntryAuditHistoryRepository extends JpaRepository<BDRREntryAudit, Long> {

    @Query("SELECT t FROM BDRREntryAudit t WHERE t.testBedName = :testBedName AND t.shift = :shift ORDER BY t.changedOn DESC")
    List<BDRREntryAudit> findAuditHistoryBDRREntrys(
            @Param("testBedName") String testBedName,
            @Param("shift") String shift
    );
}
