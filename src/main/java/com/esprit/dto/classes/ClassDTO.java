package com.esprit.dto.classes;

import java.util.List;

import com.esprit.dto.SpecialityDTO;

public class ClassDTO {

	private String classId;

	private String label;

	private boolean group;

	private SpecialityDTO speciality;

	private String category;

	private String email;

	private String typeExam;

	private int nbrStudents;

	private List<ModuleDTO> modules;

	public String getClassId() {
		return classId;
	}

	public ClassDTO classId(String classId) {
		this.classId = classId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public ClassDTO label(String label) {
		this.label = label;
		return this;
	}

	public SpecialityDTO getSpeciality() {
		return speciality;
	}

	public ClassDTO speciality(SpecialityDTO speciality) {
		this.speciality = speciality;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public ClassDTO category(String category) {
		this.category = category;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public ClassDTO email(String email) {
		this.email = email;
		return this;
	}

	public String getTypeExam() {
		return typeExam;
	}

	public ClassDTO typeExam(String typeExam) {
		this.typeExam = typeExam;
		return this;
	}

	public int getNbrStudents() {
		return nbrStudents;
	}

	public ClassDTO nbrStudents(int nbrStudents) {
		this.nbrStudents = nbrStudents;
		return this;
	}

	public boolean isGroup() {
		return group;
	}

	public ClassDTO group(boolean group) {
		this.group = group;
		return this;
	}

	public List<ModuleDTO> getModules() {
		return modules;
	}

	public ClassDTO modules(List<ModuleDTO> modules) {
		this.modules = modules;
		return this;
	}

}