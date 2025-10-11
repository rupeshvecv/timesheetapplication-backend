package com.edcapplication.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "project_table", schema = "edcapplication")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "project_code")
    private String projectCode;

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

    public Project() {}

    public Project(Long id, String projectCode, String description, String status, 
    		String projectOwner, LocalDate projectStartDate, LocalDate projectEndDate) {
        this.id = id;
        this.projectCode = projectCode;
        this.description = description;
        this.status = status;
        this.projectOwner = projectOwner;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
    }

    //getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProjectCode() { return projectCode; }
    public void setProjectCode(String projectCode) { this.projectCode = projectCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProjectOwner() { return projectOwner; }
    public void setProjectOwner(String projectOwner) { this.projectOwner = projectOwner; }

    public LocalDate getProjectStartDate() { return projectStartDate; }
    public void setProjectStartDate(LocalDate projectStartDate) { this.projectStartDate = projectStartDate; }

    public LocalDate getProjectEndDate() { return projectEndDate; }
    public void setProjectEndDate(LocalDate projectEndDate) { this.projectEndDate = projectEndDate; }

	@Override
	public String toString() {
		return "Project [id=" + id + ", projectCode=" + projectCode + ", description=" + description + ", status="
				+ status + ", projectOwner=" + projectOwner + ", projectStartDate=" + projectStartDate
				+ ", projectEndDate=" + projectEndDate + "]";
	}

}
