package com.hackathon.learningmanagement.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hackathon.learningmanagement.dto.CourseEnrollmentDto;
import com.hackathon.learningmanagement.dto.TrainingHistoryDto;
import com.hackathon.learningmanagement.dto.UserRegistrationDto;
import com.hackathon.learningmanagement.entity.CourseDetails;
import com.hackathon.learningmanagement.entity.UserRegistration;
import com.hackathon.learningmanagement.exception.NotFoundException;
import com.hackathon.learningmanagement.service.LearningService;

@RestController
public class LearningController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LearningController.class);

	@Autowired
	LearningService learningService;

	@PostMapping("/register")
	public ResponseEntity<UserRegistrationDto> registerUser(@RequestBody UserRegistration userRegistration) {
		return new ResponseEntity<>(learningService.registerUser(userRegistration),HttpStatus.CREATED);
		
	}

	@GetMapping(value = "/login")
	public ResponseEntity<String> loginUser(@RequestParam Long userId, @RequestParam String password) {
		String loginUserResponse;
		try {
			loginUserResponse = learningService.loginUser(userId, password);
		} catch (NotFoundException e) {
			LOGGER.error("Exception caught while login : {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
		LOGGER.info("Login Successfull for the user  : {}",userId);
		return new ResponseEntity<>(loginUserResponse, HttpStatus.OK);
	}

	@GetMapping(value = "/courses/{userId}")
	public ResponseEntity getCourseDetails(@PathVariable("userId") Long userId,
			@RequestParam(value = "courseName", required = false) String courseName,
			@RequestParam(value = "categoryName", required = false) String categoryName) {

		List<CourseDetails> courseDetailsList = new ArrayList<>();
		try {
			courseDetailsList= learningService.getCourseDetails(userId, courseName, categoryName);
		} catch (NotFoundException e) {
			LOGGER.error("Exception caught while fetching course details : {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
		LOGGER.info("Course details found");
		return new ResponseEntity<>(courseDetailsList,HttpStatus.OK);
	}

	@PostMapping(value = "/enroll/{userId}")
	public ResponseEntity enrollCourse(@PathVariable("userId") Long userId,
			@RequestParam("courseId") Long courseId) {
		CourseEnrollmentDto dto = new CourseEnrollmentDto();
		try {
			dto = learningService.enrollCourse(userId, courseId);
		} catch (NotFoundException e) {
			LOGGER.error("Exception caught while enrolling for the course : {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
		LOGGER.info("Enrolled successfully");
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/history/{userId}")
	public ResponseEntity getTrainingHistory(@PathVariable("userId") Long userId) throws NotFoundException {

		List<TrainingHistoryDto> trainingHistoryDtoList = new ArrayList<>();
		try {
			trainingHistoryDtoList = learningService.getTrainingHistory(userId);
		} catch (NotFoundException e) {
			LOGGER.error("Exception caught while fetching the training history : {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("Fetched training history for the userId  : {}",userId);
		return new ResponseEntity<>(trainingHistoryDtoList, HttpStatus.OK);
	}
	
	
}