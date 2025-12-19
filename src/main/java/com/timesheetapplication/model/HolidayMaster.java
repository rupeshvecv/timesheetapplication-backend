package com.timesheetapplication.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "holiday_master")
public class HolidayMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holiday_date", nullable = false, unique = true)
    private LocalDate holidayDate;

    @Column(name = "holiday_name", nullable = false)
    private String holidayName;

    @Column(name = "location")
    private String location;

    @Column(name = "is_active")
    private Boolean isActive;

    // ---------------- Constructors ----------------

    public HolidayMaster() {
    }

    public HolidayMaster(Long id, LocalDate holidayDate, String holidayName, String location, Boolean isActive) {
        this.id = id;
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
        this.location = location;
        this.isActive = isActive;
    }

    // ---------------- Getters & Setters ----------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

