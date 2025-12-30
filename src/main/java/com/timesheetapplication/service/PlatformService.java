package com.timesheetapplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.timesheetapplication.exception.BusinessException;
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
			throw new BusinessException("PLT_001");
		}
		return platforms;
	}

	public Platform getPlatformById(Long id) {
		if (id == null) {
			throw new BusinessException("PLT_002");
		}
		return platformRepository.findById(id)
				.orElseThrow(() -> new BusinessException("PLT_003",id.toString()));
	}

	public Platform addPlatform(Platform platform) {
		if (platform == null) {
			throw new BusinessException("PLT_004");
		}
		if (platform.getPlatformName() == null || platform.getPlatformName().trim().isEmpty()) {
			throw new BusinessException("PLT_005");
		}
		return platformRepository.save(platform);
	}

	public Platform updatePlatform(Long id, Platform updatedPlatform) {
		if (id == null) {
			throw new BusinessException("PLT_007");
		}
		if (updatedPlatform == null) {
			throw new BusinessException("PLT_004");
		}
		Platform existing = platformRepository.findById(id)
				.orElseThrow(() -> new BusinessException("PLT_003",id.toString()));

		if (updatedPlatform.getPlatformName() != null && !updatedPlatform.getPlatformName().trim().isEmpty()) {
			existing.setPlatformName(updatedPlatform.getPlatformName());
		} else {
			throw new BusinessException("PLT_005");
		}

		return platformRepository.save(existing);
	}

	public void deletePlatform(Long id) {
		if (id == null) {
			throw new BusinessException("PLT_006");
		}

		if (!platformRepository.existsById(id)) {
			throw new BusinessException("PLT_003", id.toString());
		}
		platformRepository.deleteById(id);
	}
}
