package com.edcapplication.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.edcapplication.model.Equipment;
import com.edcapplication.model.Problem;

@Repository
public interface ProblemRepository extends CrudRepository<Problem, Long> {
	List<Problem> findByEquipment(Equipment equipment);
}


