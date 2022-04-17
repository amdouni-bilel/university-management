package com.esprit.dto.module;

import java.util.List;

import com.esprit.dto.PeriodDTO;
import com.esprit.dto.SemesterDTO;
import com.esprit.dto.classes.ClassDTO;
import com.esprit.dto.teacher.TeacherDTO;

public class AssignClassModuleDTO {

	private String assignId;

	private double coefficient;

	private double nbrHour;

	private SemesterDTO semester;

	private List<PeriodDTO> periods;

	private String typeExam;

	private List<TeacherDTO> teachers;

	private ClassDTO classs;

	public String getAssignId() {
		return assignId;
	}

	public void setAssignId(String assignId) {
		this.assignId = assignId;
	}

	public double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}

	public double getNbrHour() {
		return nbrHour;
	}

	public void setNbrHour(double nbrHour) {
		this.nbrHour = nbrHour;
	}

	public SemesterDTO getSemester() {
		return semester;
	}

	public void setSemester(SemesterDTO semester) {
		this.semester = semester;
	}

	public List<PeriodDTO> getPeriods() {
		return periods;
	}

	public void setPeriods(List<PeriodDTO> periods) {
		this.periods = periods;
	}

	public String getTypeExam() {
		return typeExam;
	}

	public void setTypeExam(String typeExam) {
		this.typeExam = typeExam;
	}

	public List<TeacherDTO> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<TeacherDTO> teachers) {
		this.teachers = teachers;
	}

	public ClassDTO getClasss() {
		return classs;
	}

	public void setClasss(ClassDTO classs) {
		this.classs = classs;
	}

}