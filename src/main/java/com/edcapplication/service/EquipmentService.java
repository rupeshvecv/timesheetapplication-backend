package com.edcapplication.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.edcapplication.dao.EquipmentDao;
import com.edcapplication.exception.BadRequestException;
import com.edcapplication.exception.ResourceNotFoundException;
import com.edcapplication.model.Equipment;
import com.edcapplication.repository.EquipmentRepository;

@Service
public class EquipmentService {

private EquipmentRepository equipmentRepository;
	
	public EquipmentService(EquipmentRepository equipmentRepository) {
		this.equipmentRepository = equipmentRepository;
	}
	
	/*
	 * public List<Equipment> getEquipments() { return (List<Equipment>)
	 * equipmentRepository.findAll(); }
	 */
	
	 public List<EquipmentDao> getAllEquipments() {
	        List<Equipment> equipments = (List<Equipment>) equipmentRepository.findAll();
	        return equipments.stream()
	                .map(e -> new EquipmentDao(e.getId(), e.getEquipmentName()))
	                .collect(Collectors.toList());
	    }
	
		/*
		 * public Equipment getEquipmentById(Integer id) { Optional<Equipment>
		 * EquipmentResponse = equipmentRepository.findById(id); Equipment Equipment =
		 * EquipmentResponse.get(); return Equipment; }
		 */
	 
	 public EquipmentDao getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + id));

        return new EquipmentDao(equipment.getId(), equipment.getEquipmentName());
    }
	
	/*
	 * public EquipmentDao addEquipment(Equipment equipment) { Equipment saved =
	 * equipmentRepository.save(equipment); return new EquipmentDao(saved.getId(),
	 * saved.getEquipmentName()); }
	 */
	 
	 public EquipmentDao addEquipment(Equipment equipment) {
        if (equipment.getEquipmentName() == null || equipment.getEquipmentName().isBlank()) {
            throw new BadRequestException("Equipment name cannot be empty.");
        }

        Equipment saved = equipmentRepository.save(equipment);
        return new EquipmentDao(saved.getId(), saved.getEquipmentName());
    }
	
	/*
	 * public Equipment updateEquipment(Equipment equipment) { return
	 * equipmentRepository.save(equipment); }
	 */
	
	public Equipment updateEquipment(Long id, Equipment equipment) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + id));

        if (equipment.getEquipmentName() == null || equipment.getEquipmentName().isBlank()) {
            throw new BadRequestException("Equipment name cannot be empty.");
        }

        existing.setEquipmentName(equipment.getEquipmentName());
        return equipmentRepository.save(existing);
    }
	
	public void deleteEquipment(Long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipment not found with ID: " + id);
        }
        equipmentRepository.deleteById(id);
    }
}
