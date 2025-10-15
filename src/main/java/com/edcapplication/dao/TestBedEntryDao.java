package com.edcapplication.dao;

import java.time.LocalDate;

import jakarta.persistence.Column;

public class TestBedEntryDao {

    private Long testbedId;
    private LocalDate raisedOn;
    private String shift;

    private String time;
    private String raisedBy;
    private String testBedUser;
    private Long projectId;

    private double plannedHours;
    private double uptimeHours;
    private double utilizationHours;
    private double validationHours;

    private String testDescription;
    private double testDescriptionHours;

    private String workonEngineRemarks;
    private double workonEngineHours;
    
    private String setUpRemarks;
    private double setUpHours;

    private String breakDownRemarks;
    private double breakDownHours;

    private String noManPowerRemarks;
    private double noManPowerHours;

    private String powerCutRemarks;
    private double powerCutHours;
    
    private String anyOtherRemarks;
    private double anyOtherHours;

    private double totalSum;
    private String coummulativeDescription;
	public Long getTestbedId() {
		return testbedId;
	}
	public void setTestbedId(Long testbedId) {
		this.testbedId = testbedId;
	}
	public LocalDate getRaisedOn() {
		return raisedOn;
	}
	public void setRaisedOn(LocalDate raisedOn) {
		this.raisedOn = raisedOn;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
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
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
	public double getTotalSum() {
		return totalSum;
	}
	public void setTotalSum(double totalSum) {
		this.totalSum = totalSum;
	}
	public String getCoummulativeDescription() {
		return coummulativeDescription;
	}
	public void setCoummulativeHours(String coummulativeDescription) {
		this.coummulativeDescription = coummulativeDescription;
	}

    
}
