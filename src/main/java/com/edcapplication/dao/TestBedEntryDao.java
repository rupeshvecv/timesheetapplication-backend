package com.edcapplication.dao;

import java.time.LocalDate;

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

    private String runningInRemarks;
    private double runningInHours;

    private String setUpRemarks;
    private double setUpHours;

    private String workonEngineRemarks;
    private double workonEngineHours;

    private String breakDownRemarks;
    private double breakDownHours;

    private String noManPowerRemarks;
    private double noManPowerHours;

    private String anyOtherRemarks;
    private double anyOtherHours;

    private double totalSum;
    private String engineChangeoverTime;

    // Getters and Setters
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
}
