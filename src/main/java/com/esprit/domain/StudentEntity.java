package com.esprit.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ESP_ETUDIANT")
public class StudentEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "student-uuid")
	@GenericGenerator(name = "student-uuid", strategy = "uuid2")
	private String studentId;

	private String firstName;

	private String lastName;

	private int cin;

	private String email;

	@ManyToOne(optional = false)
	@JoinColumn(name = "fk_group")
	private GroupEntity group;

	@ManyToOne(optional = true)
	@JoinColumn(name = "fk_class")
	private ClassEntity classs;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public StudentEntity firstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public StudentEntity lastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public StudentEntity email(String email) {
		this.email = email;
		return this;
	}

	public ClassEntity getClasss() {
		return classs;
	}

	public StudentEntity classs(ClassEntity classs) {
		this.classs = classs;
		return this;
	}

	public int getCin() {
		return cin;
	}

	public StudentEntity cin(int cin) {
		this.cin = cin;
		return this;
	}

	public GroupEntity getGroup() {
		return group;
	}

	public StudentEntity group(GroupEntity group) {
		this.group = group;
		return this;
	}

}