package com.timesheetapplication.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "project", schema = "timesheetapplication")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", unique = true)
    private String projectName;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "project_owner")
    private String projectOwner;
    
    @Column(name = "project_start_date")
    private LocalDate projectStartDate;

    @Column(name = "project_end_date")
    private LocalDate projectEndDate;

    @ManyToOne
    @JoinColumn(name = "platform_id")
    private Platform platform;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

	public Project() {
		super();
	}

	public Project(Long id, String projectName, String description, String status, String projectOwner,
			LocalDate projectStartDate, LocalDate projectEndDate, Platform platform, Category category) {
		super();
		this.id = id;
		this.projectName = projectName;
		this.description = description;
		this.status = status;
		this.projectOwner = projectOwner;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.platform = platform;
		this.category = category;
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

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", projectName=" + projectName + ", description=" + description + ", status="
				+ status + ", projectOwner=" + projectOwner + ", projectStartDate=" + projectStartDate
				+ ", projectEndDate=" + projectEndDate + ", platform=" + platform + ", category=" + category + "]";
	}
    
}

