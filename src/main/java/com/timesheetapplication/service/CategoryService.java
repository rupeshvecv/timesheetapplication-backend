package com.timesheetapplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.timesheetapplication.exception.BusinessException;
import com.timesheetapplication.model.Category;
import com.timesheetapplication.repository.CategoryRepository;

@Service
public class CategoryService {

	private CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<Category> getAllCategorys() {
		List<Category> categorys = (List<Category>) categoryRepository.findAll();
		if (categorys.isEmpty()) {
			//throw new ResourceNotFoundException("No Categorys found in the system");
			throw new BusinessException("CAT_004");
		}
		return categorys;
	}

	public Category getCategoryById(Long id) {
		if (id == null) {
			//throw new BadRequestException("Category ID must not be null");
			throw new BusinessException("CAT_005");
		}
		return categoryRepository.findById(id)
				//.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
				.orElseThrow(() -> new BusinessException("CAT_002",id.toString()));
	}

	public Category addCategory(Category category) {
		if (category == null) {
			//throw new BadRequestException("Category data cannot be null");
			throw new BusinessException("CAT_006");
		}
		if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
			//throw new BadRequestException("Category name is required");
			throw new BusinessException("CAT_007");
		}
		return categoryRepository.save(category);
	}

	public Category updateCategory(Long id, Category updatedCategory) {
		if (id == null) {
			//throw new BadRequestException("Category ID is required for update");
			throw new BusinessException("CAT_009");
		}
		if (updatedCategory == null) {
			//throw new BadRequestException("Updated Category data cannot be null");
			throw new BusinessException("CAT_006");
		}
		Category existing = categoryRepository.findById(id)
				//.orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
				.orElseThrow(() -> new BusinessException("CAT_002",id.toString()));

		if (updatedCategory.getCategoryName() != null && !updatedCategory.getCategoryName().trim().isEmpty()) {
			existing.setCategoryName(updatedCategory.getCategoryName());
		} else {
			//throw new BadRequestException("Category name cannot be empty");
			throw new BusinessException("CAT_007");
		}

		return categoryRepository.save(existing);
	}

	public void deleteCategory(Long id) {
		if (id == null) {
			//throw new BadRequestException("Category ID is required for deletion");
			throw new BusinessException("CAT_008");
		}

		if (!categoryRepository.existsById(id)) {
			//throw new ResourceNotFoundException("Category not found with ID: " + id);
			throw new BusinessException("CAT_002",id.toString());
		}
		categoryRepository.deleteById(id);
	}
}
