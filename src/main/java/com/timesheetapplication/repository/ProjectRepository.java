package com.timesheetapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.timesheetapplication.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByPlatformId(Long platformId);
    List<Project> findByCategoryId(Long categoryId);
    List<Project> findByPlatformIdAndCategoryId(Long platformId, Long categoryId);
    
    @Query("SELECT p.projectName FROM Project p WHERE p.status = 'ACTIVE' ORDER BY p.projectName")
    List<String> findAllActiveProjectNames();

}
