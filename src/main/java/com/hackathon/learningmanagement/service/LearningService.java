package com.hackathon.learningmanagement.service;

import com.hackathon.learningmanagement.dto.UserRegistrationDto;
import com.hackathon.learningmanagement.entity.UserRegistration;
import com.hackathon.learningmanagement.exception.NotFoundException;

public interface LearningService {

	public UserRegistrationDto registerUser(UserRegistration userRegistration);

	public String loginUser(Long userId, String password) throws NotFoundException;

}
