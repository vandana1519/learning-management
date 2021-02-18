
package com.hackathon.learningmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hackathon.learningmanagement.entity.CourseDetails;

@Repository
public interface CourseRepository extends JpaRepository<CourseDetails, Long>{
	
	public List<CourseDetails> findByCourseNameContainingIgnoreCase(String courseName);
	
	@Query(value = "select * from course_details where category_id = :categoryId", nativeQuery = true)
	public List<CourseDetails> getCourseDetailsByCategoryId(Long categoryId);
	
	@Query(value = "select * from course_details where category_id = :categoryId and course_name like :courseName", nativeQuery = true)
	public List<CourseDetails> getCourseDetailsByCategoryIdAndCourseName(String courseName, Long categoryId);
	
	public CourseDetails findByCourseId(Long courseId);

}


