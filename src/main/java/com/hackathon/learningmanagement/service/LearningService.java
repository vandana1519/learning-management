package com.hackathon.learningmanagement.service;

import com.hackathon.learningmanagement.dto.UserRegistrationDto;
import com.hackathon.learningmanagement.entity.UserRegistration;

public interface LearningService {
	
	public UserRegistrationDto registerUser(UserRegistration userRegistration);

}
