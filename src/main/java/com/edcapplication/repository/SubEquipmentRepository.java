package com.edcapplication.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.edcapplication.model.Equipment;
import com.edcapplication.model.SubEquipment;

@Repository
public interface SubEquipmentRepository extends CrudRepository<SubEquipment, Long> {
	 List<SubEquipment> findByEquipment(Equipment equipment);
}


