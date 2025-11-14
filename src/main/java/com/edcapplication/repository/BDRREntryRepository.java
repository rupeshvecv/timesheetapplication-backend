package com.edcapplication.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.edcapplication.model.BDRREntry;

@Repository
public interface BDRREntryRepository extends JpaRepository<BDRREntry, Long>, JpaSpecificationExecutor<BDRREntry> {
	@Query("SELECT MAX(b.id) FROM BDRREntry b")
	Optional<Long> findMaxId();
	@Query("SELECT COUNT(e) FROM BDRREntry e WHERE e.raisedOn = :raisedOn")
	Long countByRaisedOn(@Param("raisedOn") LocalDate raisedOn);
	
	List<BDRREntry> findByStatusAndRaisedByAndAttenderAndTestBed_IdAndRaisedOnBetween(
            String status,
            String raisedBy,
            String attender,
            Long testBedId,
            LocalDate startDate,
            LocalDate endDate
    );
}
