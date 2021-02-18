package com.hackathon.learningmanagement.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.learningmanagement.constants.AppConstants;
import com.hackathon.learningmanagement.dto.CourseEnrollmentDto;
import com.hackathon.learningmanagement.dto.TrainingHistoryDto;
import com.hackathon.learningmanagement.dto.UserRegistrationDto;
import com.hackathon.learningmanagement.entity.Category;
import com.hackathon.learningmanagement.entity.CourseDetails;
import com.hackathon.learningmanagement.entity.EnrollmentDetails;
import com.hackathon.learningmanagement.entity.UserRegistration;
import com.hackathon.learningmanagement.repository.CategoryRepository;
import com.hackathon.learningmanagement.repository.CourseRepository;
import com.hackathon.learningmanagement.repository.EnrollmentRepository;
import com.hackathon.learningmanagement.repository.UserRepository;

public class LearningServiceImplTest {
	
	@InjectMocks
	LearningServiceImpl learningServiceImpl;
	
	@Mock
	UserRepository userRepository;

	@Mock
	CourseRepository courseRepository;

	@Mock
	CategoryRepository categoryRepository;
	
	@Mock
	EnrollmentRepository enrollmentRepository;
	
	static UserRegistration user = new UserRegistration();
	static UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
	static Category category = new Category();
	static CourseDetails courseDetails = new CourseDetails();
	static List<CourseDetails> courseDetailsList = new ArrayList<>();
	static EnrollmentDetails enrollmentDetails = new EnrollmentDetails();
	static CourseEnrollmentDto courseEnrollmentDto = new CourseEnrollmentDto();
	static TrainingHistoryDto trainingHistoryDto = new TrainingHistoryDto();
	static List<TrainingHistoryDto> trainingHistoryDtoList = new ArrayList<>();

	@BeforeAll
	public static void setup() {

		user.setUserName("Abc");
		user.setPassword("12345");

		userRegistrationDto.setUserId(1L);

		courseDetails.setCourseId(1L);
		courseDetails.setCourseName("Java_SpringBoot");
		courseDetails.setStartDate(LocalDate.now());
		courseDetails.setEndDate(LocalDate.now());
		courseDetails.setCategory(category);
		courseDetailsList.add(courseDetails);

		category.setCategoryId(1L);
		category.setCategoryName("Technical");

		enrollmentDetails.setEnrollmentId(1L);
		enrollmentDetails.setUserRegistration(user);
		enrollmentDetails.setCourseDetails(courseDetails);
		enrollmentDetails.setStatus(AppConstants.COMPLETED);
		
		trainingHistoryDto.setCourseId(1L);
		trainingHistoryDto.setEnrollmentId(1L);
		trainingHistoryDtoList.add(trainingHistoryDto);

	}

}
