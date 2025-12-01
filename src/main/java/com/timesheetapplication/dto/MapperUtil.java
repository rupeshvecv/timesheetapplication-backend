package com.timesheetapplication.dto;

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
        ProjectDao d = new ProjectDao();
        d.setId(p.getId());
        d.setProjectName(p.getProjectName());
        d.setPlatformId(p.getPlatform() != null ? p.getPlatform().getId() : null);
        d.setCategoryId(p.getCategory() != null ? p.getCategory().getId() : null);
        return d;
    }

    // --------------------- ACTIVITY → DTO -------------------------
    public static ActivityDao toActivityDto(Activity a) {
        ActivityDao d = new ActivityDao();
        d.setId(a.getId());
        d.setActivityName(a.getActivityName());
        d.setCategoryId(a.getCategory() != null ? a.getCategory().getId() : null);
        d.setProjectId(a.getProject() != null ? a.getProject().getId() : null);
        return d;
    }

    // --------------------- DTO → ENTITY (no associations) -------------------------
    /**
     * Converts DTO -> TimesheetEntry without attempting to create relationship stubs.
     * After calling this, resolve associations in service layer and set them on the returned entity.
     */
    public static TimesheetEntry toEntity(TimesheetEntryDto dto) {
        TimesheetEntry t = new TimesheetEntry();

        t.setId(dto.getId());
        t.setTime(dto.getTime());
        t.setEntryDate(dto.getEntryDate());
        t.setHours(dto.getHours());
        t.setUserName(dto.getUserName());
        t.setDetails(dto.getDetails());
        t.setRaisedOn(dto.getRaisedOn());
        
        System.out.println("MapperUtil.toEntity(dto.getHours()) "+dto.getHours());
        System.out.println("MapperUtil.toEntity(dto.getUserName()) "+dto.getUserName());
        System.out.println("MapperUtil.toEntity(dto.getTime()) "+dto.getTime());

        //NOTE: do NOT instantiate new Category(id) / Platform(id) here.
        //Leave associations null — resolve them in the service layer before saving.
        t.setCategory(null);
        t.setPlatform(null);
        t.setProject(null);
        t.setActivity(null);

        return t;
    }

    // --------------------- DTO -> ENTITY (with resolved associations) -------------------------
    /**
     * Use this when you already have resolved Category/Platform/Project/Activity objects
     * (fetched from repositories). This is the recommended method before saving to DB.
     */
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
        TimesheetEntryDto d = new TimesheetEntryDto();

        System.out.println("MapperUtil.toDto(e.getHours()) "+e.getHours());
        System.out.println("MapperUtil.toDto(e.getUserName()) "+e.getUserName());
        System.out.println("MapperUtil.toDto(e.getTime()) "+e.getTime());
        d.setId(e.getId());
        d.setEntryDate(e.getEntryDate());
        d.setTime(e.getTime());
        d.setHours(e.getHours());
        d.setUserName(e.getUserName());
        d.setDetails(e.getDetails());
        d.setRaisedOn(e.getRaisedOn());

        d.setCategoryId(e.getCategory() != null ? e.getCategory().getId() : null);
        d.setPlatformId(e.getPlatform() != null ? e.getPlatform().getId() : null);
        d.setProjectId(e.getProject() != null ? e.getProject().getId() : null);
        d.setActivityId(e.getActivity() != null ? e.getActivity().getId() : null);

        return d;
    }
}
