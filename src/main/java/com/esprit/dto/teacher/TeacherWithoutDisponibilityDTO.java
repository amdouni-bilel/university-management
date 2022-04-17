package com.esprit.dto.teacher;

public class TeacherWithoutDisponibilityDTO {

	private String teacherId;

	private String fullName;

	public TeacherWithoutDisponibilityDTO(String teacherId, String fullName) {
		this.teacherId = teacherId;
		this.fullName = fullName;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}