package com.edcapplication.repository;

import com.edcapplication.model.TestBedEntryAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestBedEntryAuditHistoryRepository extends JpaRepository<TestBedEntryAudit, Long> {

    @Query("SELECT t FROM TestBedEntryAudit t WHERE t.testBedName = :testBedName AND t.shift = :shift ORDER BY t.changedOn DESC")
    List<TestBedEntryAudit> findAuditHistoryTestBedEntrys(
            @Param("testBedName") String testBedName,
            @Param("shift") String shift
    );
}
