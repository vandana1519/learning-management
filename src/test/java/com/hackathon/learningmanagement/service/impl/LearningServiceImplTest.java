package com.hackathon.learningmanagement.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

@SpringBootTest
class LearningServiceImplTest {
	
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
	static List<Category> categoryList = new ArrayList<>();
	static CourseDetails courseDetails = new CourseDetails();
	static CourseDetails enrolledcourseDetails = new CourseDetails();
	static CourseDetails enrolledcourseDetails1 = new CourseDetails();
	static CourseDetails enrolledcourseDetails2 = new CourseDetails();
	static List<CourseDetails> courseDetailsList = new ArrayList<>();
	static EnrollmentDetails enrollmentDetails = new EnrollmentDetails();
	static List<EnrollmentDetails> enrollmentDetailsList = new ArrayList<>();
	static CourseEnrollmentDto courseEnrollmentDto = new CourseEnrollmentDto();
	static TrainingHistoryDto trainingHistoryDto = new TrainingHistoryDto();
	static List<TrainingHistoryDto> trainingHistoryDtoList = new ArrayList<>();

	@BeforeAll
	public static void setup() {

		user.setUserId(1L);
		user.setUserName("Abc");
		user.setPassword("12345");

		userRegistrationDto.setUserId(1L);

		courseDetails.setCourseId(1L);
		courseDetails.setCourseName("Java_SpringBoot");
		courseDetails.setStartDate(LocalDate.now().plusDays(3));
		courseDetails.setEndDate(LocalDate.now().plusDays(10));
		courseDetails.setCategory(category);
		courseDetailsList.add(courseDetails);
		
		enrolledcourseDetails.setCourseId(2L);
		enrolledcourseDetails.setCourseName("Java_Spring");
		enrolledcourseDetails.setStartDate(LocalDate.now().plusDays(11));
		enrolledcourseDetails.setEndDate(LocalDate.now().plusDays(20));
		enrolledcourseDetails.setCategory(category);
		
		enrolledcourseDetails1.setCourseId(2L);
		enrolledcourseDetails1.setCourseName("Java_Spring");
		enrolledcourseDetails1.setStartDate(LocalDate.now().plusDays(1));
		enrolledcourseDetails1.setEndDate(LocalDate.now().plusDays(20));
		enrolledcourseDetails1.setCategory(category);
		
		enrolledcourseDetails2.setCourseId(3L);
		enrolledcourseDetails2.setCourseName("Java_Spring");
		enrolledcourseDetails2.setStartDate(LocalDate.now().plusDays(3));
		enrolledcourseDetails2.setEndDate(LocalDate.now().plusDays(35));
		enrolledcourseDetails2.setCategory(category);

		category.setCategoryId(1L);
		category.setCategoryName("Technical");
		
		categoryList.add(category);

		enrollmentDetails.setEnrollmentId(1L);
		enrollmentDetails.setUserRegistration(user);
		enrollmentDetails.setCourseDetails(courseDetails);
		enrollmentDetails.setStatus(AppConstants.COMPLETED);
		enrollmentDetailsList.add(enrollmentDetails);
		
		trainingHistoryDto.setCourseId(1L);
		trainingHistoryDto.setEnrollmentId(1L);
		trainingHistoryDtoList.add(trainingHistoryDto);
		
		courseEnrollmentDto.setEnrollmentId(2L);

	}
	
	@Test
	void registerUserTestSuccess(){
		
		Mockito.when(userRepository.save(user)).thenReturn(user);
		UserRegistrationDto userRegistrationDto1 = learningServiceImpl.registerUser(user);
		assertNotNull(userRegistrationDto1);
		
	}
	
	@Test
	void loginUserTestSuccess() throws NotFoundException {
		
		Mockito.when(userRepository.findByUserIdAndPassword(Mockito.anyLong(),Mockito.anyString())).thenReturn(user);
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);
		
		String loginUserResponse = learningServiceImpl.loginUser(1L, "12345");
		Assertions.assertEquals("Login Successfull!!", loginUserResponse);
	}
	
	@Test
	@ExceptionHandler(NotFoundException.class)
	void loginUserTestMismatchException() throws NotFoundException {
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);	
		Mockito.when(userRepository.findByUserIdAndPassword(Mockito.anyLong(),Mockito.anyString())).thenReturn(null);
		
		Assertions.assertThrows(NotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				String loginUserResponse = learningServiceImpl.loginUser(1L, "123456");
				Assertions.assertEquals("User id and password mismatch!!!", loginUserResponse);
			}
		});
	}
	
	@Test
	@ExceptionHandler(NotFoundException.class)
	void loginUserTestUserNull() throws NotFoundException {
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(null);
		
		Assertions.assertThrows(NotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				String loginUserResponse = learningServiceImpl.loginUser(2L, "12345");
				Assertions.assertEquals("User not found,please register!!!", loginUserResponse);
			}
		});
	}
	
	@Test
	void getCourseDetailsTestCategoryNull() throws NotFoundException {
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);
		Mockito.when(courseRepository.findByCourseNameContainingIgnoreCase(Mockito.anyString())).thenReturn(courseDetailsList);	
		
		List<CourseDetails> courseDetailsList1 = learningServiceImpl.getCourseDetails(1L, "Java", null);
		Assertions.assertNotNull(courseDetailsList1);
	}
	
	
	@Test
	@ExceptionHandler(NotFoundException.class)
	void getCourseDetailsTestUserNull() throws NotFoundException {
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(null);
		
		Assertions.assertThrows(NotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				List<CourseDetails> courseDetailsList1 = learningServiceImpl.getCourseDetails(2L, "Java", "Technical");
				Assertions.assertNull(courseDetailsList1);
			}
		});
	}
	
	@Test
	void getCourseDetailsTestSuccess() throws NotFoundException {
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);
		Mockito.when(categoryRepository.findByCategoryNameContainingIgnoreCase(Mockito.anyString())).thenReturn(categoryList);	
		Mockito.when(courseRepository.getCourseDetailsByCategoryId(Mockito.anyLong())).thenReturn(courseDetailsList);
		
		List<CourseDetails> courseDetailsList1 = learningServiceImpl.getCourseDetails(1L, "Java", "Technical");
		Assertions.assertNotNull(courseDetailsList1);
	}
	
	@Test
	void getTrainingHistoryTestSuccess() throws NotFoundException {
		
		Mockito.when(enrollmentRepository.getTrainingHistoryByUserId(Mockito.anyLong())).thenReturn(enrollmentDetailsList);
		
		List<TrainingHistoryDto> trainingHistoryDtoList1 = learningServiceImpl.getTrainingHistory(1L);
		Assertions.assertNotNull(trainingHistoryDtoList1);
	}
	
	@Test
	@ExceptionHandler(NotFoundException.class)
	void getTrainingHistoryTestException() throws NotFoundException {
		
		Mockito.when(enrollmentRepository.getTrainingHistoryByUserId(Mockito.anyLong())).thenReturn(Collections.emptyList());
		
		Assertions.assertThrows(NotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				List<TrainingHistoryDto> trainingHistoryDtoList1 = learningServiceImpl.getTrainingHistory(1L);
				Assertions.assertNull(trainingHistoryDtoList1);
			}
		});
	}
	
	@Test
	@ExceptionHandler(NotFoundException.class)
	void enrollCourseTestUserNull() throws NotFoundException {
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(null);
		
		Assertions.assertThrows(NotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				
				CourseEnrollmentDto courseEnrollmentDto = learningServiceImpl.enrollCourse(2L,1L);
				Assertions.assertNull(courseEnrollmentDto);
			}
		});
	}
	
	@Test
	@ExceptionHandler(NotFoundException.class)
	void enrollCourseTestCourseNull() throws NotFoundException {
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);
		Mockito.when(courseRepository.findByCourseId(Mockito.anyLong())).thenReturn(null);
		
		Assertions.assertThrows(NotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				
				CourseEnrollmentDto courseEnrollmentDto = learningServiceImpl.enrollCourse(2L,1L);
				Assertions.assertNull(courseEnrollmentDto);
			}
		});
	}
	
	@Test
	@ExceptionHandler(NotFoundException.class)
	void enrollCourseTestAlreadyEnrolled() throws NotFoundException {
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);
		Mockito.when(courseRepository.findByCourseId(Mockito.anyLong())).thenReturn(courseDetails);
		Mockito.when(enrollmentRepository.getEnrollmentByUserIdAndCourseId(Mockito.anyLong(),Mockito.anyLong())).thenReturn(enrollmentDetails);
		
		Assertions.assertThrows(NotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				
				CourseEnrollmentDto courseEnrollmentDto = learningServiceImpl.enrollCourse(2L,1L);
				Assertions.assertNull(courseEnrollmentDto);
			}
		});
	}
	
	@Test
	@ExceptionHandler(NotFoundException.class)
	void enrollCourseTestAlreadyEnrolledException() throws NotFoundException {

		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);
		Mockito.when(courseRepository.findByCourseId(Mockito.anyLong())).thenReturn(courseDetails);
		Mockito.when(enrollmentRepository.getEnrollmentByUserIdAndCourseId(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(null);
		Mockito.when(courseRepository.getEnrolledCourseId(Mockito.anyLong())).thenReturn(enrolledcourseDetails);
		Mockito.when(enrollmentRepository.getEnrollmentDetailsByUserIdAndStatus(Mockito.anyLong()))
				.thenReturn(enrollmentDetailsList);
		Mockito.when(courseRepository.findByCourseId(Mockito.anyLong())).thenReturn(enrolledcourseDetails);

		Assertions.assertThrows(NotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {

				CourseEnrollmentDto courseEnrollmentDto = learningServiceImpl.enrollCourse(2L, 1L);
				Assertions.assertNotNull(courseEnrollmentDto);
			}
		});

	}
	
	@Test
	@ExceptionHandler(NullPointerException.class)
	void enrollCourseTestNotEnrolled() throws NotFoundException {

		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);
		Mockito.when(courseRepository.findByCourseId(Mockito.anyLong())).thenReturn(courseDetails);
		Mockito.when(enrollmentRepository.getEnrollmentByUserIdAndCourseId(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(null);
		Mockito.when(enrollmentRepository.getEnrollmentDetailsByUserIdAndStatus(Mockito.anyLong()))
				.thenReturn(enrollmentDetailsList);
		Mockito.when(courseRepository.getEnrolledCourseId(Mockito.anyLong())).thenReturn(enrolledcourseDetails);
		Mockito.doReturn(enrollmentDetails).when(enrollmentRepository).save(enrollmentDetails);

		Assertions.assertThrows(NullPointerException.class, new Executable() {

			@Override
			public void execute() throws Throwable {

				CourseEnrollmentDto courseEnrollmentDto = learningServiceImpl.enrollCourse(2L, 1L);
				assertNull(courseEnrollmentDto);
			}
		});

	}
	
	@Test
	@ExceptionHandler(NotFoundException.class)
	void enrollCourseTestStartDateLessThan2() throws NotFoundException {
		
		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);
		Mockito.when(courseRepository.findByCourseId(Mockito.anyLong())).thenReturn(enrolledcourseDetails1);
		Mockito.when(enrollmentRepository.getEnrollmentByUserIdAndCourseId(Mockito.anyLong(),Mockito.anyLong())).thenReturn(null);
		
		Assertions.assertThrows(NotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {
				
				CourseEnrollmentDto courseEnrollmentDto = learningServiceImpl.enrollCourse(2L,1L);
				Assertions.assertNull(courseEnrollmentDto);
			}
		});
	}
	
	@Test
	@ExceptionHandler(NotFoundException.class)
	void enrollCourseTestEndDateGreaterThan30() throws NotFoundException {

		Mockito.when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(user);
		Mockito.when(courseRepository.findByCourseId(Mockito.anyLong())).thenReturn(enrolledcourseDetails2);
		Mockito.when(enrollmentRepository.getEnrollmentByUserIdAndCourseId(Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(null);
		Mockito.when(courseRepository.getEnrolledCourseId(Mockito.anyLong())).thenReturn(enrolledcourseDetails);
		Mockito.when(enrollmentRepository.getEnrollmentDetailsByUserIdAndStatus(Mockito.anyLong()))
				.thenReturn(enrollmentDetailsList);

		Assertions.assertThrows(NotFoundException.class, new Executable() {

			@Override
			public void execute() throws Throwable {

				CourseEnrollmentDto courseEnrollmentDto = learningServiceImpl.enrollCourse(2L, 1L);
				Assertions.assertNotNull(courseEnrollmentDto);
			}
		});

	}

}
