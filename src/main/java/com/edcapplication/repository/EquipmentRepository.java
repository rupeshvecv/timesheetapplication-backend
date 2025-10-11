package com.edcapplication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.edcapplication.model.Equipment;

@Repository
public interface EquipmentRepository extends CrudRepository<Equipment, Long> {

}


