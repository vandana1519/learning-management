package com.hackathon.learningmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hackathon.learningmanagement.entity.UserRegistration;

@Repository
public interface UserRepository extends JpaRepository<UserRegistration, Long>{

}
