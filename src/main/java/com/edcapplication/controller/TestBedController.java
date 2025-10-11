package com.edcapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edcapplication.model.TestBed;
import com.edcapplication.service.TestBedService;

@RestController
@CrossOrigin
@RequestMapping("/api/edcapplication")
public class TestBedController {

	@Autowired
	TestBedService testBedService;
	
	@GetMapping("/testBeds")
	public List<TestBed> getAllTestBeds() {
		return testBedService.getAllTestBeds();
	}
	
	@GetMapping("/testBeds/{id}")
	public TestBed getTeamById(@PathVariable Long id) {
		return testBedService.getTestBedById(id);
	}
	
	@PostMapping(("/testBeds"))
    public TestBed addTestBed(@RequestBody TestBed testBed) {
        return testBedService.addTestBed(testBed);
    }
	
	@PutMapping("/testBeds/{id}")
    public TestBed updateTestBed(@PathVariable Long id, @RequestBody TestBed updatedTestBed) {
        return testBedService.updateTestBed(id, updatedTestBed);
    }
	
	@DeleteMapping("/testBeds/{id}")
	public void deleteTestBed(@PathVariable Long id) {
		System.out.println("TestBedController.deleteTestBed(id) "+id);
		testBedService.deleteTestBed(id);
    }
}
