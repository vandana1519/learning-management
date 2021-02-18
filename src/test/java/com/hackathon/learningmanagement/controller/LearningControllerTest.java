package com.hackathon.learningmanagement.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.learningmanagement.constants.AppConstants;
import com.hackathon.learningmanagement.dto.CourseEnrollmentDto;
import com.hackathon.learningmanagement.dto.TrainingHistoryDto;
import com.hackathon.learningmanagement.dto.UserRegistrationDto;
import com.hackathon.learningmanagement.entity.Category;
import com.hackathon.learningmanagement.entity.CourseDetails;
import com.hackathon.learningmanagement.entity.EnrollmentDetails;
import com.hackathon.learningmanagement.entity.UserRegistration;
import com.hackathon.learningmanagement.exception.NotFoundException;
import com.hackathon.learningmanagement.service.LearningService;

@WebMvcTest(value = LearningController.class)
class LearningControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	LearningService learningService;

	static UserRegistration user = new UserRegistration();
	static UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
	static Category category = new Category();
	static CourseDetails courseDetails = new CourseDetails();
	static List<CourseDetails> courseDetailsList = new ArrayList<>();
	static EnrollmentDetails enrollmentDetails = new EnrollmentDetails();
	static CourseEnrollmentDto courseEnrollmentDto = new CourseEnrollmentDto();
	static TrainingHistoryDto trainingHistoryDto = new TrainingHistoryDto();
	static List<TrainingHistoryDto> trainingHistoryDtoList = new ArrayList<>();

	private ObjectMapper objectMapper = new ObjectMapper();

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

	@Test
	void registerUserTestSuccess() throws Exception {

		Mockito.doReturn(userRegistrationDto).when(learningService).registerUser(user);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user));

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
	}

	@Test
	void loginUserTestSuccess() throws Exception {

		Mockito.doReturn(new String("Login Successfull!!")).when(learningService).loginUser(Mockito.anyLong(),
				Mockito.anyString());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login").accept(MediaType.APPLICATION_JSON)
				.param("userId", "1").param("password", "12345");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	@Test
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	void loginUserTestException() throws Exception {

		Mockito.doThrow(NotFoundException.class).when(learningService).loginUser(Mockito.anyLong(),
				Mockito.anyString());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/login").accept(MediaType.APPLICATION_JSON)
				.param("userId", "2").param("password", "12345");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.EXPECTATION_FAILED.value(), result.getResponse().getStatus());
	}

	@Test
	void getCourseDetailsTestSuccess() throws Exception {

		Mockito.doReturn(courseDetailsList).when(learningService).getCourseDetails(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyString());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/courses/{userId}", 1L)
				.accept(MediaType.APPLICATION_JSON).param("courseName", "Java").param("categoryName", "Technical");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	@Test
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	void getCourseDetailsTestException() throws Exception {

		Mockito.doThrow(NotFoundException.class).when(learningService).getCourseDetails(Mockito.anyLong(),
				Mockito.anyString(), Mockito.anyString());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/courses/{userId}", 1L)
				.accept(MediaType.APPLICATION_JSON).param("courseName", "Java").param("categoryName", "Technical");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.EXPECTATION_FAILED.value(), result.getResponse().getStatus());
	}

	@Test
	void enrollCourseTestSuccess() throws Exception {

		Mockito.doReturn(courseEnrollmentDto).when(learningService).enrollCourse(Mockito.anyLong(), Mockito.anyLong());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/enroll/{userId}", 1L)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).param("courseId", "1");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
	}

	@Test
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	void enrollCourseTestException() throws Exception {

		Mockito.doThrow(NotFoundException.class).when(learningService).enrollCourse(Mockito.anyLong(),
				Mockito.anyLong());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/enroll/{userId}", 1L)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).param("courseId", "1");

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.EXPECTATION_FAILED.value(), result.getResponse().getStatus());
	}
	
	@Test
	void getTrainingHistoryTestSuccess() throws Exception {

		Mockito.doReturn(trainingHistoryDtoList).when(learningService).getTrainingHistory(Mockito.anyLong());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/history/{userId}", 1L)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
	}

	@Test
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	void getTrainingHistoryTestException() throws Exception {

		Mockito.doThrow(NotFoundException.class).when(learningService).getTrainingHistory(Mockito.anyLong());
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/history/{userId}", 1L)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
	}

}
