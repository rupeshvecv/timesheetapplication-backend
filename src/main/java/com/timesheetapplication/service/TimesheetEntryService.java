package com.timesheetapplication.service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheetapplication.dto.TimesheetEntryDto;
import com.timesheetapplication.dto.TimesheetRowDto;
import com.timesheetapplication.exception.BusinessException;
import com.timesheetapplication.model.Activity;
import com.timesheetapplication.model.Category;
import com.timesheetapplication.model.Platform;
import com.timesheetapplication.model.Project;
import com.timesheetapplication.model.TimesheetEntry;
import com.timesheetapplication.model.TimesheetEntryAudit;
import com.timesheetapplication.projection.TimesheetEntryProjection;
import com.timesheetapplication.repository.ActivityRepository;
import com.timesheetapplication.repository.CategoryRepository;
import com.timesheetapplication.repository.PlatformRepository;
import com.timesheetapplication.repository.ProjectRepository;
import com.timesheetapplication.repository.TimesheetEntryAuditHistoryRepository;
import com.timesheetapplication.repository.TimesheetEntryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimesheetEntryService {

	@Autowired
	private TimesheetEntryRepository timesheetEntryRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private PlatformRepository platformRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
    private TimesheetEntryAuditHistoryRepository timesheetEntryAuditHistoryRepository;
	
	public TimesheetEntry getById(Long id) {
		return timesheetEntryRepository.findById(id)
				.orElseThrow(() -> new BusinessException("TS_007", id));
	}

	/*public List<TimesheetEntry> getAll() {
		return timesheetEntryRepository.findAll();
	}*/
	
	public List<TimesheetEntryProjection> getAllOptimizedTimesheetEntries(LocalDate startEntryDate, LocalDate endEntryDate) {
        //return timesheetEntryRepository.findAllOptimizedTimesheetEntries(startEntryDate,endEntryDate);
		//1.Validate input
	    if	(startEntryDate == null || endEntryDate == null) {
	        throw new BusinessException("TS_011");
	    }
	    if (startEntryDate.isAfter(endEntryDate)) {
	        throw new BusinessException("TS_012", startEntryDate, endEntryDate);
	    }
	    //2.Fetch data
	    List<TimesheetEntryProjection> allTSResult = timesheetEntryRepository.findAllOptimizedTimesheetEntries(startEntryDate, endEntryDate);

	    //3.Handle no data case (important for UI)
	    if (allTSResult == null || allTSResult.isEmpty()) {
	        throw new BusinessException("TS_013",startEntryDate.toString(),endEntryDate.toString());
	    }

	    return allTSResult;
    }
	
	/*public List<TimesheetEntry> findForUserBetween(String user, LocalDate from, LocalDate to) {
		return timesheetEntryRepository.findByUserNameAndEntryDateBetween(user, from, to);
	}*/
	
	public List<TimesheetEntryProjection> getAllDateUserWiseOptimizedTimesheetEntries(String user, LocalDate startEntryDate, LocalDate endEntryDate) {
        //return timesheetEntryRepository.findAllDateUserWiseOptimizedTimesheetEntries(user, startEntryDate, endEntryDate);
		//1️.Validate inputs
	    if (user == null || user.isBlank()) {
	        throw new BusinessException("TS_002");
	    }
	    
	    if (startEntryDate == null || endEntryDate == null) {
	        throw new BusinessException("TS_011");
	    }
	    
	    if (startEntryDate.isAfter(endEntryDate)) {
	        throw new BusinessException("TS_012", startEntryDate, endEntryDate);
	    }
	    
	    //2️.Fetch data
	    List<TimesheetEntryProjection> userwiseTSResult = timesheetEntryRepository.findAllDateUserWiseOptimizedTimesheetEntries(user, startEntryDate, endEntryDate);

	    //3.Handle no data found
	    if (userwiseTSResult == null || userwiseTSResult.isEmpty()) {
	        throw new BusinessException("TS_014",user,startEntryDate,endEntryDate);
	    }

	    return userwiseTSResult;
    }

    @Transactional
    public List<TimesheetEntry> saveAllTimesheetEntry(TimesheetEntryDto dto) throws Exception {
    	
    	if (dto.getUserName() == null || dto.getUserName().isBlank()) {
            throw new BusinessException("TS_002");
        }
    	if (dto.getRows() == null || dto.getRows().isEmpty()) {
	        throw new BusinessException("TS_003");
	    }
    	//Determine date range
        LocalDate startDate = dto.getFromDate() != null
                ? dto.getFromDate()
                : dto.getEntryDate();

        LocalDate endDate = dto.getToDate() != null
                ? dto.getToDate()
                : startDate;

        if (startDate == null || endDate == null) {
            throw new BusinessException("TS_004");
        }

        if (startDate.isAfter(endDate)) {
            throw new BusinessException("TS_005");
        }

        /* ------------------------------------
        1️⃣ Collect all valid working dates
        ------------------------------------ */
	     List<LocalDate> workingDates = new ArrayList<>();
	     LocalDate date = startDate;
	
	     while (!date.isAfter(endDate)) {
	         DayOfWeek day = date.getDayOfWeek();
	         if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
	             workingDates.add(date);
	         }
	         date = date.plusDays(1);
	     }
	     
	     if (workingDates.isEmpty()) {
	         throw new BusinessException("TS_006");
	     }

	     /* ------------------------------------
       		2️⃣ Validate duplicates BEFORE saving
       	 ------------------------------------ */
	    for (LocalDate dayEntryDate : workingDates) {
	        if (timesheetEntryRepository.existsByEntryDateAndUserName(dayEntryDate, dto.getUserName())) {
	            throw new BusinessException("TS_001", dayEntryDate.toString());
	        }
	    }
        //prevent duplicate date entries
        /*if (timesheetEntryRepository.existsByEntryDateAndUserName(dto.getEntryDate(), dto.getUserName())) {
            throw new IllegalArgumentException("Timesheet entries already exist for this date.");
        }*/

        List<TimesheetEntry> savedList = new ArrayList<>();

        for (LocalDate dayEntryDate : workingDates) {
	        for (TimesheetRowDto row : dto.getRows()) {
	
	            TimesheetEntry entry = new TimesheetEntry();
	
	            //entry.setEntryDate(dto.getEntryDate());
	            entry.setEntryDate(dayEntryDate);
	            entry.setUserName(dto.getUserName());
	
	            entry.setTime(LocalTime.now());
	            
	            entry.setHours(row.getHours());
	            entry.setDetails(row.getDetails());
	            entry.setRaisedOn(LocalDateTime.now());
	
	            Category category = categoryRepository.findById(row.getCategoryId())
	            		.orElseThrow(() -> new BusinessException("CAT_001"));
	            
	            Platform platform = platformRepository.findById(row.getPlatformId())
	            		.orElseThrow(() -> new BusinessException("PLT_001"));
	            
	            Project project = projectRepository.findById(row.getProjectId())
	            		.orElseThrow(() -> new BusinessException("PRJ_001"));
	            
	            Activity activity = activityRepository.findById(row.getActivityId())
	           			.orElseThrow(() -> new BusinessException("ACT_001"));
	
	            entry.setCategory(category);
	            entry.setPlatform(platform);
	            entry.setProject(project);
	            entry.setActivity(activity);
	
	            savedList.add(timesheetEntryRepository.save(entry));
	        }
        }
        //Audit history snapshot BEFORE updating
        String historyVal = "UserName-> "+dto.getUserName()+" :savedList: "+savedList;
        saveTimesheetAudit(dto.getUserName(),historyVal,"Create");
        return savedList;
    }

    @Transactional
    public List<TimesheetEntry> updateAllTimesheetEntry(List<TimesheetEntryDto> entryRequests) {
        if (entryRequests == null || entryRequests.isEmpty()) {
            throw new BusinessException("TS_008");
        }

        List<TimesheetEntry> updatedList = new ArrayList<>();

        for (TimesheetEntryDto dto : entryRequests) {
            //1. Validate ID for parent entry
            if (dto.getId() == null) {
                throw new BusinessException("TS_009");
            }

            //2. Fetch existing parent entry
            TimesheetEntry existing = timesheetEntryRepository.findById(dto.getId())
            		.orElseThrow(() -> new BusinessException("TS_007", dto.getId()));

            //3. Update parent-level fields
            existing.setEntryDate(dto.getEntryDate());
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
            
            if (dto.getTime() != null && !dto.getTime().trim().isEmpty()) {
                LocalTime parsedTime = LocalTime.parse(dto.getTime().trim(), timeFormatter);
                existing.setTime(parsedTime);
            }
            existing.setUserName(dto.getUserName());
            existing.setRaisedOn(dto.getRaisedOn());

            //NOTE: Parent entry does not contain category/project/activity
            updatedList.add(timesheetEntryRepository.save(existing));

            // ---------------------------
            // 4. Update child rows (TimesheetRowDto)
            // ---------------------------
            if (dto.getRows() != null && !dto.getRows().isEmpty()) {

                for (TimesheetRowDto row : dto.getRows()) {

                    //Fetch associations
                	 Category category = categoryRepository.findById(row.getCategoryId())
     	            		.orElseThrow(() -> new BusinessException("CAT_001"));
     	            
     	            Platform platform = platformRepository.findById(row.getPlatformId())
     	            		.orElseThrow(() -> new BusinessException("PLT_001"));
     	            
     	            Project project = projectRepository.findById(row.getProjectId())
     	            		.orElseThrow(() -> new BusinessException("PRJ_001"));
     	            
     	            Activity activity = activityRepository.findById(row.getActivityId())
     	           			.orElseThrow(() -> new BusinessException("ACT_001"));

                    //Create a NEW TimesheetEntry for every row
                    TimesheetEntry rowEntry = new TimesheetEntry();

                    rowEntry.setEntryDate(dto.getEntryDate());
                    
                    rowEntry.setTime(LocalTime.now());
                    rowEntry.setUserName(dto.getUserName());
                    rowEntry.setRaisedOn(dto.getRaisedOn());

                    rowEntry.setHours(row.getHours());
                    rowEntry.setDetails(row.getDetails());

                    rowEntry.setCategory(category);
                    rowEntry.setPlatform(platform);
                    rowEntry.setProject(project);
                    rowEntry.setActivity(activity);

                    updatedList.add(timesheetEntryRepository.save(rowEntry));
                }
            }
        }

        return updatedList;
    }

    public void deleteTimesheetEntry(LocalDate entryDate, String user) {
        int count = timesheetEntryRepository.deleteByEntryDateAndUserName(entryDate, user);
        if (count == 0) {
        	throw new BusinessException("TS_010",entryDate.toString(),user.toString());
        }
    }
	
	 private void saveTimesheetAudit(String userName,String historyVal,String operationType) throws Exception {
		 TimesheetEntryAudit auditRecord = new TimesheetEntryAudit();
	        String currentUser 				= getLoggedInUsername();// <--- FROM JWT
	        auditRecord.setUserName(userName);
	        auditRecord.setFieldValues(historyVal);
	        auditRecord.setOperationType(operationType);
	        auditRecord.setChangedBy(currentUser);  // <--- store JWT USERNAME
	        auditRecord.setChangedOn(LocalDateTime.now());

	        timesheetEntryAuditHistoryRepository.save(auditRecord);
	    }
	 private String getLoggedInUsername() 
	 {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return "SYSTEM"; // fallback
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
	 }
	 
	 @Transactional
    public List<TimesheetEntry> saveLeaveTimesheetEntry(TimesheetEntryDto dto) throws Exception {
        
        if (dto.getUserName() == null || dto.getUserName().isBlank()) {
            throw new BusinessException("TS_002");
        }
    	if (dto.getRows() == null || dto.getRows().isEmpty()) {
	        throw new BusinessException("TS_003");
	    }
    	//Determine date range
        LocalDate startDate = dto.getFromDate() != null
                ? dto.getFromDate()
                : dto.getEntryDate();

        LocalDate endDate = dto.getToDate() != null
                ? dto.getToDate()
                : startDate;

        if (startDate == null || endDate == null) {
            throw new BusinessException("TS_004");
        }

        if (startDate.isAfter(endDate)) {
            throw new BusinessException("TS_005");
        }

        /* ------------------------------------
        1️⃣ Collect all valid working dates
        ------------------------------------ */
	     List<LocalDate> workingDates = new ArrayList<>();
	     LocalDate date = startDate;
	
	     while (!date.isAfter(endDate)) {
	         DayOfWeek day = date.getDayOfWeek();
	         if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
	             workingDates.add(date);
	         }
	         date = date.plusDays(1);
	     }
	     
	     if (workingDates.isEmpty()) {
	    	 throw new BusinessException("TS_006");
	     }

	     /* ------------------------------------
       		2️⃣ Validate duplicates BEFORE saving
       	 ------------------------------------ */
	    for (LocalDate dayEntryDate : workingDates) {
	        if (timesheetEntryRepository.existsByEntryDateAndUserName(dayEntryDate, dto.getUserName())) {
	            throw new BusinessException("TS_001", dayEntryDate.toString());
	        }
	    }
        //prevent duplicate date entries
        /*if (timesheetEntryRepository.existsByEntryDateAndUserName(dto.getEntryDate(), dto.getUserName())) {
            throw new IllegalArgumentException("Timesheet entries already exist for this date.");
        }*/

        List<TimesheetEntry> savedList = new ArrayList<>();
        for (LocalDate dayEntryDate : workingDates) {
	        for (TimesheetRowDto row : dto.getRows()) {
	
	            TimesheetEntry entry = new TimesheetEntry();
	
	            //entry.setEntryDate(dto.getEntryDate());
	            entry.setEntryDate(dayEntryDate);
	            entry.setUserName(dto.getUserName());
	
	            entry.setTime(LocalTime.now());
	            
	            //entry.setHours(row.getHours());
	            entry.setHours(BigDecimal.valueOf(9));
	            entry.setDetails(row.getDetails());
	            entry.setRaisedOn(LocalDateTime.now());
	
	            Category category = categoryRepository.findById(row.getCategoryId())
	            		.orElseThrow(() -> new BusinessException("CAT_001"));
	            
	            Platform platform = platformRepository.findById(row.getPlatformId())
	            		.orElseThrow(() -> new BusinessException("PLT_001"));
	            
	            Project project = projectRepository.findById(row.getProjectId())
	            		.orElseThrow(() -> new BusinessException("PRJ_001"));
	            
	            Activity activity = activityRepository.findById(row.getActivityId())
	           			.orElseThrow(() -> new BusinessException("ACT_001"));
	
	            entry.setCategory(category);
	            entry.setPlatform(platform);
	            entry.setProject(project);
	            entry.setActivity(activity);
	
	            savedList.add(timesheetEntryRepository.save(entry));
	        }
        }
        return savedList;
    }
}
