package com.timesheetapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheetapplication.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByCategoryId(Long categoryId);
    List<Activity> findByProjectId(Long projectId);
}
