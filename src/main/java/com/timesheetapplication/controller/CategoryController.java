package com.timesheetapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timesheetapplication.model.Category;
import com.timesheetapplication.service.CategoryService;

@RestController
@CrossOrigin
@RequestMapping("/api/timesheetapplication")
public class CategoryController {

	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/categorys")
	public List<Category> getAllCategorys() {
		return categoryService.getAllCategorys();
	}
	
	@GetMapping("/categorys/{id}")
	public Category getTeamById(@PathVariable Long id) {
		return categoryService.getCategoryById(id);
	}
	
	@PostMapping(("/categorys"))
    public Category addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }
	
	@PutMapping("/categorys/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category updatedCategory) {
        return categoryService.updateCategory(id, updatedCategory);
    }
	
	@DeleteMapping("/categorys/{id}")
	public void deleteCategory(@PathVariable Long id) {
		System.out.println("CategoryController.deleteCategory(id) "+id);
		categoryService.deleteCategory(id);
    }
}
