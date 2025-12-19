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
import com.timesheetapplication.exception.BusinessException;
import com.timesheetapplication.projection.TimesheetFillingReportProjection;
import com.timesheetapplication.repository.ProjectRepository;
import com.timesheetapplication.repository.TimesheetEntryRepository;

import feign.FeignException;

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

	public List<TimesheetFillingReportProjection> getTimesheetFilledUserReport(LocalDate startDate, LocalDate endDate, String username) {
		//1️.Validate dates
	    if (startDate == null || endDate == null) {
	        throw new BusinessException("TS_011");
	    }

	    if (startDate.isAfter(endDate)) {
	        throw new BusinessException("TS_012",startDate,endDate);
	    }

	    List<TimesheetFillingReportProjection> tsFillingResult;
	    //2️.Fetch data
	    if (username == null || username.isBlank()) {
	    	tsFillingResult = timesheetEntryRepository.getTimesheetFilledUserReport(startDate, endDate);
	    } else {
	    	tsFillingResult = timesheetEntryRepository.getTimesheetFilledUserReportByUser(startDate, endDate, username);
	    }
	    
	    //3️.Handle empty result
	    if (tsFillingResult == null || tsFillingResult.isEmpty()) {
	        if (username == null || username.isBlank()) {
	            throw new BusinessException("TS_015",startDate,endDate);
	        } else {
	            throw new BusinessException("TS_016",username,startDate,endDate);
	        }
	    }

	    return tsFillingResult;
		/*if (username == null || username.isEmpty()) {
	        return timesheetEntryRepository.getTimesheetFilledUserReport(startDate, endDate);
	    } else {
	        return timesheetEntryRepository.getTimesheetFilledUserReportByUser(startDate, endDate, username);
	    }*/
	}

	public String getDepartmentByUser(String username) {
		//return userFeignClient.getDepartmentByUsername(username);
		//1️.Validate input
	    if (username == null || username.isBlank()) {
	        throw new BusinessException("USR_001");
	    }

	    try {
	        String department = userFeignClient.getDepartmentByUsername(username);

	        //2️.Handle empty response
	        if (department == null || department.isBlank()) {
	            throw new BusinessException("USR_002", username);
	        }

	        return department;

	    } catch (FeignException.NotFound ex) {
	        //3️.User not found in user-service
	    	throw new BusinessException("USR_002", username);

	    } catch (FeignException ex) {
	        //4️.User service down / timeout / 5xx
	    	throw new BusinessException("USR_003");
	    }
	}

	public String getEmpCodeByUser(String username) {
		//return userFeignClient.getEmpCodeByUsername(username);
		//1️.Input validation
	    if (username == null || username.isBlank()) {
	        throw new BusinessException("USR_001");
	    }

	    try {
	        String empCode = userFeignClient.getEmpCodeByUsername(username);

	        //2️.Handle empty response
	        if (empCode == null || empCode.isBlank()) {
	            throw new BusinessException("USR_004", username);
	        }

	        return empCode;

	    } catch (FeignException.NotFound ex) {
	        //3️.User not found in user-service
	        throw new BusinessException("USR_004", username);

	    } catch (FeignException ex) {
	        //4️.User service down / timeout / 5xx
	        throw new BusinessException("USR_003");
	    }
	}

	public List<Map<String, Object>> generateDynamicPivot(LocalDate start, LocalDate end, String userName, String projectName) {
		//1️.Validate dates
	    if (start == null || end == null) {
	        throw new BusinessException("TS_011");
	    }

	    if (start.isAfter(end)) {
	    	throw new BusinessException("TS_012",start,end);
	    }
	    
	    //2️.Fetch active projects
		//List<String> projectNames = projectRepository.findAllActiveProjectNames();
	    List<String> projectNames = projectRepository.findAllActiveProjectNames();
	    if (projectNames == null || projectNames.isEmpty()) {
	        throw new BusinessException("TS_017");
	    }

	    //3️.Fetch raw data
	    List<Object[]> raw = timesheetEntryRepository.getUserProjectSummary(start, end, userName, projectName);
	    if (raw == null || raw.isEmpty()) {
	        throw new BusinessException("TS_018",userName != null ? userName : "ALL",start,end);
	    }
	    
	    //4️.Build pivot
		Map<String, Map<String, Object>> pivot = new LinkedHashMap<>();

		for (Object[] row : raw) {
			String user = (String) row[0];
			String project = (String) row[1];
			Double hours = row[2] instanceof Double ? (Double) row[2] : ((Number) row[2]).doubleValue();

			pivot.putIfAbsent(user, new LinkedHashMap<>());
			Map<String, Object> uRow = pivot.get(user);

			uRow.put("UserName", user);
			uRow.put(project, hours);
			uRow.put("Total", ((Double) uRow.getOrDefault("Total", 0.0)) + hours);
		}

		//add zero where missing
		for (Map<String, Object> map : pivot.values()) {
			for (String proj : projectNames) {
				map.putIfAbsent(proj, 0.0);
			}
		}

		return new ArrayList<>(pivot.values());
	}

	public List<Map<String, Object>> buildUserProjectPivot(List<UserSummaryDTO> allUsers, List<Object[]> rawData,List<String> projectNames) {

		//1️.Validate inputs
	    if (allUsers == null || allUsers.isEmpty()) {
	        throw new BusinessException("TS_019");
	    }

	    if (projectNames == null || projectNames.isEmpty()) {
	        throw new BusinessException("TS_020");
	    }

	    if (rawData == null || rawData.isEmpty()) {
	        throw new BusinessException("TS_021");
	    }
	    
	    //2️.TreeMap ensures ascending sort by username
		Map<String, Map<String, Object>> userRows = new TreeMap<>(); // TreeMap to sort ascending

		//3️.Initialize user rows with zero values
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

		//4️.Fill actual hours
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

	public List<Map<String, Object>> buildUserProjectPivotFilter(List<UserSummaryDTO> allUsers, List<Object[]> rawData,List<String> projectNames) {
		//1️.Validate inputs
	    if (allUsers == null || allUsers.isEmpty()) {
	        throw new BusinessException("TS_019");
	    }

	    if (projectNames == null || projectNames.isEmpty()) {
	        throw new BusinessException("TS_020");
	    }

	    if (rawData == null || rawData.isEmpty()) {
	        throw new BusinessException("TS_021");
	    }
	    
	    //2️.Create lookup map for users (performance fix)
		Map<String, Map<String, Object>> userRows = new TreeMap<>(); // sorted ascending

		//Create rows only for users who exist in rawData
		for (Object[] record : rawData) {
			String username = (String) record[0];

			//find matching user details only for users present in rawData
			UserSummaryDTO matchedUser = allUsers.stream().filter(u -> u.userName().equals(username)).findFirst()
					.orElse(null);

			if (matchedUser != null && !userRows.containsKey(username)) {
				Map<String, Object> row = new LinkedHashMap<>();

				row.put("UserName", matchedUser.firstName() + " " + matchedUser.lastName());
				row.put("EmpCode", matchedUser.empCode());
				row.put("Designation", matchedUser.designationName());
				row.put("Department", matchedUser.departmentName());
				row.put("Status", matchedUser.statusName());

				for (String project : projectNames) {
					row.put(project, 0.0);
				}
				row.put("Total", 0.0);

				userRows.put(username, row);
			}
		}
		if (userRows.isEmpty()) {
	        throw new BusinessException("TS_022");
	    }
		//Fill hours data now
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
		return new ArrayList<>(userRows.values());
	}

}
