package com.timesheetapplication.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.timesheetapplication.dao.ActivityDao;
import com.timesheetapplication.exception.BusinessException;
import com.timesheetapplication.model.Activity;
import com.timesheetapplication.model.Category;
import com.timesheetapplication.model.Project;
import com.timesheetapplication.repository.ActivityRepository;
import com.timesheetapplication.repository.CategoryRepository;
import com.timesheetapplication.repository.ProjectRepository;

@Service
public class ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	public ActivityService(ActivityRepository activityRepository, CategoryRepository categoryRepository, ProjectRepository projectRepository) {
	    this.activityRepository = activityRepository;
	    this.categoryRepository = categoryRepository;
	    this.projectRepository = projectRepository;
	}
	
	public List<ActivityDao> getAllActivitys() {
		List<Activity> activitys = (List<Activity>) activityRepository.findAll();
		if (activitys.isEmpty()) {
			//throw new ResourceNotFoundException("No activitys found in the database");
			throw new BusinessException("ACT_001");
		}
		return activitys.stream().map(a -> new ActivityDao(a.getId(), a.getActivityName(), a.getCategory().getId(), a.getProject().getId()))
				.collect(Collectors.toList());
	}

	public Activity getActivityById(Long id) {
		if (id == null) {
			//throw new BadRequestException("Activity ID cannot be null");
			throw new BusinessException("ACT_002");
		}

		return activityRepository.findById(id)
				//.orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID: " + id));
				.orElseThrow(() -> new BusinessException("ACT_003",id.toString()));
	}

	public ActivityDao addActivity(ActivityDao dao) {
		if (dao == null) {
			//throw new BadRequestException("Activity data cannot be null");
			throw new BusinessException("ACT_004");
		}
		if (dao.getActivityName() == null || dao.getActivityName().trim().isEmpty()) {
			//throw new BadRequestException("Activity name is required");
			throw new BusinessException("ACT_005");
		}
		if (dao.getCategoryId() == null) {
			//throw new BadRequestException("Category ID is required for creating a Activity");
			throw new BusinessException("CAT_003");
		}
		if (dao.getProjectId() == null) {
			//throw new BadRequestException("Project ID is required for creating a Activity");
			throw new BusinessException("PRJ_003");
		}

		Category category = categoryRepository.findById(dao.getCategoryId())
				//.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + dao.getCategoryId()));
				.orElseThrow(() -> new BusinessException("CAT_002",dao.getCategoryId().toString()));
		Project project = projectRepository.findById(dao.getProjectId())
				//.orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + dao.getProjectId()));
				.orElseThrow(() -> new BusinessException("PRJ_002",dao.getProjectId().toString()));

		Activity activity = new Activity();
		activity.setActivityName(dao.getActivityName());
		activity.setCategory(category);
		activity.setProject(project);

		Activity saved = activityRepository.save(activity);
		return new ActivityDao(saved.getId(), saved.getActivityName(), category.getId(),project.getId());
	}

	public Activity updateActivity(Long id, Activity updatedActivity) {
		if (id == null) {
			//throw new BadRequestException("Activity ID is required for update");
			throw new BusinessException("ACT_002");
		}

		Activity existing = activityRepository.findById(id)
				//.orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID: " + id));
				.orElseThrow(() -> new BusinessException("ACT_003",id.toString()));

		if (updatedActivity.getActivityName() != null && !updatedActivity.getActivityName().trim().isEmpty()) {
			existing.setActivityName(updatedActivity.getActivityName());
		}

		if (updatedActivity.getCategory() != null) {
			Long eqId = updatedActivity.getCategory().getId();
			Category category = categoryRepository.findById(eqId)
					//.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + eqId));
					.orElseThrow(() -> new BusinessException("CAT_002",eqId.toString()));
			existing.setCategory(category);
		}
		
		if (updatedActivity.getProject() != null) {
			Long eqId = updatedActivity.getProject().getId();
			Project project = projectRepository.findById(eqId)
					//.orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + eqId));
					.orElseThrow(() -> new BusinessException("PRJ_002",eqId.toString()));
			existing.setProject(project);
		}

		return activityRepository.save(existing);
	}

	public void deleteActivity(Long id) {
		if (id == null) {
			//throw new BadRequestException("Activity ID cannot be null");
			throw new BusinessException("ACT_002");
		}

		if (!activityRepository.existsById(id)) {
			//throw new ResourceNotFoundException("Activity not found with ID: " + id);
			throw new BusinessException("ACT_003",id);
		}

		activityRepository.deleteById(id);
	}

	public List<Activity> getActivitiesByCategory(Long categoryId) {
		return activityRepository.findByCategoryId(categoryId);
	}

	public List<Activity> getActivitiesByProject(Long projectId) {
		return activityRepository.findByProjectId(projectId);
	}
}
