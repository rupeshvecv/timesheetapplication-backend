package com.edcapplication.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edcapplication.model.TestBedEntry;
import com.edcapplication.model.TestBedEntryEmbeddedId;
import com.edcapplication.projection.TestBedEntryProjection;

@Repository
public interface TestBedEntryRepository extends JpaRepository<TestBedEntry, TestBedEntryEmbeddedId> {
	boolean  existsById(TestBedEntryEmbeddedId id);

	//Fetch all entries for a given testbedId
    List<TestBedEntry> findById_TestbedIdAndId_RaisedOnBetween(Long testbedId,LocalDate startDate, LocalDate endDate);
    
    //Optional: find by shift + date range
    List<TestBedEntry> findById_ShiftAndId_RaisedOnBetween(
            String shift,
            LocalDate startDate,
            LocalDate endDate
    );

    //Optional: find all by date range
    List<TestBedEntry> findById_RaisedOnBetween(
            LocalDate startDate,
            LocalDate endDate
    );
    
    @Query("""
            SELECT 
                t.id.testbedId AS testbedId,
                t.id.raisedOn AS raisedOn,
                t.id.shift AS shift,
                t.time AS time,
                t.raisedBy AS raisedBy,
                t.testBedUser AS testBedUser,
                p.id AS projectId,
                p.projectCode AS projectName,
                t.plannedHours AS plannedHours,
                t.uptimeHours AS uptimeHours,
                t.utilizationHours AS utilizationHours,
                t.validationHours AS validationHours,
                t.testDescription AS testDescription,
                t.testDescriptionHours AS testDescriptionHours,
                t.workonEngineRemarks AS workonEngineRemarks,
                t.workonEngineHours AS workonEngineHours,
                t.setUpRemarks AS setUpRemarks,
                t.setUpHours AS setUpHours,
                t.breakDownRemarks AS breakDownRemarks,
                t.breakDownHours AS breakDownHours,
                t.noManPowerRemarks AS noManPowerRemarks,
                t.noManPowerHours AS noManPowerHours,
                t.powerCutRemarks AS powerCutRemarks,
                t.powerCutHours AS powerCutHours,
                t.anyOtherRemarks AS anyOtherRemarks,
                t.anyOtherHours AS anyOtherHours,
                t.coummulativeDescription AS coummulativeDescription,
                t.totalSum AS totalSum
            FROM TestBedEntry t
            LEFT JOIN t.project p
            WHERE t.id.raisedOn BETWEEN :startDate AND :endDate
            ORDER BY t.id.raisedOn ASC
            """)
    List<TestBedEntryProjection> findAllTestBedEntryByDateRangeProjected(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    //Projection: fetch only required fields
    @Query("""
        SELECT 
            t.id.testbedId AS testbedId,
            t.id.raisedOn AS raisedOn,
            t.id.shift AS shift,
            t.time AS time,
            t.raisedBy AS raisedBy,
            t.testBedUser AS testBedUser,
            p.id AS projectId,
            p.projectCode AS projectName,
            t.plannedHours AS plannedHours,
            t.uptimeHours AS uptimeHours,
            t.utilizationHours AS utilizationHours,
            t.validationHours AS validationHours,
            t.testDescription AS testDescription,
            t.testDescriptionHours AS testDescriptionHours,
            t.workonEngineRemarks AS workonEngineRemarks,
            t.workonEngineHours AS workonEngineHours,
            t.setUpRemarks AS setUpRemarks,
            t.setUpHours AS setUpHours,
            t.breakDownRemarks AS breakDownRemarks,
            t.breakDownHours AS breakDownHours,
            t.noManPowerRemarks AS noManPowerRemarks,
            t.noManPowerHours AS noManPowerHours,
            t.powerCutRemarks AS powerCutRemarks,
            t.powerCutHours AS powerCutHours,
            t.anyOtherRemarks AS anyOtherRemarks,
            t.anyOtherHours AS anyOtherHours,
            t.coummulativeDescription AS coummulativeDescription,
            t.totalSum AS totalSum
        FROM TestBedEntry t
        LEFT JOIN t.project p
        WHERE t.id.testbedId = :testbedId 
          AND t.id.raisedOn BETWEEN :startDate AND :endDate
        """)
    List<TestBedEntryProjection> findAllByTestBedEntryDateRangeProjected(
            @Param("testbedId") Long testbedId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("""
            SELECT 
                t.id.testbedId AS testbedId,
                t.id.raisedOn AS raisedOn,
                t.id.shift AS shift,
                t.time AS time,
                t.raisedBy AS raisedBy,
                t.testBedUser AS testBedUser,

                p.id AS projectId,
                p.projectCode AS projectName,

                t.plannedHours AS plannedHours,
                t.uptimeHours AS uptimeHours,
                t.utilizationHours AS utilizationHours,
                t.validationHours AS validationHours,

                t.testDescription AS testDescription,
                t.testDescriptionHours AS testDescriptionHours,

                t.workonEngineRemarks AS workonEngineRemarks,
                t.workonEngineHours AS workonEngineHours,

                t.setUpRemarks AS setUpRemarks,
                t.setUpHours AS setUpHours,

                t.breakDownRemarks AS breakDownRemarks,
                t.breakDownHours AS breakDownHours,

                t.noManPowerRemarks AS noManPowerRemarks,
                t.noManPowerHours AS noManPowerHours,

                t.powerCutRemarks AS powerCutRemarks,
                t.powerCutHours AS powerCutHours,

                t.anyOtherRemarks AS anyOtherRemarks,
                t.anyOtherHours AS anyOtherHours,

                t.coummulativeDescription AS coummulativeDescription,
                t.totalSum AS totalSum
            FROM TestBedEntry t
            LEFT JOIN t.project p
            ORDER BY t.id.raisedOn ASC
            """)
    List<TestBedEntryProjection> findAllTestBedEntryEntriesProjected();
    
    @Query("""
            SELECT 
                t.id.testbedId AS testbedId,
                t.id.raisedOn AS raisedOn,
                t.id.shift AS shift,
                t.time AS time,
                t.raisedBy AS raisedBy,
                t.testBedUser AS testBedUser,

                p.id AS projectId,
                p.projectCode AS projectName,

                t.plannedHours AS plannedHours,
                t.uptimeHours AS uptimeHours,
                t.utilizationHours AS utilizationHours,
                t.validationHours AS validationHours,

                t.testDescription AS testDescription,
                t.testDescriptionHours AS testDescriptionHours,

                t.workonEngineRemarks AS workonEngineRemarks,
                t.workonEngineHours AS workonEngineHours,

                t.setUpRemarks AS setUpRemarks,
                t.setUpHours AS setUpHours,

                t.breakDownRemarks AS breakDownRemarks,
                t.breakDownHours AS breakDownHours,

                t.noManPowerRemarks AS noManPowerRemarks,
                t.noManPowerHours AS noManPowerHours,

                t.powerCutRemarks AS powerCutRemarks,
                t.powerCutHours AS powerCutHours,

                t.anyOtherRemarks AS anyOtherRemarks,
                t.anyOtherHours AS anyOtherHours,

                t.coummulativeDescription AS coummulativeDescription,
                t.totalSum AS totalSum

            FROM TestBedEntry t
            LEFT JOIN t.project p
            WHERE 
                t.id.shift = :shift
                AND t.id.raisedOn BETWEEN :startDate AND :endDate
            ORDER BY t.id.raisedOn ASC, t.time ASC
            """)
    List<TestBedEntryProjection> findAllByShiftDateRangeProjected(
            @Param("shift") String shift,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
