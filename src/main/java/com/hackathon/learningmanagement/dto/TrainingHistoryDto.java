package com.hackathon.learningmanagement.dto;

public class TrainingHistoryDto {
	
	private Long enrollmentId;
	private Long courseId;
	
	public Long getEnrollmentId() {
		return enrollmentId;
	}
	public void setEnrollmentId(Long enrollmentId) {
		this.enrollmentId = enrollmentId;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	

}
