package com.esprit.dto.student;

import com.esprit.dto.GroupDTO;
import com.esprit.dto.classes.ClassDTO;

public class StudentDTO {

	private String studentId;

	private String firstName;

	private String lastName;

	private int cin;

	private GroupDTO group;

	private String email;

	private ClassDTO classs;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ClassDTO getClasss() {
		return classs;
	}

	public void setClasss(ClassDTO classs) {
		this.classs = classs;
	}

	public int getCin() {
		return cin;
	}

	public void setCin(int cin) {
		this.cin = cin;
	}

	public GroupDTO getGroup() {
		return group;
	}

	public void setGroup(GroupDTO group) {
		this.group = group;
	}

}