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
		List<Activity> activities = (List<Activity>) activityRepository.findAll();
		if (activities.isEmpty()) {
			throw new BusinessException("ACT_001");
		}
		return activities.stream()
	            .map(a -> new ActivityDao(
	                    a.getId(),
	                    a.getActivityName(),

	                    a.getCategory().getId(),
	                    a.getCategory().getCategoryName(),

	                    a.getProject().getId(),
	                    a.getProject().getProjectName()
	            ))
	            .collect(Collectors.toList());
	}

	public Activity getActivityById(Long id) {
		if (id == null) {
			throw new BusinessException("ACT_002");
		}

		return activityRepository.findById(id)
				.orElseThrow(() -> new BusinessException("ACT_003",id.toString()));
	}

	public ActivityDao addActivity(ActivityDao dao) {
		if (dao == null) {
			throw new BusinessException("ACT_004");
		}
		if (dao.getActivityName() == null || dao.getActivityName().trim().isEmpty()) {
			throw new BusinessException("ACT_005");
		}
		if (dao.getCategoryId() == null) {
			throw new BusinessException("CAT_003");
		}
		if (dao.getProjectId() == null) {
			throw new BusinessException("PRJ_003");
		}

		Category category = categoryRepository.findById(dao.getCategoryId())
				.orElseThrow(() -> new BusinessException("CAT_002",dao.getCategoryId().toString()));
		Project project = projectRepository.findById(dao.getProjectId())
				.orElseThrow(() -> new BusinessException("PRJ_002",dao.getProjectId().toString()));

		Activity activity = new Activity();
		activity.setActivityName(dao.getActivityName());
		activity.setCategory(category);
		activity.setProject(project);

		Activity saved = activityRepository.save(activity);
		return new ActivityDao(saved.getId(), saved.getActivityName(), category.getId(), category.getCategoryName(),project.getId(),project.getProjectName());
	}

	public ActivityDao updateActivity(Long id, ActivityDao updatedActivity) {

	    if (id == null) {
	        throw new BusinessException("ACT_002");
	    }

	    Activity existing = activityRepository.findById(id)
	            .orElseThrow(() -> new BusinessException("ACT_003", id.toString()));

	    //Update Activity Name
	    if (updatedActivity.getActivityName() != null &&
	        !updatedActivity.getActivityName().trim().isEmpty()) {

	        existing.setActivityName(updatedActivity.getActivityName());
	    }

	    //Update Category
	    if (updatedActivity.getCategoryId() != null) {
	        Category category = categoryRepository.findById(updatedActivity.getCategoryId())
	                .orElseThrow(() -> new BusinessException(
	                        "CAT_002",
	                        updatedActivity.getCategoryId().toString()
	                ));
	        existing.setCategory(category);
	    }

	    //Update Project
	    if (updatedActivity.getProjectId() != null) {
	        Project project = projectRepository.findById(updatedActivity.getProjectId())
	                .orElseThrow(() -> new BusinessException(
	                        "PRJ_002",
	                        updatedActivity.getProjectId().toString()
	                ));
	        existing.setProject(project);
	    }

	    Activity saved = activityRepository.save(existing);

	    //Convert Entity → DAO (RETURN TYPE)
	    return new ActivityDao(
	            saved.getId(),
	            saved.getActivityName(),
	            saved.getCategory().getId(),
	            saved.getProject().getId()
	    );
	}

	public void deleteActivity(Long id) {
		if (id == null) {
			throw new BusinessException("ACT_002");
		}

		if (!activityRepository.existsById(id)) {
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
