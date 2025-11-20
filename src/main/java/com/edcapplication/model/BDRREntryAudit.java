package com.edcapplication.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "bdrr_entry_audit")
public class BDRREntryAudit {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditBDRRId;

    @Column(nullable = false, length = 50)
    private String bdrrNumber;
    
    @Column(nullable = false, length = 50)
    private String testBedName;

    @Column(nullable = false, length = 50)
    private String shift;

    @Column(nullable = false, length = 100)
    private String changedBy;

    @Column(nullable = false)
    private LocalDateTime changedOn;

    @Column(name = "operation_type", length = 50)
    private String operationType;

    @Lob
    @Column(name = "field_values", columnDefinition = "LONGTEXT") // allows long strings
    private String fieldValues;

    public BDRREntryAudit() {}

	public BDRREntryAudit(Long auditBDRRId, String bdrrNumber, String testBedName, String shift, String changedBy,
			LocalDateTime changedOn, String operationType, String fieldValues) {
		super();
		this.auditBDRRId = auditBDRRId;
		this.bdrrNumber = bdrrNumber;
		this.testBedName = testBedName;
		this.shift = shift;
		this.changedBy = changedBy;
		this.changedOn = changedOn;
		this.operationType = operationType;
		this.fieldValues = fieldValues;
	}

	public Long getAuditBDRRId() {
		return auditBDRRId;
	}

	public void setAuditBDRRId(Long auditBDRRId) {
		this.auditBDRRId = auditBDRRId;
	}

	public String getBdrrNumber() {
		return bdrrNumber;
	}

	public void setBdrrNumber(String bdrrNumber) {
		this.bdrrNumber = bdrrNumber;
	}

	public String getTestBedName() {
		return testBedName;
	}

	public void setTestBedName(String testBedName) {
		this.testBedName = testBedName;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public LocalDateTime getChangedOn() {
		return changedOn;
	}

	public void setChangedOn(LocalDateTime changedOn) {
		this.changedOn = changedOn;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(String fieldValues) {
		this.fieldValues = fieldValues;
	}

}