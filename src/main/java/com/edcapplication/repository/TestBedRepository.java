package com.edcapplication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.edcapplication.model.TestBed;

@Repository
public interface TestBedRepository extends CrudRepository<TestBed, Long> {

}


