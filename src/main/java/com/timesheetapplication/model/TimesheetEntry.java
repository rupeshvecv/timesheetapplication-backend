package com.timesheetapplication.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "timesheet_entry",schema = "timesheetapplication",
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"entry_date", "user_name"})
	    }
	)
public class TimesheetEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "entry_date", nullable = false)
	private LocalDate entryDate;
	
    @Column(name = "time")
    private String time;

	@Column(nullable = false)
	private BigDecimal hours;

	@Column(name = "user_name", nullable = false)
	private String userName;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "platform_id")
	private Platform platform;

	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;

	@ManyToOne
	@JoinColumn(name = "activity_id")
	private Activity activity;

	@Column(name = "details")
	private String details;

	@Column(name = "raised_on")
	private LocalDateTime raisedOn;

	public TimesheetEntry() {
		super();
	}

	public TimesheetEntry(Long id, LocalDate entryDate, String time, BigDecimal hours, String userName, Category category,
			Platform platform, Project project, Activity activity, String details, LocalDateTime raisedOn) {
		super();
		this.id = id;
		this.entryDate = entryDate;
		this.time = time;
		this.hours = hours;
		this.userName = userName;
		this.category = category;
		this.platform = platform;
		this.project = project;
		this.activity = activity;
		this.details = details;
		this.raisedOn = raisedOn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public LocalDate getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}

	public BigDecimal getHours() {
		return hours;
	}

	public void setHours(BigDecimal hours) {
		this.hours = hours;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDateTime getRaisedOn() {
		return raisedOn;
	}

	public void setRaisedOn(LocalDateTime raisedOn) {
		this.raisedOn = raisedOn;
	}

	@Override
	public String toString() {
		return "TimesheetEntry [id=" + id + ", entryDate=" + entryDate + ", hours=" + hours + ", userName=" + userName
				+ ", category=" + category + ", platform=" + platform + ", project=" + project + ", activity="
				+ activity + ", details=" + details + ", raisedOn=" + raisedOn + "]";
	}

}
