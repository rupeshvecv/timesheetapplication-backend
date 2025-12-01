package com.timesheetapplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.timesheetapplication.exception.BadRequestException;
import com.timesheetapplication.exception.ResourceNotFoundException;
import com.timesheetapplication.model.Platform;
import com.timesheetapplication.repository.PlatformRepository;

@Service
public class PlatformService {

	private PlatformRepository platformRepository;

	public PlatformService(PlatformRepository platformRepository) {
		this.platformRepository = platformRepository;
	}

	public List<Platform> getAllPlatforms() {
		List<Platform> platforms = (List<Platform>) platformRepository.findAll();
		if (platforms.isEmpty()) {
			throw new ResourceNotFoundException("No Platforms found in the system");
		}
		return platforms;
	}

	public Platform getPlatformById(Long id) {
		if (id == null) {
			throw new BadRequestException("Platform ID must not be null");
		}
		return platformRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Platform not found with ID: " + id));
	}

	public Platform addPlatform(Platform platform) {
		if (platform == null) {
			throw new BadRequestException("Platform data cannot be null");
		}
		if (platform.getPlatformName() == null || platform.getPlatformName().trim().isEmpty()) {
			throw new BadRequestException("Platform name is required");
		}
		return platformRepository.save(platform);
	}

	public Platform updatePlatform(Long id, Platform updatedPlatform) {
		if (id == null) {
			throw new BadRequestException("Platform ID is required for update");
		}
		if (updatedPlatform == null) {
			throw new BadRequestException("Updated Platform data cannot be null");
		}
		Platform existing = platformRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Platform not found with ID: " + id));

		if (updatedPlatform.getPlatformName() != null && !updatedPlatform.getPlatformName().trim().isEmpty()) {
			existing.setPlatformName(updatedPlatform.getPlatformName());
		} else {
			throw new BadRequestException("Platform name cannot be empty");
		}

		return platformRepository.save(existing);
	}

	public void deletePlatform(Long id) {
		if (id == null) {
			throw new BadRequestException("Platform ID is required for deletion");
		}

		if (!platformRepository.existsById(id)) {
			throw new ResourceNotFoundException("Platform not found with ID: " + id);
		}
		platformRepository.deleteById(id);
	}
}
