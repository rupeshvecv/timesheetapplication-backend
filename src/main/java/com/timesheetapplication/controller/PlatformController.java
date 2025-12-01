package com.timesheetapplication.controller;

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

import com.timesheetapplication.model.Platform;
import com.timesheetapplication.service.PlatformService;

@RestController
@CrossOrigin
@RequestMapping("/api/timesheetapplication")
public class PlatformController {

	@Autowired
	PlatformService platformService;
	
	@GetMapping("/platforms")
	public List<Platform> getAllPlatforms() {
		return platformService.getAllPlatforms();
	}
	
	@GetMapping("/platforms/{id}")
	public Platform getTeamById(@PathVariable Long id) {
		return platformService.getPlatformById(id);
	}
	
	@PostMapping(("/platforms"))
    public Platform addPlatform(@RequestBody Platform platform) {
        return platformService.addPlatform(platform);
    }
	
	@PutMapping("/platforms/{id}")
    public Platform updatePlatform(@PathVariable Long id, @RequestBody Platform updatedPlatform) {
        return platformService.updatePlatform(id, updatedPlatform);
    }
	
	@DeleteMapping("/platforms/{id}")
	public void deletePlatform(@PathVariable Long id) {
		System.out.println("PlatformController.deletePlatform(id) "+id);
		platformService.deletePlatform(id);
    }

	
}
