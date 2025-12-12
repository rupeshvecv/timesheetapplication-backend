package com.timesheetapplication.controller;

import com.timesheetapplication.model.TimesheetEntryAudit;
import com.timesheetapplication.repository.TimesheetEntryAuditHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/timesheetapplication/history/audit")
@CrossOrigin
public class AuditHistoryController {

    @Autowired
    private TimesheetEntryAuditHistoryRepository timesheetEntryAuditHistoryRepository;

    @GetMapping("/timesheetEntries")
    public ResponseEntity<List<TimesheetEntryAudit>> getTimesheetAuditHistory(
            @RequestParam String userName,
            @RequestParam String fromDate,
            @RequestParam String toDate) {

        // Convert input strings to LocalDateTime
        LocalDateTime fromDateTime = parseToDateTime(fromDate, true);
        LocalDateTime toDateTime   = parseToDateTime(toDate, false);

        // Pass converted LocalDateTime to repository
        List<TimesheetEntryAudit> history =
                timesheetEntryAuditHistoryRepository.findAuditHistoryTimesheetEntries(
                        userName,
                        fromDateTime,
                        toDateTime
                );

        return ResponseEntity.ok(history);
    }
    
    private LocalDateTime parseToDateTime(String date, boolean startOfDay) {
        try {
            // Example: "2025-12-09"
            LocalDate parsedDate = LocalDate.parse(date);

            return startOfDay
                    ? parsedDate.atStartOfDay()           // 2025-12-09 00:00:00
                    : parsedDate.atTime(23, 59, 59);      // 2025-12-09 23:59:59

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Expected: yyyy-MM-dd");
        }
    }

}
