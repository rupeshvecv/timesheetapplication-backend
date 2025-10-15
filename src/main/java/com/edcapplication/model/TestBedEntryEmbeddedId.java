package com.edcapplication.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class TestBedEntryEmbeddedId implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Column(name = "testbed_id")
    private Long testbedId;

    @Column(name = "raised_on")
    private LocalDate raisedOn;

    @Column(name = "shift")
    private String shift;

    //Default constructor (required for JPA)
    public TestBedEntryEmbeddedId() {
    }

    //Constructor that matches your usage
    public TestBedEntryEmbeddedId(Long testbedId, LocalDate raisedOn, String shift) {
        this.testbedId = testbedId;
        this.raisedOn = raisedOn;
        this.shift = shift;
    }
    
    //Constructor that matches your usage
    public TestBedEntryEmbeddedId(Long testbedId) {
        this.testbedId = testbedId;
    }

    // Getters and setters
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

    // Required equals and hashCode for composite keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestBedEntryEmbeddedId)) return false;
        TestBedEntryEmbeddedId that = (TestBedEntryEmbeddedId) o;
        return testbedId == that.testbedId &&
                Objects.equals(raisedOn, that.raisedOn) &&
                Objects.equals(shift, that.shift);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testbedId, raisedOn, shift);
    }
}
