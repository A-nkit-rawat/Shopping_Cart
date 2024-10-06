package com.ecom.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.model.Category;

@Service
public interface CategoryService {
	public Category saveCategory(Category category);
	public List<Category> getAllCategory();
	public boolean existCategory(Category category);
	public boolean deleteCategory(int categoryId);
	public boolean updateCategory(Category category);
	public Category getCategoryById(int id);
	public List<Category> getAllActiveCategory();
}
