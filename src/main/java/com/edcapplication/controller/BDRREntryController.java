package com.edcapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.edcapplication.dao.BDRREntryDao;
import com.edcapplication.model.BDRREntry;
import com.edcapplication.service.BDRREntryService;

@RestController
@CrossOrigin
@RequestMapping("/api/edcapplication")
public class BDRREntryController {

    @Autowired
    private BDRREntryService bdrrEntryService;

    @GetMapping("/bdrrEntries")
    public List<BDRREntry> getBDRREntries() {
        return bdrrEntryService.getBDRREntries();
    }

    @GetMapping("/bdrrEntries/{id}")
    public BDRREntry getBDRREntryById(@PathVariable("id") Long id) {
        return bdrrEntryService.getBDRREntryById(id);
    }

    @PostMapping("/bdrrEntries")
    public BDRREntry createBDRREntry(@RequestBody BDRREntryDao dao) {
        return bdrrEntryService.createBDRREntry(dao);
    }

    @PutMapping("/bdrrEntries/{id}")
    public BDRREntry updateBDRREntry(@PathVariable Long id, @RequestBody BDRREntryDao dao) {
        return bdrrEntryService.updateBDRREntry(id, dao);
    }

    @DeleteMapping("/bdrrEntries/{id}")
    public void deleteBDRREntry(@PathVariable Long id) {
        bdrrEntryService.deleteBDRREntry(id);
    }
}
