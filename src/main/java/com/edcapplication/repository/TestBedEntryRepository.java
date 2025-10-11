package com.edcapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.edcapplication.model.TestBedEntry;
import com.edcapplication.model.TestBedEntryEmbeddedId;

@Repository
public interface TestBedEntryRepository extends JpaRepository<TestBedEntry, TestBedEntryEmbeddedId> {
	boolean  existsById(TestBedEntryEmbeddedId id);

}
