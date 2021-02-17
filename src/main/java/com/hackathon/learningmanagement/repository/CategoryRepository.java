package com.hackathon.learningmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.learningmanagement.entity.Category;

@Repository
public interface CategoryRepository  extends JpaRepository<Category, Long>{

}
