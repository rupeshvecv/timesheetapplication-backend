package com.timesheetapplication.service;

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
				.orElseThrow(() -> new IllegalArgumentException("Timesheet not found: " + id));
	}

	/*public List<TimesheetEntry> getAll() {
		return timesheetEntryRepository.findAll();
	}*/
	
	public List<TimesheetEntryProjection> getAllOptimizedTimesheetEntries(LocalDate startEntryDate, LocalDate endEntryDate) {
        return timesheetEntryRepository.findAllOptimizedTimesheetEntries(startEntryDate,endEntryDate);
    }
	
	/*public List<TimesheetEntry> findForUserBetween(String user, LocalDate from, LocalDate to) {
		return timesheetEntryRepository.findByUserNameAndEntryDateBetween(user, from, to);
	}*/
	
	public List<TimesheetEntryProjection> getAllDateUserWiseOptimizedTimesheetEntries(String user, LocalDate startEntryDate, LocalDate endEntryDate) {
        return timesheetEntryRepository.findAllDateUserWiseOptimizedTimesheetEntries(user, startEntryDate, endEntryDate);
    }

    @Transactional
    public List<TimesheetEntry> saveAllTimesheetEntry(TimesheetEntryDto dto) throws Exception {
        //prevent duplicate date entries
        if (timesheetEntryRepository.existsByEntryDateAndUserName(dto.getEntryDate(), dto.getUserName())) {
            throw new IllegalArgumentException("Timesheet entries already exist for this date.");
        }

        List<TimesheetEntry> savedList = new ArrayList<>();

        for (TimesheetRowDto row : dto.getRows()) {

            TimesheetEntry entry = new TimesheetEntry();

            entry.setEntryDate(dto.getEntryDate());
            entry.setUserName(dto.getUserName());

            entry.setTime(LocalTime.now());
            
            entry.setHours(row.getHours());
            entry.setDetails(row.getDetails());
            entry.setRaisedOn(LocalDateTime.now());

            Category category = categoryRepository.findById(row.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            
            Platform platform = platformRepository.findById(row.getPlatformId())
                    .orElseThrow(() -> new IllegalArgumentException("Platform not found"));
            
            Project project = projectRepository.findById(row.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            
            Activity activity = activityRepository.findById(row.getActivityId())
                    .orElseThrow(() -> new IllegalArgumentException("Activity not found"));

            entry.setCategory(category);
            entry.setPlatform(platform);
            entry.setProject(project);
            entry.setActivity(activity);

            savedList.add(timesheetEntryRepository.save(entry));
        }
        //Audit history snapshot BEFORE updating
        String historyVal = "UserName-> "+dto.getUserName()+" :savedList: "+savedList;
        saveTimesheetAudit(dto.getUserName(),historyVal,"Create");
        return savedList;
    }

	/*@Transactional
	public TimesheetEntry createTimesheetEntry(TimesheetEntryDto dto) {
		Category category = null;
		Platform platform = null;
		Project project = null;
		Activity activity = null;
		
		if (dto.getCategoryId() != null) {
			category = categoryRepository.findById(dto.getCategoryId())
					.orElseThrow(() -> new IllegalArgumentException("Category not found: " + dto.getCategoryId()));
		}

		if (dto.getPlatformId() != null) {
			platform = platformRepository.findById(dto.getPlatformId())
					.orElseThrow(() -> new IllegalArgumentException("Platform not found: " + dto.getPlatformId()));
		}

		if (dto.getProjectId() != null) {
			project = projectRepository.findById(dto.getProjectId())
					.orElseThrow(() -> new IllegalArgumentException("Project not found: " + dto.getProjectId()));
		}

		if (dto.getActivityId() != null) {
			activity = activityRepository.findById(dto.getActivityId())
					.orElseThrow(() -> new IllegalArgumentException("Activity not found: " + dto.getActivityId()));
		}

		//Map DTO -> Entity with resolved associations
		TimesheetEntry entity = MapperUtil.toEntityWithAssociations(dto, category, platform, project, activity);

		 boolean exists = timesheetEntryRepository.existsByEntryDateAndUserName(entity.getEntryDate(), entity.getUserName());
        if (exists) {
            throw new IllegalArgumentException("Timesheet entry already exists for this date!");
        }
	        
		//Set raisedOn if missing
		if (entity.getRaisedOn() == null) {
			entity.setRaisedOn(LocalDateTime.now());
		}

		return timesheetEntryRepository.save(entity);
	}

	public TimesheetEntry save(TimesheetEntry timesheetEntry) {
		timesheetEntry.setRaisedOn(LocalDateTime.now());
		return timesheetEntryRepository.save(timesheetEntry);
	}
*/
    @Transactional
    public List<TimesheetEntry> updateAllTimesheetEntry(List<TimesheetEntryDto> entryRequests) {
        if (entryRequests == null || entryRequests.isEmpty()) {
            throw new IllegalArgumentException("Entry list cannot be empty");
        }

        List<TimesheetEntry> updatedList = new ArrayList<>();

        for (TimesheetEntryDto dto : entryRequests) {
            //1. Validate ID for parent entry
            if (dto.getId() == null) {
                throw new IllegalArgumentException("TimesheetEntry ID is required for update.");
            }

            //2. Fetch existing parent entry
            TimesheetEntry existing = timesheetEntryRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Entry not found: " + dto.getId()));

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
                            .orElseThrow(() -> new IllegalArgumentException("Category not found: " + row.getCategoryId()));

                    Platform platform = platformRepository.findById(row.getPlatformId())
                            .orElseThrow(() -> new IllegalArgumentException("Platform not found: " + row.getPlatformId()));

                    Project project = projectRepository.findById(row.getProjectId())
                            .orElseThrow(() -> new IllegalArgumentException("Project not found: " + row.getProjectId()));

                    Activity activity = activityRepository.findById(row.getActivityId())
                            .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + row.getActivityId()));

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

	
	/*@Transactional
	public TimesheetEntry updateTimesheetEntry(TimesheetEntryDto dto) {

		TimesheetEntry existing = timesheetEntryRepository.findById(dto.getId())
				.orElseThrow(() -> new IllegalArgumentException("Timesheet not found: " + dto.getId()));

		Category category = dto.getCategoryId() != null ? categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(() -> new IllegalArgumentException("Category not found")) : null;

		Platform platform = dto.getPlatformId() != null ? platformRepository.findById(dto.getPlatformId())
				.orElseThrow(() -> new IllegalArgumentException("Platform not found")) : null;

		Project project = dto.getProjectId() != null ? projectRepository.findById(dto.getProjectId())
				.orElseThrow(() -> new IllegalArgumentException("Project not found")) : null;

		Activity activity = dto.getActivityId() != null ? activityRepository.findById(dto.getActivityId())
				.orElseThrow(() -> new IllegalArgumentException("Activity not found")) : null;

		BigDecimal totalWithoutCurrent = timesheetEntryRepository.getTotalHoursForUserAndDate(
	            existing.getUserName(),
	            existing.getEntryDate()
	    ).subtract(existing.getHours());
		
		BigDecimal newTotal = totalWithoutCurrent.add(dto.getHours());

	    if (newTotal.compareTo(new BigDecimal("9")) < 0) {
	        throw new IllegalArgumentException("Total hours for this date must be at least 9.");
	    }

	    if (newTotal.compareTo(new BigDecimal("12")) >= 0) {
	        throw new IllegalArgumentException("Total hours cannot exceed 12.");
	    }
	    
		existing.setEntryDate(dto.getEntryDate());
		existing.setTime(dto.getTime());;
		existing.setHours(dto.getHours());
		existing.setUserName(dto.getUserName());
		existing.setDetails(dto.getDetails());
		existing.setRaisedOn(dto.getRaisedOn());
		existing.setCategory(category);
		existing.setPlatform(platform);
		existing.setProject(project);
		existing.setActivity(activity);

		return timesheetEntryRepository.save(existing);
	}*/

    public void deleteTimesheetEntry(LocalDate entryDate, String user) {
        int count = timesheetEntryRepository.deleteByEntryDateAndUserName(entryDate, user);
        if (count == 0) {
            throw new IllegalArgumentException("No timesheet entry found for date "
                    + entryDate + " and user " + user);
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
        //prevent duplicate date entries
        if (timesheetEntryRepository.existsByEntryDateAndUserName(dto.getEntryDate(), dto.getUserName())) {
            throw new IllegalArgumentException("Timesheet entries already exist for this date.");
        }

        List<TimesheetEntry> savedList = new ArrayList<>();

        for (TimesheetRowDto row : dto.getRows()) {

            TimesheetEntry entry = new TimesheetEntry();

            entry.setEntryDate(dto.getEntryDate());
            entry.setUserName(dto.getUserName());

            entry.setTime(LocalTime.now());
            
            entry.setHours(row.getHours());
            entry.setDetails(row.getDetails());
            entry.setRaisedOn(LocalDateTime.now());

            Category category = categoryRepository.findById(row.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            
            Platform platform = platformRepository.findById(row.getPlatformId())
                    .orElseThrow(() -> new IllegalArgumentException("Platform not found"));
            
            Project project = projectRepository.findById(row.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            
            Activity activity = activityRepository.findById(row.getActivityId())
                    .orElseThrow(() -> new IllegalArgumentException("Activity not found"));

            entry.setCategory(category);
            entry.setPlatform(platform);
            entry.setProject(project);
            entry.setActivity(activity);

            savedList.add(timesheetEntryRepository.save(entry));
        }
        return savedList;
    }
}
