package com.timesheetapplication.dao;

public class ActivityDao {
	private Long id;
    private String activityName;
    private Long categoryId;
    private Long projectId;

    public ActivityDao() {}

    public ActivityDao(Long id, String activityName, Long categoryId, Long projectId) {
        this.id = id;
        this.activityName = activityName;
        this.categoryId = categoryId;
        this.projectId = projectId;
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
}
