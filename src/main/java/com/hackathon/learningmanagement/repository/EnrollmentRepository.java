package com.hackathon.learningmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hackathon.learningmanagement.entity.EnrollmentDetails;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentDetails, Long> {

	@Query(value = "select * from enrollment_details e where e.user_id= :userId and e.status='ENROLLED'", nativeQuery = true)
	public List<EnrollmentDetails> getEnrollmentDetailsByUserIdAndStatus(Long userId);
	
	@Query(value = "select * from enrollment_details e where e.user_id= :userId and e.course_id= :courseId", nativeQuery = true)
	public EnrollmentDetails getEnrollmentByUserIdAndCourseId(Long userId,Long courseId);

}
