package com.timesheetapplication.service;

import java.time.LocalDate;
import java.util.List;

import com.timesheetapplication.model.HolidayMaster;

public interface HolidayMasterService {

    HolidayMaster createHoliday(HolidayMaster holiday);

    HolidayMaster getHolidayByDate(LocalDate date);

    List<HolidayMaster> getAllActiveHolidays();

    List<HolidayMaster> getAllHolidays();

    void deleteHoliday(Long id);
}

