package com.esprit.dto.migration;

public class ClassMigration {

	private String classId;

	private String label;

	private String speciality;

	private String category;

	private String email;

	public String getClassId() {
		return classId;
	}

	public ClassMigration classId(String classId) {
		this.classId = classId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public ClassMigration label(String label) {
		this.label = label;
		return this;
	}

	public String getSpeciality() {
		return speciality;
	}

	public ClassMigration speciality(String speciality) {
		this.speciality = speciality;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public ClassMigration category(String category) {
		this.category = category;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public ClassMigration email(String email) {
		this.email = email;
		return this;
	}

}