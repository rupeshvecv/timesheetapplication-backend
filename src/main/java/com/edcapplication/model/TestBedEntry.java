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

    @Column(name = "test_description")
    private String testDescription;

    @Column(name = "test_description_hours")
    private double testDescriptionHours;
    
    @Column(name = "workon_engine_remarks")
    private String workonEngineRemarks;

    @Column(name = "workon_engine_hours")
    private double workonEngineHours;

    @Column(name = "set_up_remarks")
    private String setUpRemarks;

    @Column(name = "set_up_hours")
    private double setUpHours;

    @Column(name = "break_down_remarks")
    private String breakDownRemarks;

    @Column(name = "break_down_hours")
    private double breakDownHours;

    @Column(name = "no_man_power_remarks")
    private String noManPowerRemarks;

    @Column(name = "no_man_power_hours")
    private double noManPowerHours;
    
    @Column(name = "power_cut_remarks")
    private String powerCutRemarks;

    @Column(name = "power_cut_hours")
    private double powerCutHours;

    @Column(name = "any_other_remarks")
    private String anyOtherRemarks;

    @Column(name = "any_other_hours")
    private double anyOtherHours;

    @Column(name = "coummulative_description")
    private String coummulativeDescription;
    
    @Column(name = "total_sum")
    private double totalSum;

    // --- Constructors ---
    public TestBedEntry() {}

	public TestBedEntry(TestBedEntryEmbeddedId id, String time, String raisedBy, String testBedUser, Project project,
			double plannedHours, double uptimeHours, double utilizationHours, double validationHours,
			String testDescription, double testDescriptionHours, String workonEngineRemarks, double workonEngineHours,
			String setUpRemarks, double setUpHours, String breakDownRemarks, double breakDownHours,
			String noManPowerRemarks, double noManPowerHours, String powerCutRemarks, double powerCutHours,
			String anyOtherRemarks, double anyOtherHours, String coummulativeHours, double totalSum) {
		super();
		this.id = id;
		this.time = time;
		this.raisedBy = raisedBy;
		this.testBedUser = testBedUser;
		this.project = project;
		this.plannedHours = plannedHours;
		this.uptimeHours = uptimeHours;
		this.utilizationHours = utilizationHours;
		this.validationHours = validationHours;
		this.testDescription = testDescription;
		this.testDescriptionHours = testDescriptionHours;
		this.workonEngineRemarks = workonEngineRemarks;
		this.workonEngineHours = workonEngineHours;
		this.setUpRemarks = setUpRemarks;
		this.setUpHours = setUpHours;
		this.breakDownRemarks = breakDownRemarks;
		this.breakDownHours = breakDownHours;
		this.noManPowerRemarks = noManPowerRemarks;
		this.noManPowerHours = noManPowerHours;
		this.powerCutRemarks = powerCutRemarks;
		this.powerCutHours = powerCutHours;
		this.anyOtherRemarks = anyOtherRemarks;
		this.anyOtherHours = anyOtherHours;
		this.coummulativeDescription = coummulativeDescription;
		this.totalSum = totalSum;
	}

	public TestBedEntryEmbeddedId getId() {
		return id;
	}

	public void setId(TestBedEntryEmbeddedId id) {
		this.id = id;
	}

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

	public String getTestDescription() {
		return testDescription;
	}

	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

	public double getTestDescriptionHours() {
		return testDescriptionHours;
	}

	public void setTestDescriptionHours(double testDescriptionHours) {
		this.testDescriptionHours = testDescriptionHours;
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

	public String getPowerCutRemarks() {
		return powerCutRemarks;
	}

	public void setPowerCutRemarks(String powerCutRemarks) {
		this.powerCutRemarks = powerCutRemarks;
	}

	public double getPowerCutHours() {
		return powerCutHours;
	}

	public void setPowerCutHours(double powerCutHours) {
		this.powerCutHours = powerCutHours;
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

	public String getCoummulativeDescription() {
		return coummulativeDescription;
	}

	public void setCoummulativeDescription(String coummulativeDescription) {
		this.coummulativeDescription = coummulativeDescription;
	}

	public double getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(double totalSum) {
		this.totalSum = totalSum;
	}

	@Override
	public String toString() {
		return "TestBedEntry [id=" + id + ", time=" + time + ", raisedBy=" + raisedBy + ", testBedUser=" + testBedUser
				+ ", project=" + project + ", plannedHours=" + plannedHours + ", uptimeHours=" + uptimeHours
				+ ", utilizationHours=" + utilizationHours + ", validationHours=" + validationHours
				+ ", testDescription=" + testDescription + ", testDescriptionHours=" + testDescriptionHours
				+ ", workonEngineRemarks=" + workonEngineRemarks + ", workonEngineHours=" + workonEngineHours
				+ ", setUpRemarks=" + setUpRemarks + ", setUpHours=" + setUpHours + ", breakDownRemarks="
				+ breakDownRemarks + ", breakDownHours=" + breakDownHours + ", noManPowerRemarks=" + noManPowerRemarks
				+ ", noManPowerHours=" + noManPowerHours + ", powerCutRemarks=" + powerCutRemarks + ", powerCutHours="
				+ powerCutHours + ", anyOtherRemarks=" + anyOtherRemarks + ", anyOtherHours=" + anyOtherHours
				+ ", coummulativeDescription=" + coummulativeDescription + ", totalSum=" + totalSum + "]";
	}

}
