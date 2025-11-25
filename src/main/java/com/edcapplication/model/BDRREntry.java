package com.edcapplication.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="bdrrentry_table") 
public class BDRREntry {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name= "bdrr_number")
	private String bdrrNumber;
	
	@Column(name= "status")
	private String status;
	
	@Column(name= "raised_on")
	private LocalDateTime raisedOn;
	
	@Column(name= "raised_by")
	private String raisedBy;
	
	@Column(name= "shift")
	private String shift;
	
	@ManyToOne
    @JoinColumn(name = "testbed_id")  //FK column in bdrr_entry table
    private TestBed testBed;
	
	@ManyToOne
    @JoinColumn(name = "equipment_id")
	private Equipment equipment;
	
	@ManyToOne
    @JoinColumn(name = "subequipment_id")
	private SubEquipment subEquipment;
	
	@ManyToOne
    @JoinColumn(name = "problem_id")
	private Problem problem;
	
	@Column(name= "test_affected")
	private String testAffected;
	
	@Column(name= "alternate_arrangement")
	private String alternateArrangement;
	
	@Column(name= "suggestion")
	private String suggestion;
	
	@Column(name= "attender")
	private String attender;
	
	@Column(name= "closing_date")
	private LocalDateTime closingDate;
	
	@Column(name= "break_down_description")
	private String breakDownDescription;
	
	@Column(name= "initial_analysis")
	private String initialAnalysis;
	
	@Column(name= "work_done_description")
	private String workDoneDescription;
	
	@Column(name= "solution_root_cause")
	private String solutionRootCause;
	
	@Column(name= "solution_action_taken")
	private String solutionActionTaken;
	
	@Column(name= "solution_by")
	private String solutionBy;
	
	@Column(name= "solution_given_on")
	private LocalDateTime solutionGivenOn;
	
	@Column(name= "bdrr_of_date")
	private LocalDateTime bdrrOfDate;
	
	@Column(name= "area_attender")
	private String areaAttender;
	
	@Column(name= "target_date")
	private LocalDateTime targetDate;
	
	@Column(name= "part_used")
	private String partUsed;
	
	@Column(name= "part_number")
	private String partNumber;
	
	@Column(name= "part_descriptions")
	private String partDescriptions;
	
	@Column(name= "quantity")
	private String quantity;
	
	public BDRREntry() {
		super();
	}
	public BDRREntry(Long id, String bdrrNumber, String status, LocalDateTime raisedOn, String raisedBy, String shift,
			TestBed testBed, Equipment equipment, SubEquipment subEquipment, Problem problem, String testAffected,
			String alternateArrangement, String suggestion, String attender, String solutionRootCause,
			String solutionActionTaken, String solutionBy, LocalDateTime solutionGivenOn, LocalDateTime bdrrOfDate,
			String areaAttender, LocalDateTime targetDate, LocalDateTime closingDate, String partUsed, String partNumber,
			String partDescriptions, String quantity, String breakDownDescription, String initialAnalysis,
			String workDoneDescription) {
		super();
		this.id = id;
		this.bdrrNumber = bdrrNumber;
		this.status = status;
		this.raisedOn = raisedOn;
		this.raisedBy = raisedBy;
		this.shift = shift;
		this.testBed = testBed;
		this.equipment = equipment;
		this.subEquipment = subEquipment;
		this.problem = problem;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public LocalDateTime getRaisedOn() {
		return raisedOn;
	}

	public void setRaisedOn(LocalDateTime raisedOn) {
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

	public TestBed getTestBed() {
		return testBed;
	}

	public void setTestBed(TestBed testBed) {
		this.testBed = testBed;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public SubEquipment getSubEquipment() {
		return subEquipment;
	}

	public void setSubEquipment(SubEquipment subEquipment) {
		this.subEquipment = subEquipment;
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
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

	public LocalDateTime getSolutionGivenOn() {
		return solutionGivenOn;
	}

	public void setSolutionGivenOn(LocalDateTime solutionGivenOn) {
		this.solutionGivenOn = solutionGivenOn;
	}

	public LocalDateTime getBdrrOfDate() {
		return bdrrOfDate;
	}

	public void setBdrrOfDate(LocalDateTime bdrrOfDate) {
		this.bdrrOfDate = bdrrOfDate;
	}

	public String getAreaAttender() {
		return areaAttender;
	}

	public void setAreaAttender(String areaAttender) {
		this.areaAttender = areaAttender;
	}

	public LocalDateTime getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(LocalDateTime targetDate) {
		this.targetDate = targetDate;
	}

	public LocalDateTime getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(LocalDateTime closingDate) {
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
		return "BDRREntry [id=" + id + ", bdrrNumber=" + bdrrNumber+ ", status=" + status+ ", raisedOn=" + raisedOn+ ", raisedBy=" 
				+ raisedBy+ ", shift=" + shift+ ", equipment=" + equipment+ ", subEquipment=" + subEquipment+ ", testAffected="
				+ testAffected+ ", alternateArrangement=" + alternateArrangement+ ", suggestion=" + suggestion+ ", attender=" 
				+ attender+ ", solutionRootCause=" + solutionRootCause+ ", solutionActionTaken=" + solutionActionTaken+ ", solutionBy=" 
				+ solutionBy+ ", solutionGivenOn=" + solutionGivenOn+ ", bdrrOfDate=" + bdrrOfDate+ ", areaAttender=" + areaAttender+ ", targetDate=" 
				+ targetDate+ ", closingDate=" + closingDate+ ", partUsed=" + partUsed+ ", partNumber=" + partNumber+ ", partDescriptions=" 
				+ partDescriptions+ ", quantity=" + quantity+ ", breakDownDescription=" + breakDownDescription+ ", initialAnalysis=" 
				+ initialAnalysis+ ", workDoneDescription=" + workDoneDescription+"]";
	}
}
