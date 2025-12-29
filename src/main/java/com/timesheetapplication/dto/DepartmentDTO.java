package com.timesheetapplication.dto;

public record DepartmentDTO(
        String departmentName,
        String departmentDescription    
        
) {
	public String departmentName() {
		return departmentName;
	}

	public String departmentDescription() {
		return departmentDescription;
	}
	
}
