package com.ecom.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.ecom.model.Category;
import com.ecom.repository.CategoryRepository;
import com.ecom.service.CategoryService;

import jakarta.servlet.http.HttpSession;


@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepo;
	
	
	CategoryServiceImpl(@Autowired CategoryRepository categoryRepo){
		this.categoryRepo=categoryRepo;
	}
	
	@Override
	public Category saveCategory(Category category) {
//		Category tempCategory=null;
//		if(category!=null && !categoryRepo.existsByName(category.getName())) {
//			tempCategory=categoryRepo.save(category);
//		}
//		System.out.println("chl gaya syd");
		// TODO Auto-generated method stub
		return categoryRepo.save(category);
	}

	@Override
	public List<Category> getAllCategory() {
			List<Category> listOfCategory=categoryRepo.findAll();
			return listOfCategory;
	}

	@Override
	public boolean existCategory(Category category){
		return categoryRepo.existsByName(category.getName())||categoryRepo.existsByImageName(category.getImageName());

	}

	public boolean deleteCategory(int categoryId) {
		if (categoryRepo.existsById(categoryId)){
			categoryRepo.deleteById(categoryId);
			return true;
		}
		return false;
	}

	@Override
	public boolean updateCategory(Category category) {
		categoryRepo.save(category);
		return false;
	}

	@Override
	public Category getCategoryById(int id) {

		Optional<Category> o =categoryRepo.findById(id);

		return o.get() ;
	}

	@Override
	public List<Category> getAllActiveCategory() {
		List<Category> categoryList= categoryRepo.findByIsActiveTrue();
		return categoryList;
	}

}
