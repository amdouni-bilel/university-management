package com.esprit.dto.migration;

import java.util.List;
import java.util.Set;

public class TeacherMigration {

	private String teacherId;

	private int cin;

	private String sex;

	private String fullName;

	private String email;

	private String phone;

	private String type;

	private String up;

	private String departement;

	private Set<String> modules;

	private List<DisponibilityMigration> disponibilities;

	public String getTeacherId() {
		return teacherId;
	}

	public TeacherMigration teacherId(String teacherId) {
		this.teacherId = teacherId;
		return this;
	}

	public int getCin() {
		return cin;
	}

	public TeacherMigration cin(int cin) {
		this.cin = cin;
		return this;
	}

	public String getSex() {
		return sex;
	}

	public TeacherMigration sex(String sex) {
		this.sex = sex;
		return this;
	}

	public String getFullName() {
		return fullName;
	}

	public TeacherMigration fullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public TeacherMigration email(String email) {
		this.email = email;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public TeacherMigration phone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getType() {
		return type;
	}

	public TeacherMigration type(String type) {
		this.type = type;
		return this;
	}

	public String getUp() {
		return up;
	}

	public TeacherMigration up(String up) {
		this.up = up;
		return this;
	}

	public String getDepartement() {
		return departement;
	}

	public TeacherMigration departement(String departement) {
		this.departement = departement;
		return this;
	}

	public Set<String> getModules() {
		return modules;
	}

	public TeacherMigration modules(Set<String> modules) {
		this.modules = modules;
		return this;
	}

	public List<DisponibilityMigration> getDisponibilities() {
		return disponibilities;
	}

	public TeacherMigration disponibilities(List<DisponibilityMigration> disponibilities) {
		this.disponibilities = disponibilities;
		return this;
	}

}