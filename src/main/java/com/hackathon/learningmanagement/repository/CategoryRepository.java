package com.hackathon.learningmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.learningmanagement.entity.Category;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long>{

	public List<Category> findByCategoryNameContainingIgnoreCase(String categoryName);
	
	public List<Long> findCategoryIdByCategoryNameContainingIgnoreCase(String categoryName);

}
