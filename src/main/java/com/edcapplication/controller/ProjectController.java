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

import com.edcapplication.model.Project;
import com.edcapplication.service.ProjectService;

@RestController
@CrossOrigin
@RequestMapping("/api/edcapplication")
public class ProjectController {

	@Autowired
	ProjectService projectService;
	
	@GetMapping("/projects")
	public List<Project> getProjects() {
		return projectService.getProjects();
	}
	
	@GetMapping("/projects/{id}")
	public Project getProjectById(@PathVariable("id") Long projectId) {
		return projectService.getProjectById(projectId);
	}
	
	@PostMapping(("/projects"))
    public Project addProject(@RequestBody Project project) {
        return projectService.addProject(project);
    }
	
	@PutMapping(("/projects"))
	public Project updateProject(@RequestBody Project project) {
        return projectService.updateProject(project);
    }
	
	@DeleteMapping("/projects/{id}")
	public void deleteProject(@PathVariable("id") Long id) {
		System.out.println("ProjectController.deleteProject(id) "+id);
		projectService.deleteProject(id);
    }
}
