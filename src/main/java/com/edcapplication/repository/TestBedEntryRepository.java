package com.edcapplication.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edcapplication.model.TestBedEntry;
import com.edcapplication.model.TestBedEntryEmbeddedId;

@Repository
public interface TestBedEntryRepository extends JpaRepository<TestBedEntry, TestBedEntryEmbeddedId> {
	boolean  existsById(TestBedEntryEmbeddedId id);

	//Fetch all entries for a given testbedId
    List<TestBedEntry> findById_TestbedIdAndId_RaisedOnBetween(Long testbedId,LocalDate startDate, LocalDate endDate);
    
    // Optional: find by shift + date range
    List<TestBedEntry> findById_ShiftAndId_RaisedOnBetween(
            String shift,
            LocalDate startDate,
            LocalDate endDate
    );

    // Optional: find all by date range
    List<TestBedEntry> findById_RaisedOnBetween(
            LocalDate startDate,
            LocalDate endDate
    );
}
