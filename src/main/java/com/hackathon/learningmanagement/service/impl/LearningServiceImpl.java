package com.hackathon.learningmanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hackathon.learningmanagement.dto.UserRegistrationDto;
import com.hackathon.learningmanagement.entity.UserRegistration;
import com.hackathon.learningmanagement.exception.NotFoundException;
import com.hackathon.learningmanagement.repository.UserRepository;
import com.hackathon.learningmanagement.service.LearningService;

@Service
public class LearningServiceImpl implements LearningService {

	@Autowired
	UserRepository userRepository;

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

}