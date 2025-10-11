package com.edcapplication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.edcapplication.model.BDRREntry;

@Repository
public interface BDRREntryRepository extends JpaRepository<BDRREntry, Long> {
	
	@Query("SELECT MAX(b.id) FROM BDRREntry b")
	Optional<Long> findMaxId();
	
}
