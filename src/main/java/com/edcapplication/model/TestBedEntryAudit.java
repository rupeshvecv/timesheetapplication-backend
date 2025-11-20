package com.edcapplication.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "testbed_entry_audit")
public class TestBedEntryAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditTBId;

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

    //Default constructor
    public TestBedEntryAudit() {
    }

    //Parameterized constructor
    public TestBedEntryAudit(Long auditTBId, String testBedName, String shift, String changedBy,
                             LocalDateTime changedOn, String operationType, String fieldValues) {
        this.auditTBId = auditTBId;
        this.testBedName = testBedName;
        this.shift = shift;
        this.changedBy = changedBy;
        this.changedOn = changedOn;
        this.operationType = operationType;
        this.fieldValues = fieldValues;
    }

    //Getters and Setters
    public Long getAuditTBId() {
        return auditTBId;
    }

    public void setAuditTBId(Long auditTBId) {
        this.auditTBId = auditTBId;
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
