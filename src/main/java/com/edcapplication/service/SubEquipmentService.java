package com.edcapplication.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edcapplication.dao.EquipmentDao;
import com.edcapplication.dao.SubEquipmentDao;
import com.edcapplication.exception.BadRequestException;
import com.edcapplication.exception.ResourceNotFoundException;
import com.edcapplication.model.Equipment;
import com.edcapplication.model.SubEquipment;
import com.edcapplication.repository.EquipmentRepository;
import com.edcapplication.repository.SubEquipmentRepository;

@Service
public class SubEquipmentService {

private SubEquipmentRepository subequipmentRepository;

@Autowired
private EquipmentRepository equipmentRepository;
	
	public SubEquipmentService(SubEquipmentRepository subequipmentRepository) {
		this.subequipmentRepository = subequipmentRepository;
	}
	
	/*
	 * public List<SubEquipment> getSubEquipments() { return (List<SubEquipment>)
	 * subequipmentRepository.findAll(); }
	 */
	 public List<SubEquipmentDao> getAllSubEquipments() {
		 List<SubEquipment> subEquipments = (List<SubEquipment>) subequipmentRepository.findAll();
		 if (subEquipments.isEmpty()){
	            throw new ResourceNotFoundException("No SubEquipments found in the system");
	     }
		 
		 return subEquipments.stream()
				 .map(s -> new SubEquipmentDao(s.getId(), s.getSubequipmentName(), s.getEquipment().getId()))
	                .collect(Collectors.toList());
	 }

	public SubEquipment getSubEquipmentById(Long id) {
		if (id == null) {
            throw new BadRequestException("SubEquipment ID must not be null");
        }
		Optional<SubEquipment> SubEquipmentResponse =  subequipmentRepository.findById(id);
		SubEquipment SubEquipment = SubEquipmentResponse.get();
		return SubEquipment;
	}
	
	/*
	 * public SubEquipmentDao addSubEquipment(SubEquipment s) { SubEquipment saved =
	 * subequipmentRepository.save(s); return new SubEquipmentDao(saved.getId(),
	 * saved.getSubequipmentName(), saved.getEquipment().getId()); }
	 */
	
	public SubEquipmentDao addSubEquipment(SubEquipmentDao dao) {
		
		if (dao == null) {
            throw new BadRequestException("SubEquipment data cannot be null");
        }
        if (dao.getSubequipmentName() == null || dao.getSubequipmentName().trim().isEmpty()) {
            throw new BadRequestException("SubEquipment name is required");
        }
        if (dao.getEquipmentId() == null) {
            throw new BadRequestException("Equipment ID is required for SubEquipment");
        }
	    Equipment equipment = equipmentRepository.findById(dao.getEquipmentId())
	                            .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

	    SubEquipment sub = new SubEquipment();
	    sub.setSubequipmentName(dao.getSubequipmentName());
	    sub.setEquipment(equipment);

	    SubEquipment saved = subequipmentRepository.save(sub);
	    return new SubEquipmentDao(saved.getId(), saved.getSubequipmentName(), equipment.getId());
	}
	/*
	 * public SubEquipment addSubEquipment(SubEquipment subequipment) { SubEquipment
	 * subequipment1 = subequipmentRepository.save(subequipment); return
	 * subequipment1; }
	 */
	
	/*
	 * public SubEquipment updateSubEquipment(SubEquipment subequipment) { return
	 * subequipmentRepository.save(subequipment); }
	 */
	
	public SubEquipmentDao updateSubEquipment(Long id, SubEquipmentDao dao) {
        if (id == null) {
            throw new BadRequestException("SubEquipment ID is required for update");
        }
        if (dao == null) {
            throw new BadRequestException("SubEquipment data cannot be null");
        }

        SubEquipment existing = subequipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubEquipment not found with ID: " + id));

        if (dao.getSubequipmentName() != null && !dao.getSubequipmentName().trim().isEmpty()) {
            existing.setSubequipmentName(dao.getSubequipmentName());
        }

        if (dao.getEquipmentId() != null) {
            Equipment equipment = equipmentRepository.findById(dao.getEquipmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + dao.getEquipmentId()));
            existing.setEquipment(equipment);
        }

        SubEquipment updated = subequipmentRepository.save(existing);
        return new SubEquipmentDao(updated.getId(), updated.getSubequipmentName(), updated.getEquipment().getId());
    }
	
	public void deleteSubEquipment(Long id) {
		if (id == null) {
            throw new BadRequestException("SubEquipment ID is required for deletion");
        }
		if (!subequipmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("SubEquipment not found with ID: " + id);
        }
		subequipmentRepository.deleteById(id);
	}
	
	 //Get SubEquipments by Equipment ID
    public List<SubEquipmentDao> getSubEquipmentsByEquipmentId(Long equipmentId) {
        Equipment equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + equipmentId));

        List<SubEquipment> subEquipments = subequipmentRepository.findByEquipment(equipment);

        return subEquipments.stream()
                .map(s -> new SubEquipmentDao(s.getId(), s.getSubequipmentName(), equipment.getId()))
                .collect(Collectors.toList());
    }
}
