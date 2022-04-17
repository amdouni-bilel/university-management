package com.esprit.dto.classes;

public class CreateClassRequest {

	private String classId;

	private String label;

	private String speciality;

	private String category;

	private String email;

	private boolean group;

	public String getClassId() {
		return classId;
	}

	public CreateClassRequest classId(String classId) {
		this.classId = classId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public CreateClassRequest label(String label) {
		this.label = label;
		return this;
	}

	public String getSpeciality() {
		return speciality;
	}

	public CreateClassRequest speciality(String speciality) {
		this.speciality = speciality;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public CreateClassRequest category(String category) {
		this.category = category;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public CreateClassRequest email(String email) {
		this.email = email;
		return this;
	}

	public boolean isGroup() {
		return group;
	}

	public CreateClassRequest group(boolean group) {
		this.group = group;
		return this;
	}

}