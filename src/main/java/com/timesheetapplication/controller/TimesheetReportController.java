package com.timesheetapplication.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.timesheetapplication.client.UserServiceFeignClient;
import com.timesheetapplication.dto.UserSummaryDTO;
import com.timesheetapplication.projection.TimesheetFillingReportProjection;
import com.timesheetapplication.repository.ProjectRepository;
import com.timesheetapplication.repository.TimesheetEntryRepository;
import com.timesheetapplication.service.ExcelGenerator;
import com.timesheetapplication.service.TimesheetReportService;

@RestController
@RequestMapping("/api/timesheetapplication/report")
@CrossOrigin(origins = "*")
public class TimesheetReportController {

    @Autowired
    private TimesheetReportService timesheetReportService;

    @Autowired
	private TimesheetEntryRepository timesheetEntryRepository;
    
	@Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ExcelGenerator excelGenerator;
    
	@Autowired
	private UserServiceFeignClient userFeignClient;

    //JSON Response
    @GetMapping("/timesheetFilledUser")
    public ResponseEntity<List<TimesheetFillingReportProjection>> getTimesheetFilledUserReport(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String userName) {

        List<TimesheetFillingReportProjection> reportData =
                timesheetReportService.getTimesheetFilledUserReport(startDate, endDate, userName);

        if (reportData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reportData);
    }

    //Excel Export
    @GetMapping("/timesheetFilledUserExcel")
    public ResponseEntity<InputStreamResource> exportReportToFilledUserExcel(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String userName) throws IOException {

        List<TimesheetFillingReportProjection> report =
                timesheetReportService.getTimesheetFilledUserReport(startDate, endDate, userName);

        ByteArrayInputStream excelFile = excelGenerator.exportReportToFilledUserExcel(report);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Timesheet_Filling_Report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(excelFile));
    }
    
    @GetMapping("/timesheetUserProject")
    public ResponseEntity<List<Map<String, Object>>> getTimesheetUserProject(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String projectName) {

        List<Map<String, Object>> reportData = timesheetReportService.generateDynamicPivot(startDate, endDate, userName, projectName);

        if (reportData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reportData);
    }

    @GetMapping("/timesheetUserProjectExcel")
    public ResponseEntity<InputStreamResource> exportReportToUserProjectExcel(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String projectName) throws IOException {

        //List<Map<String, Object>> reportData = timesheetReportService.generateDynamicPivot(startDate, endDate);
        
        List<UserSummaryDTO> allUsers = userFeignClient.getAllOptimizedUsers();
        List<String> projectNames = projectRepository.findAllActiveProjectNames();
        //List<Object[]> raw = timesheetEntryRepository.getUserProjectSummary(startDate, endDate);
        List<Object[]> raw = timesheetEntryRepository.getUserProjectSummary(startDate, endDate, userName, projectName);

        List<Map<String, Object>> finalReportData = timesheetReportService.buildUserProjectPivot(allUsers, raw, projectNames);

        ByteArrayInputStream excelFile = excelGenerator.exportReportToUserProjectExcel(finalReportData, projectNames);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=TimesheetUserProjectReport.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(excelFile));
    }
    
}
