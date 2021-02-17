package com.hackathon.learningmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.learningmanagement.dto.UserRegistrationDto;
import com.hackathon.learningmanagement.entity.UserRegistration;
import com.hackathon.learningmanagement.exception.NotFoundException;
import com.hackathon.learningmanagement.service.LearningService;

@RestController
public class LearningController {

	@Autowired
	LearningService learningService;

	@PostMapping("/register")
	public UserRegistrationDto registerUser(@RequestBody UserRegistration userRegistration) {
		return learningService.registerUser(userRegistration);

	}

	@GetMapping(value = "/login")
	public ResponseEntity<String> loginUser(@RequestParam Long userId, @RequestParam String password) {
		String loginUserResponse;
		try {
			loginUserResponse = learningService.loginUser(userId, password);
		} catch (NotFoundException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<>(loginUserResponse, HttpStatus.OK);
	}
}
