package com.timesheetapplication.controller;
 
import java.time.LocalDate;
import java.util.List;
 
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import com.timesheetapplication.model.HolidayMaster;
import com.timesheetapplication.service.HolidayMasterService;
 
@RestController
@RequestMapping("/api/timesheetapplication/holidays")
@CrossOrigin
public class HolidayMasterController {
 
    private final HolidayMasterService holidayMasterService;
 
    public HolidayMasterController(HolidayMasterService holidayMasterService) {
        this.holidayMasterService = holidayMasterService;
    }
 
    @PostMapping
    public ResponseEntity<HolidayMaster> createHoliday(@RequestBody HolidayMaster holiday) {
        return ResponseEntity.ok(holidayMasterService.createHoliday(holiday));
    }
 
    @PutMapping("/{id}")
    public ResponseEntity<HolidayMaster> updateHoliday(@PathVariable Long id, @RequestBody HolidayMaster holiday) {
        return ResponseEntity.ok(holidayMasterService.updateHoliday(id, holiday));
    }
  
    @GetMapping("/{date}")
    public ResponseEntity<HolidayMaster> getHolidayByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(holidayMasterService.getHolidayByDate(date));
    }
 
    @GetMapping("/active")
    public ResponseEntity<List<HolidayMaster>> getAllActiveHolidays() {
        return ResponseEntity.ok(holidayMasterService.getAllActiveHolidays());
    }
 
    @GetMapping
    public ResponseEntity<List<HolidayMaster>> getAllHolidays() {
        return ResponseEntity.ok(holidayMasterService.getAllHolidays());
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHoliday(@PathVariable Long id) {
        holidayMasterService.deleteHoliday(id);
        return ResponseEntity.ok("Holiday deleted successfully");
    }
}
 