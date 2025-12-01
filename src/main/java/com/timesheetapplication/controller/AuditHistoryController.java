package com.timesheetapplication.controller;

import com.timesheetapplication.model.TimesheetEntryAudit;
import com.timesheetapplication.repository.TimesheetEntryAuditHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam LocalDateTime fromDate,
            @RequestParam LocalDateTime toDate) {

        List<TimesheetEntryAudit> history =
                timesheetEntryAuditHistoryRepository.findAuditHistoryTimesheetEntries(userName, fromDate, toDate);

        return ResponseEntity.ok(history);
    }
}
