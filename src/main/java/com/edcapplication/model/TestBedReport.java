package com.edcapplication.model;

import jakarta.persistence.*;

@Entity
@Table(name = "testbed_report")
public class TestBedReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "testbed_name")
    private String testBedName;

    private double plannedHrs;
    private double uptimeHrs;
    private double uptimePercent;
    private double utilizationHrs;
    private double utilizationPercent;
    private double validationHrs;
    private double validationPercent;
    private double shortfallHrs;
    private double shortfallPercent;

    private double breakdownHrs;
    private double manpowerShortageHrs;
    private double powerCutHrs;
    private double otherHrs;

    public TestBedReport() {}

    public TestBedReport(String testBedName, double plannedHrs, double uptimeHrs, double uptimePercent,
                         double utilizationHrs, double utilizationPercent, double validationHrs, double validationPercent,
                         double shortfallHrs, double shortfallPercent, double breakdownHrs,
                         double manpowerShortageHrs, double powerCutHrs, double otherHrs) {
        this.testBedName = testBedName;
        this.plannedHrs = plannedHrs;
        this.uptimeHrs = uptimeHrs;
        this.uptimePercent = uptimePercent;
        this.utilizationHrs = utilizationHrs;
        this.utilizationPercent = utilizationPercent;
        this.validationHrs = validationHrs;
        this.validationPercent = validationPercent;
        this.shortfallHrs = shortfallHrs;
        this.shortfallPercent = shortfallPercent;
        this.breakdownHrs = breakdownHrs;
        this.manpowerShortageHrs = manpowerShortageHrs;
        this.powerCutHrs = powerCutHrs;
        this.otherHrs = otherHrs;
    }

    // getters & setters
}
