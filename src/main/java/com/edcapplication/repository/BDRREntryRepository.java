package com.edcapplication.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.edcapplication.model.BDRREntry;
import com.edcapplication.projection.BDRREntryProjection;

@Repository
public interface BDRREntryRepository extends JpaRepository<BDRREntry, Long>, JpaSpecificationExecutor<BDRREntry> {
	@Query("SELECT MAX(b.id) FROM BDRREntry b")
	Optional<Long> findMaxId();
	
	@Query("SELECT COUNT(e) FROM BDRREntry e WHERE e.raisedOn = :raisedOn")
	Long countByRaisedOn(@Param("raisedOn") LocalDateTime raisedOn);
	
	@Query("""
		    SELECT COUNT(e)
		    FROM BDRREntry e
		    WHERE MONTH(e.raisedOn) = :month
		      AND YEAR(e.raisedOn) = :year
		""")
		Long countByRaisedOnMonthYear(
		        @Param("month") int month,
		        @Param("year") int year
		);
	
	List<BDRREntry> findByStatusAndRaisedByAndAttenderAndTestBed_IdAndRaisedOnBetween(
            String status,
            String raisedBy,
            String attender,
            Long testBedId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
	
	@Query("""
	    SELECT 
	     	b.id AS id,
	        b.bdrrNumber AS bdrrNumber,
	        b.status AS status,
	        b.raisedOn AS raisedOn,
	        b.raisedBy AS raisedBy,
	        b.shift AS shift,

	        tb.id AS testbedId,
	        tb.name AS testbedName,

	        e.id AS equipmentId,
	        e.equipmentName AS equipmentName,

	        se.id AS subEquipmentId,
	        se.subequipmentName AS subEquipmentName,

	        p.id AS problemId,
	        p.problemName AS problemName,

	        b.testAffected AS testAffected,
	        b.alternateArrangement AS alternateArrangement,
	        b.suggestion AS suggestion,
	        b.attender AS attender,

	        b.solutionRootCause AS solutionRootCause,
	        b.solutionActionTaken AS solutionActionTaken,
	        b.solutionBy AS solutionBy,
	        b.solutionGivenOn AS solutionGivenOn,

	        b.bdrrOfDate AS bdrrOfDate,
	        b.areaAttender AS areaAttender,
	        b.targetDate AS targetDate,
	        b.closingDate AS closingDate,

	        b.partUsed AS partUsed,
	        b.partNumber AS partNumber,
	        b.partDescriptions AS partDescriptions,
	        b.quantity AS quantity,

	        b.breakDownDescription AS breakDownDescription,
	        b.initialAnalysis AS initialAnalysis,
	        b.workDoneDescription AS workDoneDescription
	    FROM BDRREntry b
	    LEFT JOIN b.testBed tb
	    LEFT JOIN b.equipment e
	    LEFT JOIN b.subEquipment se
	    LEFT JOIN b.problem p
	    ORDER BY b.raisedOn DESC
	""")
	List<BDRREntryProjection> findAllBDRREntryProjected();

	@Query("""
	        SELECT 
	         	b.id AS id,
	            b.bdrrNumber AS bdrrNumber,
	            b.status AS status,
	            b.raisedOn AS raisedOn,
	            b.raisedBy AS raisedBy,
	            b.shift AS shift,

	            tb.id AS testbedId,
	            e.id AS equipmentId,
	            se.id AS subEquipmentId,
	            p.id AS problemId,

	            tb.name AS testBedName,
	            e.equipmentName AS equipmentName,
	            se.subequipmentName AS subequipmentName,
	            p.problemName AS problemName,

	            b.testAffected AS testAffected,
	            b.alternateArrangement AS alternateArrangement,
	            b.suggestion AS suggestion,
	            b.attender AS attender,

	            b.solutionRootCause AS solutionRootCause,
	            b.solutionActionTaken AS solutionActionTaken,
	            b.solutionBy AS solutionBy,
	            b.solutionGivenOn AS solutionGivenOn,

	            b.bdrrOfDate AS bdrrOfDate,
	            b.areaAttender AS areaAttender,
	            b.targetDate AS targetDate,
	            b.closingDate AS closingDate,

	            b.partUsed AS partUsed,
	            b.partNumber AS partNumber,
	            b.partDescriptions AS partDescriptions,
	            b.quantity AS quantity,

	            b.breakDownDescription AS breakDownDescription,
	            b.initialAnalysis AS initialAnalysis,
	            b.workDoneDescription AS workDoneDescription

	        FROM BDRREntry b
	        LEFT JOIN b.testBed tb
	        LEFT JOIN b.equipment e
	        LEFT JOIN b.subEquipment se
	        LEFT JOIN b.problem p
	        WHERE 
	            (:status IS NULL OR b.status = :status)
	            AND (:raisedBy IS NULL OR b.raisedBy = :raisedBy)
	            AND (:attender IS NULL OR b.attender = :attender)
	            AND (:testbedId IS NULL OR tb.id = :testbedId)
	            AND (:startDate IS NULL OR b.raisedOn >= :startDate)
	            AND (:endDate IS NULL OR b.raisedOn <= :endDate)
	        ORDER BY b.raisedOn DESC
	        """)
	List<BDRREntryProjection> findAllBDRREntryByFilters(
	        @Param("status") String status,
	        @Param("raisedBy") String raisedBy,
	        @Param("attender") String attender,
	        @Param("testbedId") Long testbedId,
	        @Param("startDate") LocalDateTime startDate,
	        @Param("endDate") LocalDateTime endDate
	);

}
