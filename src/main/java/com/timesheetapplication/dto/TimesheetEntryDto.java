package com.timesheetapplication.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TimesheetEntryDto {

    private Long id;
    //For single-date entry (optional)
    private LocalDate entryDate;
    //For multiple-date entry
    private LocalDate fromDate;
    private LocalDate toDate;

    private String time;
    private String userName;
    private LocalDateTime raisedOn;
    
    private List<TimesheetRowDto> rows;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public LocalDate getToDate() {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public LocalDateTime getRaisedOn() {
		return raisedOn;
	}
	public void setRaisedOn(LocalDateTime raisedOn) {
		this.raisedOn = raisedOn;
	}
	public List<TimesheetRowDto> getRows() {
		return rows;
	}
	public void setRows(List<TimesheetRowDto> rows) {
		this.rows = rows;
	}
    
}

