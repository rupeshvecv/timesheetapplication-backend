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
    private String categoryName;
    private String platformName;
    
    public ProjectDao() {}

    public ProjectDao(
            Long id,
            String projectName,
            String description,
            String status,
            String projectOwner,
            LocalDate projectStartDate,
            LocalDate projectEndDate,
            Long categoryId,
            Long platformId,
            String categoryName,
            String platformName
    ) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.status = status;
        this.projectOwner = projectOwner;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.categoryId = categoryId;
        this.platformId = platformId;
        this.categoryName = categoryName;
        this.platformName = platformName;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

}
