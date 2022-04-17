package com.esprit.dto.module;

import java.util.List;

import com.esprit.dto.PeriodDTO;
import com.esprit.dto.SemesterDTO;
import com.esprit.dto.classes.ClassDTO;
import com.esprit.dto.teacher.TeacherDTO;

public class ModuleClassDTO {

	private String moduleId;

	private String designation;

	private String assignId;

	private double coefficient;

	private double nbrHour;

	private SemesterDTO semester;

	private List<PeriodDTO> periods;

	private String typeExam;

	private List<TeacherDTO> teachers;

	private ClassDTO classs;

	public String getModuleId() {
		return moduleId;
	}

	public ModuleClassDTO moduleId(String moduleId) {
		this.moduleId = moduleId;
		return this;
	}

	public String getDesignation() {
		return designation;
	}

	public ModuleClassDTO designation(String designation) {
		this.designation = designation;
		return this;
	}

	public String getAssignId() {
		return assignId;
	}

	public ModuleClassDTO assignId(String assignId) {
		this.assignId = assignId;
		return this;
	}

	public double getCoefficient() {
		return coefficient;
	}

	public ModuleClassDTO coefficient(double coefficient) {
		this.coefficient = coefficient;
		return this;
	}

	public double getNbrHour() {
		return nbrHour;
	}

	public ModuleClassDTO nbrHour(double nbrHour) {
		this.nbrHour = nbrHour;
		return this;
	}

	public SemesterDTO getSemester() {
		return semester;
	}

	public ModuleClassDTO semester(SemesterDTO semester) {
		this.semester = semester;
		return this;
	}

	public List<PeriodDTO> getPeriods() {
		return periods;
	}

	public ModuleClassDTO periods(List<PeriodDTO> periods) {
		this.periods = periods;
		return this;
	}

	public String getTypeExam() {
		return typeExam;
	}

	public ModuleClassDTO typeExam(String typeExam) {
		this.typeExam = typeExam;
		return this;
	}

	public List<TeacherDTO> getTeachers() {
		return teachers;
	}

	public ModuleClassDTO teachers(List<TeacherDTO> teachers) {
		this.teachers = teachers;
		return this;
	}

	public ClassDTO getClasss() {
		return classs;
	}

	public ModuleClassDTO classs(ClassDTO classs) {
		this.classs = classs;
		return this;
	}

}