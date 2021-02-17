package com.hackathon.learningmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.learningmanagement.entity.CourseDetails;

@Repository
public interface CourseRepository extends JpaRepository<CourseDetails, Long>{

}
