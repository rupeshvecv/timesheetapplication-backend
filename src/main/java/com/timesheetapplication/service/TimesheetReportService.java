package com.timesheetapplication.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timesheetapplication.client.UserServiceFeignClient;
import com.timesheetapplication.dto.UserSummaryDTO;
import com.timesheetapplication.projection.TimesheetFillingReportProjection;
import com.timesheetapplication.repository.ProjectRepository;
import com.timesheetapplication.repository.TimesheetEntryRepository;

@Service
public class TimesheetReportService {

	@Autowired
	private TimesheetEntryRepository timesheetEntryRepository;

	@Autowired
	private UserServiceFeignClient userFeignClient;

	@Autowired
	private ProjectRepository projectRepository;

	public TimesheetReportService(TimesheetEntryRepository timesheetEntryRepository) {
		this.timesheetEntryRepository = timesheetEntryRepository;
	}

	public List<TimesheetFillingReportProjection> getTimesheetFilledUserReport(LocalDate startDate, LocalDate endDate,String username) {

		if (username == null || username.isEmpty()) {
			return timesheetEntryRepository.getTimesheetFilledUserReport(startDate, endDate);
		} else {
			return timesheetEntryRepository.getTimesheetFilledUserReportByUser(startDate, endDate, username);
		}
	}

	public String getDepartmentByUser(String username) {
		return userFeignClient.getDepartmentByUsername(username);
	}

	public String getEmpCodeByUser(String username) {
		return userFeignClient.getEmpCodeByUsername(username);
	}

	public List<Map<String, Object>> generateDynamicPivot(LocalDate start, LocalDate end, String username, String project) {
		List<String> projectNames = projectRepository.findAllActiveProjectNames();
		//List<Object[]> raw = timesheetEntryRepository.getUserProjectSummary(start, end);
		 List<Object[]> raw = timesheetEntryRepository.getUserProjectSummary(start, end, username, project);


		Map<String, Map<String, Object>> pivot = new LinkedHashMap<>();

		for (Object[] row : raw) {
			String user = (String) row[0];
			String proj = (String) row[1];
			Double hours = row[2] instanceof Double ? (Double) row[2] : ((Number) row[2]).doubleValue();

			pivot.putIfAbsent(user, new LinkedHashMap<>());
			Map<String, Object> uRow = pivot.get(user);

			uRow.put("UserName", user);
			uRow.put(proj, hours);
			uRow.put("Total", ((Double) uRow.getOrDefault("Total", 0.0)) + hours);
		}

		// add zero where missing
		for (Map<String, Object> map : pivot.values()) {
			for (String proj : projectNames) {
				map.putIfAbsent(proj, 0.0);
			}
		}

		return new ArrayList<>(pivot.values());
	}

	public List<Map<String, Object>> buildUserProjectPivot(List<UserSummaryDTO> allUsers, List<Object[]> rawData,
			List<String> projectNames) {

		Map<String, Map<String, Object>> userRows = new TreeMap<>(); // TreeMap to sort ascending

		for (UserSummaryDTO user : allUsers) {
			Map<String, Object> row = new LinkedHashMap<>();
			// row.put("UserName", user.userName());
			row.put("UserName", user.firstName() + " " + user.lastName());
			row.put("EmpCode", user.empCode());
			row.put("Designation", user.designationName());
			row.put("Department", user.departmentName());
			row.put("Status", user.statusName());

			for (String project : projectNames)
				row.put(project, 0.0);
			row.put("Total", 0.0);

			userRows.put(user.userName(), row);
		}

		for (Object[] record : rawData) {
			String username = (String) record[0];
			String projectName = (String) record[1];
			Double hours = ((Number) record[2]).doubleValue();

			Map<String, Object> row = userRows.get(username);
			if (row != null) {
				row.put(projectName, hours);
				row.put("Total", ((Double) row.get("Total")) + hours);
			}
		}

		return new ArrayList<>(userRows.values()); // sorted by username
	}

}
