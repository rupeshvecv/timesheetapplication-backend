package com.timesheetapplication.dao;

public class ActivityDao {
	private Long id;
    private String activityName;
    private Long categoryId;
    private String categoryName;
    private Long projectId;
    private String projectName;
    
    public ActivityDao() {}

    public ActivityDao(
            Long id,
            String activityName,
            Long categoryId,
            String categoryName,
            Long projectId,
            String projectName) {

        this.id = id;
        this.activityName = activityName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Long getCategoryId() {
        return categoryId;
    }
   
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Long getProjectId() {
        return projectId;
    }
   
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
    
}
