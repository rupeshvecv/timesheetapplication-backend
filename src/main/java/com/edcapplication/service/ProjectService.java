package com.edcapplication.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.edcapplication.exception.BadRequestException;
import com.edcapplication.exception.ResourceNotFoundException;
import com.edcapplication.model.Project;
import com.edcapplication.repository.ProjectRepository;

@Service
public class ProjectService {

private ProjectRepository projectRepository;
	
	public ProjectService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}
	
	public List<Project> getProjects() {
		
		List<Project> projects = (List<Project>)projectRepository.findAll();
        if (projects.isEmpty()) {
            throw new ResourceNotFoundException("No projects found in the system");
        }
		//return (List<Project>) projectRepository.findAll();
        return projects;
	}
	
	public Project getProjectById(Long id) {
		if (id == null) {
            throw new BadRequestException("Project ID cannot be null");
        }
		Optional<Project> ProjectResponse =  projectRepository.findById(id);
		Project Project = ProjectResponse.get();
		return Project;
	}
	
	public Project addProject(Project project) {
		 if (project == null) {
	            throw new BadRequestException("Project data cannot be null");
	        }
	        if (project.getProjectCode() == null || project.getProjectCode().trim().isEmpty()) {
	            throw new BadRequestException("Project name is required");
	        }
		Project project1 =  projectRepository.save(project);
		return project1;
	}
	
	public Project updateProject(Project project) {
        return projectRepository.save(project);
    }
	
	public void deleteProject(Long id) {
		if (id == null) {
            throw new BadRequestException("Project ID is required for deletion");
        }

        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
		projectRepository.deleteById(id);
	}
}
