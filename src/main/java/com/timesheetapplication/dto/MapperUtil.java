package com.timesheetapplication.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.timesheetapplication.dao.ActivityDao;
import com.timesheetapplication.dao.ProjectDao;
import com.timesheetapplication.model.Activity;
import com.timesheetapplication.model.Category;
import com.timesheetapplication.model.Platform;
import com.timesheetapplication.model.Project;
import com.timesheetapplication.model.TimesheetEntry;

public class MapperUtil {

    // --------------------- PROJECT → DTO -------------------------
    public static ProjectDao toProjectDto(Project p) {
        if (p == null) return null;
        
        ProjectDao d = new ProjectDao();
        d.setId(p.getId());
        d.setProjectName(p.getProjectName());
        d.setPlatformId(p.getPlatform() != null ? p.getPlatform().getId() : null);
        d.setCategoryId(p.getCategory() != null ? p.getCategory().getId() : null);
        return d;
    }

    // --------------------- ACTIVITY → DTO -------------------------
    public static ActivityDao toActivityDto(Activity a) {
        if (a == null) return null;

        ActivityDao d = new ActivityDao();
        d.setId(a.getId());
        d.setActivityName(a.getActivityName());
        d.setCategoryId(a.getCategory() != null ? a.getCategory().getId() : null);
        d.setProjectId(a.getProject() != null ? a.getProject().getId() : null);
        return d;
    }

    // --------------------- DTO → ENTITY (no associations) -------------------------
    public static TimesheetEntry toEntity(TimesheetEntryDto dto) {
        if (dto == null) return null;

        TimesheetEntry t = new TimesheetEntry();
        TimesheetRowDto rd = new TimesheetRowDto();
        
        t.setId(dto.getId());
        t.setTime(LocalTime.now());
        t.setEntryDate(dto.getEntryDate());
        t.setHours(rd.getHours());
        t.setUserName(dto.getUserName());
        t.setRaisedOn(dto.getRaisedOn());
        t.setDetails(rd.getDetails());
        
        //Associations are resolved at service layer
        t.setCategory(null);
        t.setPlatform(null);
        t.setProject(null);
        t.setActivity(null);

        return t;
    }

    // --------------------- DTO → ENTITY (with associations) -------------------------
    public static TimesheetEntry toEntityWithAssociations(
            TimesheetEntryDto dto,
            Category category,
            Platform platform,
            Project project,
            Activity activity) {

        TimesheetEntry t = toEntity(dto);

        t.setCategory(category);
        t.setPlatform(platform);
        t.setProject(project);
        t.setActivity(activity);

        return t;
    }

    // --------------------- ENTITY → DTO -------------------------
    public static TimesheetEntryDto toDto(TimesheetEntry e) {
        if (e == null) return null;

        TimesheetEntryDto d = new TimesheetEntryDto();
        TimesheetRowDto rd = new TimesheetRowDto();
        
        d.setId(e.getId());
        d.setEntryDate(e.getEntryDate());
        if (e.getTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            d.setTime(e.getTime().format(formatter));
        }
        rd.setHours(e.getHours());
        d.setUserName(e.getUserName());
        d.setRaisedOn(e.getRaisedOn());
        rd.setDetails(e.getDetails());

        rd.setCategoryId(e.getCategory() != null ? e.getCategory().getId() : null);
        rd.setPlatformId(e.getPlatform() != null ? e.getPlatform().getId() : null);
        rd.setProjectId(e.getProject() != null ? e.getProject().getId() : null);
        rd.setActivityId(e.getActivity() != null ? e.getActivity().getId() : null);

        return d;
    }
}
