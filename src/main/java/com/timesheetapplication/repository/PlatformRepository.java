package com.timesheetapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheetapplication.model.Platform;

public interface PlatformRepository extends JpaRepository<Platform, Long> {}
