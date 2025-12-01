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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.timesheetapplication.dao.ProjectDao;
import com.timesheetapplication.model.Project;
import com.timesheetapplication.service.ProjectService;

@RestController
@RequestMapping("/api/timesheetapplication")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/projects")
	public ResponseEntity<List<ProjectDao>> getAllProjects() {
		List<ProjectDao> projects = projectService.getAllProjects();
		return ResponseEntity.ok(projects);
	}

	@GetMapping("/projects/{id}")
	public ResponseEntity<Project> getProjectById(@PathVariable("id") Long id) {
		Project project = projectService.getProjectById(id);
		return ResponseEntity.ok(project);
	}

	@PostMapping(("/projects"))
	public ResponseEntity<ProjectDao> addProject(@RequestBody ProjectDao dao) {
		ProjectDao saved = projectService.addProject(dao);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/projects/{id}")
	public ResponseEntity<Project> updateProject(@PathVariable("id") Long id, @RequestBody Project project) {
		Project updated = projectService.updateProject(id, project);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/projects/{id}")
	public ResponseEntity<Void> deleteProject(@PathVariable("id") Long id) {
		projectService.deleteProject(id);
		return ResponseEntity.noContent().build();
	}
	
    @GetMapping("/projects/byplatform/{platformId}")
    public List<Project> getByPlatform(@PathVariable Long platformId) {
        return projectService.getProjectsByPlatform(platformId);
    }

    @GetMapping("/projects/bycategory/{categoryId}")
    public List<Project> getByCategory(@PathVariable Long categoryId) {
        return projectService.getProjectsByCategory(categoryId);
    }

    @GetMapping("/projects/filter")
    public List<Project> getByPlatformAndCategory(
            @RequestParam Long platformId,
            @RequestParam Long categoryId
    ) {
        return projectService.getProjects(platformId, categoryId);
    }
}

