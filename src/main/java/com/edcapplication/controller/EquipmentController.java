package com.edcapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.edcapplication.dao.EquipmentDao;
import com.edcapplication.model.Equipment;
import com.edcapplication.service.EquipmentService;

@RestController
@CrossOrigin
@RequestMapping("/api/edcapplication")
public class EquipmentController {

	@Autowired
	EquipmentService equipmentService;
	
	/*
	 * @GetMapping("/equipments") public List<EquipmentDao> getEquipments() { return
	 * equipmentService.getAllEquipments(); }
	 */
	
	@GetMapping("/equipments")
	public ResponseEntity<List<EquipmentDao>> getAllEquipments() {
        List<EquipmentDao> equipments = equipmentService.getAllEquipments();
        return ResponseEntity.ok(equipments);
    }
	
	/*
	 * @GetMapping("/equipments/{id}") public EquipmentDao
	 * getEquipmentById(@PathVariable Long id) { return
	 * equipmentService.getEquipmentById(id); }
	 */
	@GetMapping("/equipments/{id}")
	public ResponseEntity<EquipmentDao> getEquipmentById(@PathVariable("id") Long id) {
        EquipmentDao equipment = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(equipment);
    }
	
	/*
	 * @PostMapping(("/equipments")) public EquipmentDao
	 * createEquipment(@RequestBody Equipment equipment) { return
	 * equipmentService.addEquipment(equipment); }
	 */
	@PostMapping(("/equipments"))
	public ResponseEntity<EquipmentDao> addEquipment(@RequestBody Equipment equipment) {
        EquipmentDao created = equipmentService.addEquipment(equipment);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
	
	/*
	 * @PutMapping(("/equipments")) public Equipment updateEquipment(@RequestBody
	 * Equipment equipment) { return equipmentService.updateEquipment(equipment); }
	 */
	@PutMapping("/equipments/{id}")
	 public ResponseEntity<Equipment> updateEquipment(@PathVariable("id") Long id,@RequestBody Equipment equipment) {

	        Equipment updated = equipmentService.updateEquipment(id, equipment);
	        return ResponseEntity.ok(updated);
	    }
	
	@DeleteMapping("/equipments/{id}")
	public void deleteEquipment(@PathVariable("id") Long id) {
		System.out.println("EquipmentController.deleteEquipment(id) "+id);
		equipmentService.deleteEquipment(id);
    }
}
