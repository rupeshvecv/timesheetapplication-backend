package com.timesheetapplication.dto;

public class NotFilledDTO {

    private String employee;
    private String manager;
    private int workingDays;
    private int filledDays;

    public NotFilledDTO(String employee, String manager, int workingDays, int filledDays) {
        this.employee = employee;
        this.manager = manager;
        this.workingDays = workingDays;
        this.filledDays = filledDays;
    }

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public int getWorkingDays() {
		return workingDays;
	}

	public void setWorkingDays(int workingDays) {
		this.workingDays = workingDays;
	}

	public int getFilledDays() {
		return filledDays;
	}

	public void setFilledDays(int filledDays) {
		this.filledDays = filledDays;
	}

}

