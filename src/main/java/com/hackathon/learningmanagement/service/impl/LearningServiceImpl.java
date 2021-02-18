package com.hackathon.learningmanagement.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackathon.learningmanagement.constants.AppConstants;
import com.hackathon.learningmanagement.dto.CourseEnrollmentDto;
import com.hackathon.learningmanagement.dto.TrainingHistoryDto;
import com.hackathon.learningmanagement.dto.UserRegistrationDto;
import com.hackathon.learningmanagement.entity.Category;
import com.hackathon.learningmanagement.entity.CourseDetails;
import com.hackathon.learningmanagement.entity.EnrollmentDetails;
import com.hackathon.learningmanagement.entity.UserRegistration;
import com.hackathon.learningmanagement.exception.NotFoundException;
import com.hackathon.learningmanagement.repository.CategoryRepository;
import com.hackathon.learningmanagement.repository.CourseRepository;
import com.hackathon.learningmanagement.repository.EnrollmentRepository;
import com.hackathon.learningmanagement.repository.UserRepository;
import com.hackathon.learningmanagement.service.LearningService;

@Service
public class LearningServiceImpl implements LearningService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LearningServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Override
	public UserRegistrationDto registerUser(UserRegistration userRegistration) {
		UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
		UserRegistration registerResponse = userRepository.save(userRegistration);
		userRegistrationDto.setUserId(registerResponse.getUserId());
		LOGGER.info("User registered successfully with userId : {}", registerResponse.getUserId());
		return userRegistrationDto;
	}

	@Override
	public String loginUser(Long userId, String password) throws NotFoundException {
		UserRegistration userRegistration = userRepository.findByUserIdAndPassword(userId, password);
		UserRegistration user = userRepository.findByUserId(userId);
		if (user == null) {
			LOGGER.error("User not found for userId : {}",userId);			
			throw new NotFoundException("User not found,please register!!!");
		}
		if (userRegistration == null) {
			LOGGER.error("User id and password mismatch");	
			throw new NotFoundException("User id and password mismatch!!!");
		}
		LOGGER.info("Login Successful for userId : {}, userId");
		return "Login Successfull!!";
	}

	@Override
	public List getCourseDetails(Long userId, String courseName, String categoryName) throws NotFoundException {

		List<CourseDetails> courseDetailsList = new ArrayList<>();

		if (userRepository.findByUserId(userId) == null) {
			LOGGER.error("User not found for userId : {}", userId);
			throw new NotFoundException("User not found, Please register!!!");
		}
		if (categoryName == null) {
			LOGGER.info("Category name not found in the request");
			return courseRepository.findByCourseNameContainingIgnoreCase(courseName);
		} else {

			List<Category> categoryList = categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);

			for (Category category : categoryList) {
				List<CourseDetails> courseDetail = courseRepository
						.getCourseDetailsByCategoryId(category.getCategoryId());
				if (!courseDetail.isEmpty()) {
					for (CourseDetails course : courseDetail) {
						course.setCategory(new Category(category.getCategoryId(), category.getCategoryName()));
					}
					courseDetailsList.addAll(courseDetail);
				}

			}
			LOGGER.info("Returning course details list");
			return courseDetailsList;
		}
	}

	@Override

	public CourseEnrollmentDto enrollCourse(Long userId, Long courseId) throws NotFoundException {
		EnrollmentDetails enrollmentDetails = new EnrollmentDetails();
		CourseEnrollmentDto enrollmentDto = new CourseEnrollmentDto();
		CourseDetails courseDetails = new CourseDetails();
		CourseDetails enrolledCourseDetails = new CourseDetails();
		UserRegistration userRegistration = new UserRegistration();
		userRegistration = userRepository.findByUserId(userId);
		List<EnrollmentDetails> enrollmentDetailsList = new ArrayList<>();

		if (userRegistration == null) {
			LOGGER.error("User not found for userId : {}",userId);
			throw new NotFoundException("User not found, Please register!!!");
		}

		if (courseRepository.findByCourseId(courseId) == null) {
			LOGGER.error("Course not found for courseId : {}",courseId);
			throw new NotFoundException("Course not found!!!");
		}

		if (enrollmentRepository.getEnrollmentByUserIdAndCourseId(userId, courseId) != null) {
			LOGGER.error("User already enrolled for this course earlier!!");
			throw new NotFoundException("User already enrolled for this course earlier!!");
		}
		courseDetails = courseRepository.findByCourseId(courseId);
		Long days = ChronoUnit.DAYS.between(courseDetails.getStartDate(), courseDetails.getEndDate());
		Long startdays = ChronoUnit.DAYS.between(LocalDate.now(), courseDetails.getStartDate());
		if (startdays < 2) {
			LOGGER.error("Cannot enroll as duration must be greater than 2 days!!");
			throw new NotFoundException("Cannot enroll as duration must be greater than 2 days!!");
		}

		enrollmentDetailsList = enrollmentRepository.getEnrollmentDetailsByUserIdAndStatus(userId);
		for (EnrollmentDetails enroll : enrollmentDetailsList) {
			Long enrolledCourseId = enroll.getCourseDetails().getCourseId();
			enrolledCourseDetails = courseRepository.findByCourseId(enrolledCourseId);
			LocalDate enrolledStartDate = enrolledCourseDetails.getStartDate();
			LocalDate enrolledEndDate = enrolledCourseDetails.getEndDate();
			if (enrolledStartDate == courseDetails.getStartDate()
					|| courseDetails.getStartDate().isAfter(enrolledStartDate)
							&& courseDetails.getEndDate().isBefore(enrolledEndDate)) {

				LOGGER.error("Already enrolled for other course for this duration!!!");
				throw new NotFoundException("Already enrolled for other course for this duration!!");
			}

		}

		if (days > 2 && days < 30)

		{
			enrollmentDetails.setCourseDetails(courseDetails);
			enrollmentDetails.setUserRegistration(userRegistration);
			enrollmentDetails.setStatus(AppConstants.ENROLLED);
			enrollmentDetails.setStatus(AppConstants.COMPLETED);
			enrollmentDetails = enrollmentRepository.save(enrollmentDetails);
			enrollmentDto.setEnrollmentId(enrollmentDetails.getEnrollmentId());
		} else {
			LOGGER.error("Cannot enroll as duration must be less than 30 days!!");
			throw new NotFoundException("Cannot enroll as duration must be less than 30 days!!");
		}

		return enrollmentDto;
	}

	public List<TrainingHistoryDto> getTrainingHistory(Long userId) throws NotFoundException {

		List<TrainingHistoryDto> trainingHistoryDtoList = new ArrayList<>();
		List<EnrollmentDetails> enrollmentList = enrollmentRepository.getTrainingHistoryByUserId(userId);

		if (!enrollmentList.isEmpty()) {
			LOGGER.info("Fetched enrollment list for the userId : {}", userId);
			for (EnrollmentDetails enrollment : enrollmentList) {
				TrainingHistoryDto trainingHistoryDto = new TrainingHistoryDto();
				trainingHistoryDto.setCourseId(enrollment.getCourseDetails().getCourseId());
				trainingHistoryDto.setEnrollmentId(enrollment.getEnrollmentId());
				trainingHistoryDtoList.add(trainingHistoryDto);
			}
		} else {
			LOGGER.error("Training history not found for userId : {}", userId);
			throw new NotFoundException("Training history not found!!");
		}
		return trainingHistoryDtoList;
	}
}