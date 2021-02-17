package com.hackathon.learningmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackathon.learningmanagement.dto.UserRegistrationDto;
import com.hackathon.learningmanagement.entity.Category;
import com.hackathon.learningmanagement.entity.CourseDetails;
import com.hackathon.learningmanagement.entity.UserRegistration;
import com.hackathon.learningmanagement.exception.NotFoundException;
import com.hackathon.learningmanagement.repository.CategoryRepository;
import com.hackathon.learningmanagement.repository.CourseRepository;
import com.hackathon.learningmanagement.repository.UserRepository;
import com.hackathon.learningmanagement.service.LearningService;

@Service
public class LearningServiceImpl implements LearningService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Override
	public UserRegistrationDto registerUser(UserRegistration userRegistration) {
		UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
		UserRegistration registerResponse = userRepository.save(userRegistration);
		userRegistrationDto.setUserId(registerResponse.getUserId());
		return userRegistrationDto;
	}

	@Override
	public String loginUser(Long userId, String password) throws NotFoundException {
		UserRegistration userRegistration = userRepository.findByUserIdAndPassword(userId, password);
		UserRegistration user = userRepository.findByUserId(userId);
		if (user == null) {
			throw new NotFoundException("User not found,please register!!!");
		}
		if (userRegistration == null) {
			throw new NotFoundException("User id and password mismatch!!!");
		}
		return "Login Successfull!!";
	}

	@Override
	public List getCourseDetails(Long userId, String courseName, String categoryName) throws NotFoundException {

		List<CourseDetails> courseDetailsList = new ArrayList<>();

		if (userRepository.findByUserId(userId) == null) {
			throw new NotFoundException("User not found, Please register!!!");
		}
		if (categoryName == null) {
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
			return courseDetailsList;
		}
	}
}