package com.timesheetapplication.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timesheetapplication.model.HolidayMaster;

@Repository
public interface HolidayMasterRepository extends JpaRepository<HolidayMaster, Long> {

    Optional<HolidayMaster> findByHolidayDate(LocalDate holidayDate);
    boolean existsByHolidayDate(LocalDate holidayDate);
    List<HolidayMaster> findByIsActiveTrue();
}

