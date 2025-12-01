package com.timesheetapplication.repository;

import com.timesheetapplication.model.TimesheetEntryAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimesheetEntryAuditHistoryRepository extends JpaRepository<TimesheetEntryAudit, Long> {

    @Query("SELECT t FROM TimesheetEntryAudit t " +
           "WHERE t.userName = :userName " +
           "AND t.changedOn BETWEEN :fromDate AND :toDate " +
           "ORDER BY t.changedOn DESC")
    List<TimesheetEntryAudit> findAuditHistoryTimesheetEntries(
            @Param("userName") String userName,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
}
