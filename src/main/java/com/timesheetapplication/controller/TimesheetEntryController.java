package com.timesheetapplication.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.timesheetapplication.dto.MapperUtil;
import com.timesheetapplication.dto.TimesheetEntryDto;
import com.timesheetapplication.model.TimesheetEntry;
import com.timesheetapplication.projection.TimesheetEntryProjection;
import com.timesheetapplication.service.TimesheetEntryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/timesheetapplication")
@RequiredArgsConstructor
public class TimesheetEntryController {

	@Autowired
	private TimesheetEntryService timesheetEntryService;

	/*@GetMapping("/timesheetEntry")
	public ResponseEntity<List<TimesheetEntryDto>> getAll() {
		List<TimesheetEntry> entries = timesheetEntryService.getAll();

		List<TimesheetEntryDto> result = entries.stream().map(MapperUtil::toDto).collect(Collectors.toList());
		return ResponseEntity.ok(result);
	}*/

	@GetMapping("/timesheetEntry/allTimesheetEntries")
	public ResponseEntity<List<TimesheetEntryProjection>> getAllOptimizedTimesheetEntries(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startEntryDate,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endEntryDate) {
	    List<TimesheetEntryProjection> result = timesheetEntryService.getAllOptimizedTimesheetEntries(startEntryDate, endEntryDate);

	    if (result.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }

	    return ResponseEntity.ok(result);
	}
	
	/*@GetMapping("/timesheetEntry/fromto")
	public List<TimesheetEntryDto> query(@RequestParam String user,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
		return timesheetEntryService.findForUserBetween(user, from, to).stream().map(MapperUtil::toDto).toList();
	}*/
	
	@GetMapping("/timesheetEntry/allDateUserWiseTimesheetEntries")
	public ResponseEntity<List<TimesheetEntryProjection>> getAllDateUserWiseOptimizedTimesheetEntries(
	        @RequestParam String user,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startEntryDate,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endEntryDate) {

	    List<TimesheetEntryProjection> result = timesheetEntryService.getAllDateUserWiseOptimizedTimesheetEntries(user, startEntryDate, endEntryDate);

	    if (result.isEmpty()) {
	        return ResponseEntity.noContent().build();
	    }

	    return ResponseEntity.ok(result);
	}

	@GetMapping("/timesheetEntry/{id}")
	public ResponseEntity<TimesheetEntryDto> getById(@PathVariable Long id) {
		TimesheetEntry entry = timesheetEntryService.getById(id);
		return ResponseEntity.ok(MapperUtil.toDto(entry));
	}

	@PostMapping("/timesheetEntry/saveAll")
    public ResponseEntity<?> saveAllTimesheetEntry(@RequestBody TimesheetEntryDto dto) throws Exception {
        return ResponseEntity.ok(timesheetEntryService.saveAllTimesheetEntry(dto));
    }

	@PutMapping("/updateAll")
	public ResponseEntity<?> updateMultipleEntries(@RequestBody List<TimesheetEntryDto> entries) {
	    return ResponseEntity.ok(timesheetEntryService.updateAllTimesheetEntry(entries));
	}

	@DeleteMapping("/timesheetEntry")
	public ResponseEntity<String> deleteTimesheetEntry(
		@RequestParam String user,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate) {
	    timesheetEntryService.deleteTimesheetEntry(entryDate, user);
	    return ResponseEntity.ok("Timesheet entry deleted successfully.");
	}
}
