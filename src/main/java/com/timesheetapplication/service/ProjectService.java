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
			throw new BusinessException("PRJ_001");
		}

		 return projects.stream().map(p ->
         new ProjectDao(
                 p.getId(),
                 p.getProjectName(),
                 p.getDescription(),
                 p.getStatus(),
                 p.getProjectOwner(),
                 p.getProjectStartDate(),
                 p.getProjectEndDate(),
                 p.getCategory() != null ? p.getCategory().getId() : null,
                 p.getPlatform() != null ? p.getPlatform().getId() : null,
                 p.getCategory() != null ? p.getCategory().getCategoryName() : null,   // ✅ Category Name
                 p.getPlatform() != null ? p.getPlatform().getPlatformName() : null    // ✅ Platform Name
         )
 ).collect(Collectors.toList());
	}

	public Project getProjectById(Long id) {
		if (id == null) {
			throw new BusinessException("PRJ_004");
		}

		return projectRepository.findById(id)
				.orElseThrow(() -> new BusinessException("PRJ_002",id.toString()));
	}

	public ProjectDao addProject(ProjectDao dao) {
		if (dao == null) {
			throw new BusinessException("PRJ_005");
		}
		if (dao.getProjectName() == null || dao.getProjectName().trim().isEmpty()) {
			throw new BusinessException("PRJ_006");
		}
		if (dao.getCategoryId() == null) {
			throw new BusinessException("PRJ_009");
		}
		if (dao.getPlatformId() == null) {
			throw new BusinessException("PRJ_010");
		}

		Category category = categoryRepository.findById(dao.getCategoryId())
				.orElseThrow(() -> new BusinessException("CAT_002",dao.getCategoryId().toString()));
		
		Platform platform = platformRepository.findById(dao.getPlatformId())
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
		return new ProjectDao(
	            saved.getId(),
	            saved.getProjectName(),
	            saved.getDescription(),
	            saved.getStatus(),
	            saved.getProjectOwner(),
	            saved.getProjectStartDate(),
	            saved.getProjectEndDate(),
	            category.getId(),
	            platform.getId(),
	            category.getCategoryName(),
	            platform.getPlatformName()
	    );
	}

	public Project updateProject(Long id, Project updatedProject) {
		if (id == null) {
			throw new BusinessException("PRJ_010");
		}

		Project existing = projectRepository.findById(id)
				.orElseThrow(() -> new BusinessException("PRJ_002",id.toString()));

		if (updatedProject.getProjectName() != null && !updatedProject.getProjectName().trim().isEmpty()) {
			existing.setProjectName(updatedProject.getProjectName());
		}

		if (updatedProject.getCategory() != null) {
			Long eqId = updatedProject.getCategory().getId();
			Category category = categoryRepository.findById(eqId)
					.orElseThrow(() -> new BusinessException("CAT_002",eqId.toString()));
			existing.setCategory(category);
		}
		
		if (updatedProject.getPlatform() != null) {
			Long eqId = updatedProject.getPlatform().getId();
			Platform platform = platformRepository.findById(eqId)
					.orElseThrow(() -> new BusinessException("PLT_003",eqId.toString()));
			existing.setPlatform(platform);
		}

		return projectRepository.save(existing);
	}

	public void deleteProject(Long id) {
		if (id == null) {
			throw new BusinessException("PRJ_004");
		}

		if (!projectRepository.existsById(id)) {
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

