package com.edcapplication.dao;

import java.time.LocalDate;

public class BDRREntryDao {

    private String bdrrNumber;
    private String status;
    private LocalDate raisedOn;
    private String raisedBy;
    private String shift;
    private Long testbedId;
    private Long equipmentId;
    private Long subEquipmentId;
    private Long problemId;
    private String testAffected;
    private String alternateArrangement;
    private String suggestion;
    private String attender;
    private String solutionRootCause;
    private String solutionActionTaken;
    private String solutionBy;
    private LocalDate solutionGivenOn;
    private LocalDate bdrrOfDate;
    private String areaAttender;
    private LocalDate targetDate;
    private LocalDate closingDate;
    private String partUsed;
    private String partNumber;
    private String partDescriptions;
    private String quantity;
    private String breakDownDescription;
    private String initialAnalysis;
    private String workDoneDescription;

    public BDRREntryDao() {
    }

    public BDRREntryDao(String bdrrNumber, String status, LocalDate raisedOn, String raisedBy, String shift, Long testbedId,
    		Long equipmentId, Long subEquipmentId, Long problemId, String testAffected, String alternateArrangement,
                        String suggestion, String attender, String solutionRootCause, String solutionActionTaken, String solutionBy,
                        LocalDate solutionGivenOn, LocalDate bdrrOfDate, String areaAttender, LocalDate targetDate, LocalDate closingDate,
                        String partUsed, String partNumber, String partDescriptions, String quantity, String breakDownDescription,
                        String initialAnalysis, String workDoneDescription) {
        this.bdrrNumber = bdrrNumber;
        this.status = status;
        this.raisedOn = raisedOn;
        this.raisedBy = raisedBy;
        this.shift = shift;
        this.testbedId = testbedId;
        this.equipmentId = equipmentId;
        this.subEquipmentId = subEquipmentId;
        this.problemId = problemId;
        this.testAffected = testAffected;
        this.alternateArrangement = alternateArrangement;
        this.suggestion = suggestion;
        this.attender = attender;
        this.solutionRootCause = solutionRootCause;
        this.solutionActionTaken = solutionActionTaken;
        this.solutionBy = solutionBy;
        this.solutionGivenOn = solutionGivenOn;
        this.bdrrOfDate = bdrrOfDate;
        this.areaAttender = areaAttender;
        this.targetDate = targetDate;
        this.closingDate = closingDate;
        this.partUsed = partUsed;
        this.partNumber = partNumber;
        this.partDescriptions = partDescriptions;
        this.quantity = quantity;
        this.breakDownDescription = breakDownDescription;
        this.initialAnalysis = initialAnalysis;
        this.workDoneDescription = workDoneDescription;
    }

    // Getters and Setters (you can use Lombok @Data if preferred)

    public String getBdrrNumber() {
        return bdrrNumber;
    }

    public void setBdrrNumber(String bdrrNumber) {
        this.bdrrNumber = bdrrNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getRaisedOn() {
        return raisedOn;
    }

    public void setRaisedOn(LocalDate raisedOn) {
        this.raisedOn = raisedOn;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public Long getTestbedId() {
        return testbedId;
    }

    public void setTestbedId(Long testbedId) {
        this.testbedId = testbedId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getSubEquipmentId() {
        return subEquipmentId;
    }

    public void setSubEquipmentId(Long subEquipmentId) {
        this.subEquipmentId = subEquipmentId;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public String getTestAffected() {
        return testAffected;
    }

    public void setTestAffected(String testAffected) {
        this.testAffected = testAffected;
    }

    public String getAlternateArrangement() {
        return alternateArrangement;
    }

    public void setAlternateArrangement(String alternateArrangement) {
        this.alternateArrangement = alternateArrangement;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getAttender() {
        return attender;
    }

    public void setAttender(String attender) {
        this.attender = attender;
    }

    public String getSolutionRootCause() {
        return solutionRootCause;
    }

    public void setSolutionRootCause(String solutionRootCause) {
        this.solutionRootCause = solutionRootCause;
    }

    public String getSolutionActionTaken() {
        return solutionActionTaken;
    }

    public void setSolutionActionTaken(String solutionActionTaken) {
        this.solutionActionTaken = solutionActionTaken;
    }

    public String getSolutionBy() {
        return solutionBy;
    }

    public void setSolutionBy(String solutionBy) {
        this.solutionBy = solutionBy;
    }

    public LocalDate getSolutionGivenOn() {
        return solutionGivenOn;
    }

    public void setSolutionGivenOn(LocalDate solutionGivenOn) {
        this.solutionGivenOn = solutionGivenOn;
    }

    public LocalDate getBdrrOfDate() {
        return bdrrOfDate;
    }

    public void setBdrrOfDate(LocalDate bdrrOfDate) {
        this.bdrrOfDate = bdrrOfDate;
    }

    public String getAreaAttender() {
        return areaAttender;
    }

    public void setAreaAttender(String areaAttender) {
        this.areaAttender = areaAttender;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
    }

    public String getPartUsed() {
        return partUsed;
    }

    public void setPartUsed(String partUsed) {
        this.partUsed = partUsed;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartDescriptions() {
        return partDescriptions;
    }

    public void setPartDescriptions(String partDescriptions) {
        this.partDescriptions = partDescriptions;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBreakDownDescription() {
        return breakDownDescription;
    }

    public void setBreakDownDescription(String breakDownDescription) {
        this.breakDownDescription = breakDownDescription;
    }

    public String getInitialAnalysis() {
        return initialAnalysis;
    }

    public void setInitialAnalysis(String initialAnalysis) {
        this.initialAnalysis = initialAnalysis;
    }

    public String getWorkDoneDescription() {
        return workDoneDescription;
    }

    public void setWorkDoneDescription(String workDoneDescription) {
        this.workDoneDescription = workDoneDescription;
    }

    @Override
    public String toString() {
        return "BDRREntryDTO{" +
                "bdrrNumber='" + bdrrNumber + '\'' +
                ", status='" + status + '\'' +
                ", raisedOn=" + raisedOn +
                ", raisedBy='" + raisedBy + '\'' +
                ", shift='" + shift + '\'' +
                ", testbedId=" + testbedId +
                ", equipmentId=" + equipmentId +
                ", subEquipmentId=" + subEquipmentId +
                ", problemId=" + problemId +
                ", testAffected='" + testAffected + '\'' +
                ", alternateArrangement='" + alternateArrangement + '\'' +
                ", suggestion='" + suggestion + '\'' +
                ", attender='" + attender + '\'' +
                ", solutionRootCause='" + solutionRootCause + '\'' +
                ", solutionActionTaken='" + solutionActionTaken + '\'' +
                ", solutionBy='" + solutionBy + '\'' +
                ", solutionGivenOn=" + solutionGivenOn +
                ", bdrrOfDate=" + bdrrOfDate +
                ", areaAttender='" + areaAttender + '\'' +
                ", targetDate=" + targetDate +
                ", closingDate=" + closingDate +
                ", partUsed='" + partUsed + '\'' +
                ", partNumber='" + partNumber + '\'' +
                ", partDescriptions='" + partDescriptions + '\'' +
                ", quantity='" + quantity + '\'' +
                ", breakDownDescription='" + breakDownDescription + '\'' +
                ", initialAnalysis='" + initialAnalysis + '\'' +
                ", workDoneDescription='" + workDoneDescription + '\'' +
                '}';
    }
}
