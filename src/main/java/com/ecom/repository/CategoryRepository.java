package com.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
	public boolean existsByName(String categoryName);
	public boolean existsByImageName(String imageName);
	public List<Category> findByIsActiveTrue();
}
