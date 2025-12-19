package com.timesheetapplication.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timesheetapplication.dao.ProjectDao;
import com.timesheetapplication.exception.BusinessException;
import com.timesheetapplication.model.Category;
import com.timesheetapplication.model.Platform;
import com.timesheetapplication.model.Project;
import com.timesheetapplication.repository.CategoryRepository;
import com.timesheetapplication.repository.PlatformRepository;
import com.timesheetapplication.repository.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private PlatformRepository platformRepository;

    public ProjectService(ProjectRepository projectRepository, CategoryRepository categoryRepository, PlatformRepository platformRepository) {
	    this.projectRepository = projectRepository;
	    this.categoryRepository = categoryRepository;
	    this.platformRepository = platformRepository;
	}
	
	public List<ProjectDao> getAllProjects() {
		List<Project> projects = (List<Project>) projectRepository.findAll();
		if (projects.isEmpty()) {
			//throw new ResourceNotFoundException("No projects found in the database");
			throw new BusinessException("PRJ_001");
		}

		return projects.stream().map(p -> new ProjectDao(p.getId(), p.getProjectName(), p.getDescription(), p.getStatus(), p.getProjectOwner(), p.getProjectStartDate(), p.getProjectEndDate(), p.getCategory().getId(), p.getPlatform().getId()))
				.collect(Collectors.toList());
	}

	public Project getProjectById(Long id) {
		if (id == null) {
			//throw new BadRequestException("Project ID cannot be null");
			throw new BusinessException("PRJ_004");
		}

		return projectRepository.findById(id)
				//.orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
				.orElseThrow(() -> new BusinessException("PRJ_002",id.toString()));
	}

	public ProjectDao addProject(ProjectDao dao) {
		if (dao == null) {
			//throw new BadRequestException("Project data cannot be null");
			throw new BusinessException("PRJ_005");
		}
		if (dao.getProjectName() == null || dao.getProjectName().trim().isEmpty()) {
			//throw new BadRequestException("Project name is required");
			throw new BusinessException("PRJ_006");
		}
		if (dao.getCategoryId() == null) {
			//throw new BadRequestException("Category ID is required for creating a Project");
			throw new BusinessException("PRJ_009");
		}
		if (dao.getPlatformId() == null) {
			//throw new BadRequestException("Platform ID is required for creating a Project");
			throw new BusinessException("PRJ_010");
		}

		Category category = categoryRepository.findById(dao.getCategoryId())
				//.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + dao.getCategoryId()));
				.orElseThrow(() -> new BusinessException("CAT_002",dao.getCategoryId().toString()));
		
		Platform platform = platformRepository.findById(dao.getPlatformId())
				//.orElseThrow(() -> new ResourceNotFoundException("Platform not found with ID: " + dao.getPlatformId()));
				.orElseThrow(() -> new BusinessException("PLT_003",dao.getPlatformId().toString()));

		Project project = new Project();
		project.setProjectName(dao.getProjectName());
		project.setDescription(dao.getDescription());
		project.setProjectOwner(dao.getProjectOwner());
		project.setProjectStartDate(dao.getProjectStartDate());
		project.setProjectEndDate(dao.getProjectEndDate());
		project.setStatus(dao.getStatus());
		project.setCategory(category);
		project.setPlatform(platform);
		
		Project saved = projectRepository.save(project);
		return new ProjectDao(saved.getId(), saved.getProjectName(),saved.getDescription(),saved.getProjectOwner(),saved.getStatus(),saved.getProjectStartDate(),saved.getProjectEndDate(), category.getId(),platform.getId());
	}

	public Project updateProject(Long id, Project updatedProject) {
		if (id == null) {
			//throw new BadRequestException("Project ID is required for update");
			throw new BusinessException("PRJ_010");
		}

		Project existing = projectRepository.findById(id)
				//.orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
				.orElseThrow(() -> new BusinessException("PRJ_002",id.toString()));

		if (updatedProject.getProjectName() != null && !updatedProject.getProjectName().trim().isEmpty()) {
			existing.setProjectName(updatedProject.getProjectName());
		}

		if (updatedProject.getCategory() != null) {
			Long eqId = updatedProject.getCategory().getId();
			Category category = categoryRepository.findById(eqId)
					//.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + eqId));
					.orElseThrow(() -> new BusinessException("CAT_002",eqId.toString()));
			existing.setCategory(category);
		}
		
		if (updatedProject.getPlatform() != null) {
			Long eqId = updatedProject.getPlatform().getId();
			Platform platform = platformRepository.findById(eqId)
					//.orElseThrow(() -> new ResourceNotFoundException("Platform not found with ID: " + eqId));
					.orElseThrow(() -> new BusinessException("PLT_003",eqId.toString()));
			existing.setPlatform(platform);
		}

		return projectRepository.save(existing);
	}

	public void deleteProject(Long id) {
		if (id == null) {
			//throw new BadRequestException("Project ID cannot be null");
			throw new BusinessException("PRJ_004");
		}

		if (!projectRepository.existsById(id)) {
			//throw new ResourceNotFoundException("Project not found with ID: " + id);
			throw new BusinessException("PRJ_002",id.toString());
		}

		projectRepository.deleteById(id);
	}
	
    public List<Project> getProjectsByPlatform(Long platformId) {
        return projectRepository.findByPlatformId(platformId);
    }

    public List<Project> getProjectsByCategory(Long categoryId) {
        return projectRepository.findByCategoryId(categoryId);
    }

    public List<Project> getProjects(Long platformId, Long categoryId) {
        return projectRepository.findByPlatformIdAndCategoryId(platformId, categoryId);
    }
}

