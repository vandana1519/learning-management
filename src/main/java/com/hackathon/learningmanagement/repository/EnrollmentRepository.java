package com.hackathon.learningmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.learningmanagement.entity.EnrollmentDetails;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentDetails, Long>{

}
