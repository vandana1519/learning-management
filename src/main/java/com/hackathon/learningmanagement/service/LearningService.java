
package com.hackathon.learningmanagement.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;


import com.hackathon.learningmanagement.dto.CourseEnrollmentDto;

import com.hackathon.learningmanagement.dto.TrainingHistoryDto;

import com.hackathon.learningmanagement.dto.UserRegistrationDto;
import com.hackathon.learningmanagement.entity.CourseDetails;
import com.hackathon.learningmanagement.entity.UserRegistration;
import com.hackathon.learningmanagement.exception.NotFoundException;

public interface LearningService {

	public UserRegistrationDto registerUser(UserRegistration userRegistration);

	public String loginUser(Long userId, String password) throws NotFoundException;
	
	public List<CourseDetails> getCourseDetails(Long userId, String courseName, String categoryName) throws NotFoundException;

	public CourseEnrollmentDto enrollCourse(Long userId, Long courseId) throws NotFoundException;

	public List<TrainingHistoryDto>  getTrainingHistory(Long userId) throws NotFoundException;

}

