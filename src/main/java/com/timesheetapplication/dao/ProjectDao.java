package com.timesheetapplication.dao;

import java.time.LocalDate;

public class ProjectDao {
	private Long id;
	private String projectName;
	private String description;
	private String status;
	private String projectOwner;
	private LocalDate projectStartDate;
	private LocalDate projectEndDate;
    private Long categoryId;
    private Long platformId;

    public ProjectDao() {}

	public ProjectDao(Long id,String projectName, String description, String status, String projectOwner, LocalDate projectStartDate,
			LocalDate projectEndDate, Long categoryId, Long platformId) {
		super();
		this.id = id;
		this.projectName = projectName;
		this.description = description;
		this.status = status;
		this.projectOwner = projectOwner;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.categoryId = categoryId;
		this.platformId = platformId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProjectOwner() {
		return projectOwner;
	}

	public void setProjectOwner(String projectOwner) {
		this.projectOwner = projectOwner;
	}

	public LocalDate getProjectStartDate() {
		return projectStartDate;
	}

	public void setProjectStartDate(LocalDate projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public LocalDate getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(LocalDate projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

}
