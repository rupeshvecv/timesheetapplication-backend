package com.timesheetapplication.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "timesheet_entry_audit")
public class TimesheetEntryAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditTBId;

    @Column(nullable = false, length = 50)
    private String userName;

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
    public TimesheetEntryAudit() {
    }

    //Parameterized constructor
    public TimesheetEntryAudit(Long auditTBId, String userName,String changedBy,
                             LocalDateTime changedOn, String operationType, String fieldValues) {
        this.auditTBId = auditTBId;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
