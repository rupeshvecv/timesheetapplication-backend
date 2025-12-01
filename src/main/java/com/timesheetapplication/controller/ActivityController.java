package com.timesheetapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timesheetapplication.dao.ActivityDao;
import com.timesheetapplication.model.Activity;
import com.timesheetapplication.service.ActivityService;

@RestController
@RequestMapping("/api/timesheetapplication")
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@GetMapping("/activitys")
	public ResponseEntity<List<ActivityDao>> getAllActivitys() {
		List<ActivityDao> activitys = activityService.getAllActivitys();
		return ResponseEntity.ok(activitys);
	}

	@GetMapping("/activitys/{id}")
	public ResponseEntity<Activity> getActivityById(@PathVariable("id") Long id) {
		Activity activity = activityService.getActivityById(id);
		return ResponseEntity.ok(activity);
	}

	@PostMapping(("/activitys"))
	public ResponseEntity<ActivityDao> addActivity(@RequestBody ActivityDao dao) {
		ActivityDao saved = activityService.addActivity(dao);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/activitys/{id}")
	public ResponseEntity<Activity> updateActivity(@PathVariable("id") Long id, @RequestBody Activity activity) {
		Activity updated = activityService.updateActivity(id, activity);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/activitys/{id}")
	public ResponseEntity<Void> deleteActivity(@PathVariable("id") Long id) {
		activityService.deleteActivity(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/activities/bycategory/{categoryId}")
	public List<Activity> getByCategory(@PathVariable Long categoryId) {
		return activityService.getActivitiesByCategory(categoryId);
	}

	@GetMapping("/activities/byproject/{projectId}")
	public List<Activity> getByProject(@PathVariable Long projectId) {
		return activityService.getActivitiesByProject(projectId);
	}
}
