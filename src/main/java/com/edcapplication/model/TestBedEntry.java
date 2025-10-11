package com.edcapplication.model;

import jakarta.persistence.*;

@Entity
@Table(name = "testbedentry_table")
public class TestBedEntry {

    @EmbeddedId
    private TestBedEntryEmbeddedId id;

    @Column(name = "time")
    private String time;

    @Column(name = "raised_by")
    private String raisedBy;

    @Column(name = "test_bed_user")
    private String testBedUser;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "planned_hours")
    private double plannedHours;

    @Column(name = "uptime_hours")
    private double uptimeHours;

    @Column(name = "utilization_hours")
    private double utilizationHours;

    @Column(name = "validation_hours")
    private double validationHours;

    @Column(name = "running_in_remarks")
    private String runningInRemarks;

    @Column(name = "running_in_hours")
    private double runningInHours;

    @Column(name = "set_up_remarks")
    private String setUpRemarks;

    @Column(name = "set_up_hours")
    private double setUpHours;

    @Column(name = "workon_engine_remarks")
    private String workonEngineRemarks;

    @Column(name = "workon_engine_hours")
    private double workonEngineHours;

    @Column(name = "break_down_remarks")
    private String breakDownRemarks;

    @Column(name = "break_down_hours")
    private double breakDownHours;

    @Column(name = "no_man_power_remarks")
    private String noManPowerRemarks;

    @Column(name = "no_man_power__hours")
    private double noManPowerHours;

    @Column(name = "any_other_remarks")
    private String anyOtherRemarks;

    @Column(name = "any_other_hours")
    private double anyOtherHours;

    @Column(name = "total_sum")
    private double totalSum;

    @Column(name = "engine_changeover_time")
    private String engineChangeoverTime;

    // --- Constructors ---
    public TestBedEntry() {}

	/*
	 * public TestBedEntry(TestBedEntryEmbeddedId id, TestBed testBed, String time,
	 * String raisedBy, Project project, String testBedUser, double plannedHours,
	 * double uptimeHours, double utilizationHours, double validationHours, String
	 * runningInRemarks, double runningInHours, String setUpRemarks, double
	 * setUpHours, String workonEngineRemarks, double workonEngineHours, String
	 * breakDownRemarks, double breakDownHours, String noManPowerRemarks, double
	 * noManPowerHours, String anyOtherRemarks, double anyOtherHours, double
	 * totalSum, String engineChangeoverTime) {
	 */
    	
    	
    	 public TestBedEntry(TestBedEntryEmbeddedId id, String time, String raisedBy,
                 Project project, String testBedUser, double plannedHours, double uptimeHours,
                 double utilizationHours, double validationHours, String runningInRemarks,
                 double runningInHours, String setUpRemarks, double setUpHours,
                 String workonEngineRemarks, double workonEngineHours, String breakDownRemarks,
                 double breakDownHours, String noManPowerRemarks, double noManPowerHours,
                 String anyOtherRemarks, double anyOtherHours, double totalSum,
                 String engineChangeoverTime) {
        this.id = id;
        //this.testBed = testBed;
        this.time = time;
        this.raisedBy = raisedBy;
        this.project = project;
        this.testBedUser = testBedUser;
        this.plannedHours = plannedHours;
        this.uptimeHours = uptimeHours;
        this.utilizationHours = utilizationHours;
        this.validationHours = validationHours;
        this.runningInRemarks = runningInRemarks;
        this.runningInHours = runningInHours;
        this.setUpRemarks = setUpRemarks;
        this.setUpHours = setUpHours;
        this.workonEngineRemarks = workonEngineRemarks;
        this.workonEngineHours = workonEngineHours;
        this.breakDownRemarks = breakDownRemarks;
        this.breakDownHours = breakDownHours;
        this.noManPowerRemarks = noManPowerRemarks;
        this.noManPowerHours = noManPowerHours;
        this.anyOtherRemarks = anyOtherRemarks;
        this.anyOtherHours = anyOtherHours;
        this.totalSum = totalSum;
        this.engineChangeoverTime = engineChangeoverTime;
    }

    public TestBedEntryEmbeddedId getId() {
        return id;
    }

    public void setId(TestBedEntryEmbeddedId id) {
        this.id = id;
    }

    /*public TestBed getTestBed() {
        return testBed;
    }

    public void setTestBed(TestBed testBed) {
        this.testBed = testBed;
    }*/

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public String getTestBedUser() {
        return testBedUser;
    }

    public void setTestBedUser(String testBedUser) {
        this.testBedUser = testBedUser;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public double getPlannedHours() {
        return plannedHours;
    }

    public void setPlannedHours(double plannedHours) {
        this.plannedHours = plannedHours;
    }

    public double getUptimeHours() {
        return uptimeHours;
    }

    public void setUptimeHours(double uptimeHours) {
        this.uptimeHours = uptimeHours;
    }

    public double getUtilizationHours() {
        return utilizationHours;
    }

    public void setUtilizationHours(double utilizationHours) {
        this.utilizationHours = utilizationHours;
    }

    public double getValidationHours() {
        return validationHours;
    }

    public void setValidationHours(double validationHours) {
        this.validationHours = validationHours;
    }

    public String getRunningInRemarks() {
        return runningInRemarks;
    }

    public void setRunningInRemarks(String runningInRemarks) {
        this.runningInRemarks = runningInRemarks;
    }

    public double getRunningInHours() {
        return runningInHours;
    }

    public void setRunningInHours(double runningInHours) {
        this.runningInHours = runningInHours;
    }

    public String getSetUpRemarks() {
        return setUpRemarks;
    }

    public void setSetUpRemarks(String setUpRemarks) {
        this.setUpRemarks = setUpRemarks;
    }

    public double getSetUpHours() {
        return setUpHours;
    }

    public void setSetUpHours(double setUpHours) {
        this.setUpHours = setUpHours;
    }

    public String getWorkonEngineRemarks() {
        return workonEngineRemarks;
    }

    public void setWorkonEngineRemarks(String workonEngineRemarks) {
        this.workonEngineRemarks = workonEngineRemarks;
    }

    public double getWorkonEngineHours() {
        return workonEngineHours;
    }

    public void setWorkonEngineHours(double workonEngineHours) {
        this.workonEngineHours = workonEngineHours;
    }

    public String getBreakDownRemarks() {
        return breakDownRemarks;
    }

    public void setBreakDownRemarks(String breakDownRemarks) {
        this.breakDownRemarks = breakDownRemarks;
    }

    public double getBreakDownHours() {
        return breakDownHours;
    }

    public void setBreakDownHours(double breakDownHours) {
        this.breakDownHours = breakDownHours;
    }

    public String getNoManPowerRemarks() {
        return noManPowerRemarks;
    }

    public void setNoManPowerRemarks(String noManPowerRemarks) {
        this.noManPowerRemarks = noManPowerRemarks;
    }

    public double getNoManPowerHours() {
        return noManPowerHours;
    }

    public void setNoManPowerHours(double noManPowerHours) {
        this.noManPowerHours = noManPowerHours;
    }

    public String getAnyOtherRemarks() {
        return anyOtherRemarks;
    }

    public void setAnyOtherRemarks(String anyOtherRemarks) {
        this.anyOtherRemarks = anyOtherRemarks;
    }

    public double getAnyOtherHours() {
        return anyOtherHours;
    }

    public void setAnyOtherHours(double anyOtherHours) {
        this.anyOtherHours = anyOtherHours;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
    }

    public String getEngineChangeoverTime() {
        return engineChangeoverTime;
    }

    public void setEngineChangeoverTime(String engineChangeoverTime) {
        this.engineChangeoverTime = engineChangeoverTime;
    }

	@Override
	public String toString() {
		return "TestBedEntry [id=" + id + ", time=" + time + ", raisedBy=" + raisedBy
				+ ", testBedUser=" + testBedUser + ", project=" + project + ", plannedHours=" + plannedHours
				+ ", uptimeHours=" + uptimeHours + ", utilizationHours=" + utilizationHours + ", validationHours="
				+ validationHours + ", runningInRemarks=" + runningInRemarks + ", runningInHours=" + runningInHours
				+ ", setUpRemarks=" + setUpRemarks + ", setUpHours=" + setUpHours + ", workonEngineRemarks="
				+ workonEngineRemarks + ", workonEngineHours=" + workonEngineHours + ", breakDownRemarks="
				+ breakDownRemarks + ", breakDownHours=" + breakDownHours + ", noManPowerRemarks=" + noManPowerRemarks
				+ ", noManPowerHours=" + noManPowerHours + ", anyOtherRemarks=" + anyOtherRemarks + ", anyOtherHours="
				+ anyOtherHours + ", totalSum=" + totalSum + ", engineChangeoverTime=" + engineChangeoverTime + "]";
	}
    
}
