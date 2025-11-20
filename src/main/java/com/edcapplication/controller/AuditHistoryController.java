package com.edcapplication.controller;

import com.edcapplication.model.BDRREntryAudit;
import com.edcapplication.model.TestBedEntryAudit;
import com.edcapplication.repository.BDRREntryAuditHistoryRepository;
import com.edcapplication.repository.TestBedEntryAuditHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/edcapplication/history/audit")
@CrossOrigin
public class AuditHistoryController {

	@Autowired
    private TestBedEntryAuditHistoryRepository testBedEntryAuditHistoryRepository;
	
	@Autowired
    private BDRREntryAuditHistoryRepository bdrrEntryAuditHistoryRepository;

    public AuditHistoryController(TestBedEntryAuditHistoryRepository testBedEntryAuditHistoryRepository) {
        this.testBedEntryAuditHistoryRepository = testBedEntryAuditHistoryRepository;
    }

    @GetMapping("/testBedEntrys")
    public ResponseEntity<List<TestBedEntryAudit>> getAuditTestBedEntrysHistory(
    		 @RequestParam String testBedName,
             @RequestParam String shift) {
        List<TestBedEntryAudit> history = testBedEntryAuditHistoryRepository.findAuditHistoryTestBedEntrys(testBedName,shift);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/bdrrEntries")
    public ResponseEntity<List<BDRREntryAudit>> getAuditBDRRHistory(
    		 @RequestParam String testBedName,
             @RequestParam String shift) {
        List<BDRREntryAudit> history = bdrrEntryAuditHistoryRepository.findAuditHistoryBDRREntrys(testBedName,shift);
        return ResponseEntity.ok(history);
    }
}
