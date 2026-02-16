package com.timesheetapplication.dto;

public record WeeklyTimesheetDTO(
        String employeeName,
        String userName,
        String departmentName,
        String managerName,
        String status      // Filled / Not Filled / Submitted Date
) {}
