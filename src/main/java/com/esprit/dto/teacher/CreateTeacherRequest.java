package com.esprit.dto.teacher;

import java.util.List;

import com.esprit.dto.CreateDisponibilityRequest;

public class CreateTeacherRequest {

	private String teacherId;

	private String fullName;

	private String email;

	private String phone;

	private String up;

	private String type;

	private String departement;

	private List<CreateDisponibilityRequest> disponibilities;

	public String getTeacherId() {
		return teacherId;
	}

	public CreateTeacherRequest teacherId(String teacherId) {
		this.teacherId = teacherId;
		return this;
	}

	public String getFullName() {
		return fullName;
	}

	public CreateTeacherRequest fullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public CreateTeacherRequest email(String email) {
		this.email = email;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public CreateTeacherRequest phone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getUp() {
		return up;
	}

	public CreateTeacherRequest up(String up) {
		this.up = up;
		return this;
	}

	public String getType() {
		return type;
	}

	public CreateTeacherRequest type(String type) {
		this.type = type;
		return this;
	}

	public String getDepartement() {
		return departement;
	}

	public CreateTeacherRequest departement(String departement) {
		this.departement = departement;
		return this;
	}

	public List<CreateDisponibilityRequest> getDisponibilities() {
		return disponibilities;
	}

	public CreateTeacherRequest disponibilities(List<CreateDisponibilityRequest> disponibilities) {
		this.disponibilities = disponibilities;
		return this;
	}

}