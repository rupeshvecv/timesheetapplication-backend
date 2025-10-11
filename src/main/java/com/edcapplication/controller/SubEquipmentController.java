package com.edcapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edcapplication.dao.SubEquipmentDao;
import com.edcapplication.model.SubEquipment;
import com.edcapplication.service.SubEquipmentService;

@RestController
@CrossOrigin
@RequestMapping("/api/edcapplication")
public class SubEquipmentController {

	@Autowired
	SubEquipmentService subEquipmentService;
	
	@GetMapping("/subEquipments")
	/*
	 * public List<SubEquipment> getSubEquipments() { return
	 * subEquipmentService.getSubEquipments(); }
	 */
	 public List<SubEquipmentDao> getAllSubEquipments() {
        return subEquipmentService.getAllSubEquipments();
    }
	
	@GetMapping("/subEquipments/{id}")
	public SubEquipment getTeamById(@PathVariable("id") Long subEquipmentId) {
		return subEquipmentService.getSubEquipmentById(subEquipmentId);
	}
	
	@PostMapping(("/subEquipments"))
	/*
	 * public SubEquipment addSubEquipment(@RequestBody SubEquipment subEquipment) {
	 * return subEquipmentService.addSubEquipment(subEquipment); }
	 */
	public SubEquipmentDao createSubEquipment(@RequestBody SubEquipmentDao subEquipmentDao) {
        return subEquipmentService.addSubEquipment(subEquipmentDao);
    }
	
	@PutMapping("/subEquipments/{id}")
	public SubEquipmentDao updateSubEquipment(@PathVariable("id") Long subEquipmentId,@RequestBody SubEquipmentDao subEquipmentDao) {
        return subEquipmentService.updateSubEquipment(subEquipmentId,subEquipmentDao);
    }
	
	@DeleteMapping("/subEquipments/{id}")
	public void deleteSubEquipment(@PathVariable("id") Long id) {
		System.out.println("SubEquipmentController.deleteSubEquipment(id) "+id);
		subEquipmentService.deleteSubEquipment(id);
    }
	
	 //Get all SubEquipments by Equipment ID
    @GetMapping("/subEquipments/equipment/{equipmentId}")
    public ResponseEntity<List<SubEquipmentDao>> getSubEquipmentsByEquipmentId(@PathVariable Long equipmentId) {
        List<SubEquipmentDao> subEquipments = subEquipmentService.getSubEquipmentsByEquipmentId(equipmentId);
        return ResponseEntity.ok(subEquipments);
    }
}
