package com.timesheetapplication.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheetapplication.exception.BusinessException;
import com.timesheetapplication.model.HolidayMaster;
import com.timesheetapplication.repository.HolidayMasterRepository;

@Service
@Transactional
public class HolidayMasterServiceImpl implements HolidayMasterService {

    private final HolidayMasterRepository holidayMasterRepository;

    public HolidayMasterServiceImpl(HolidayMasterRepository holidayMasterRepository) {
        this.holidayMasterRepository = holidayMasterRepository;
    }

    @Override
    public HolidayMaster createHoliday(HolidayMaster holiday) {

        if (holidayMasterRepository.existsByHolidayDate(holiday.getHolidayDate())) {
            throw new BusinessException("HOLIDAY_001", holiday.getHolidayDate().toString());
        }

        holiday.setIsActive(true);
        return holidayMasterRepository.save(holiday);
    }

    @Override
    public HolidayMaster getHolidayByDate(LocalDate date) {
        return holidayMasterRepository.findByHolidayDate(date)
                .orElseThrow(() -> new BusinessException("HOLIDAY_002", date.toString()));
    }

    @Override
    public List<HolidayMaster> getAllActiveHolidays() {
        List<HolidayMaster> holidays = holidayMasterRepository.findByIsActiveTrue();
        if (holidays.isEmpty()) {
            throw new BusinessException("HOLIDAY_003");
        }
        return holidays;
    }

    @Override
    public List<HolidayMaster> getAllHolidays() {
        return holidayMasterRepository.findAll();
    }

    @Override
    public void deleteHoliday(Long id) {
        HolidayMaster holiday = holidayMasterRepository.findById(id)
                .orElseThrow(() -> new BusinessException("HOLIDAY_004", id.toString()));

        holidayMasterRepository.delete(holiday);
    }
}

