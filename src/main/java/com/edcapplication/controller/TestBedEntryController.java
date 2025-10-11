package com.edcapplication.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.edcapplication.dao.TestBedEntryDao;
import com.edcapplication.model.TestBedEntry;
import com.edcapplication.model.TestBedEntryEmbeddedId;
import com.edcapplication.service.TestBedEntryService;

@RestController
@CrossOrigin
@RequestMapping("/api/edcapplication")
public class TestBedEntryController {

    @Autowired
    private TestBedEntryService testBedEntryService;

    @GetMapping("/testBedEntrys")
    public List<TestBedEntry> getAllTestBedEntries() {
        return testBedEntryService.getAllTestBedEntries();
    }

    @GetMapping("/testBedEntrys/{testbedId}/{raisedOn}/{shift}")
    public TestBedEntry getTestBedEntryById(
            @PathVariable("testbedId") Long testbedId,
            @PathVariable("raisedOn") String raisedOn,
            @PathVariable("shift") String shift) {
    	 LocalDate raisedOnDate = LocalDate.parse(raisedOn);  // ✅ Fix: Parse String to LocalDate
        TestBedEntryEmbeddedId id = new TestBedEntryEmbeddedId(testbedId, raisedOnDate, shift);
        return testBedEntryService.getTestBedEntryById(id);
    }

    @PostMapping("/testBedEntrys")
    public TestBedEntry createTestBedEntry(@RequestBody TestBedEntryDao dao) {
        return testBedEntryService.createTestBedEntry(dao);
    }

    @PutMapping("/testBedEntrys")
    public TestBedEntry updateTestBedEntry(@RequestBody TestBedEntryDao dao) {
        return testBedEntryService.updateTestBedEntry(dao);
    }

    @DeleteMapping("/testBedEntrys/{testbedId}/{raisedOn}/{shift}")
    public void deleteTestBedEntry(
            @PathVariable("testbedId") Long testbedId,
            @PathVariable("raisedOn") String raisedOn,
            @PathVariable("shift") String shift) {
    	 LocalDate raisedOnDate = LocalDate.parse(raisedOn);  // ✅ Fix: Parse String to LocalDate
         TestBedEntryEmbeddedId id = new TestBedEntryEmbeddedId(testbedId, raisedOnDate, shift);
        testBedEntryService.deleteTestBedEntry(id);
    }
}
