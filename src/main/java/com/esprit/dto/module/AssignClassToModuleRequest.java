package com.esprit.dto.module;

import java.util.List;

public class AssignClassToModuleRequest {

	private String classId;

	private double coefficient;

	private double nbrHour;

	private String semesterId;

	private List<String> periodId;

	private String typeExam;

	private List<String> teacherIds;

	public double getCoefficient() {
		return coefficient;
	}

	public AssignClassToModuleRequest coefficient(double coefficient) {
		this.coefficient = coefficient;
		return this;
	}

	public double getNbrHour() {
		return nbrHour;
	}

	public AssignClassToModuleRequest nbrHour(double nbrHour) {
		this.nbrHour = nbrHour;
		return this;
	}

	public String getSemesterId() {
		return semesterId;
	}

	public AssignClassToModuleRequest semesterId(String semesterId) {
		this.semesterId = semesterId;
		return this;
	}

	public List<String> getPeriodId() {
		return periodId;
	}

	public AssignClassToModuleRequest periodId(List<String> periodId) {
		this.periodId = periodId;
		return this;
	}

	public String getTypeExam() {
		return typeExam;
	}

	public AssignClassToModuleRequest typeExam(String typeExam) {
		this.typeExam = typeExam;
		return this;
	}

	public List<String> getTeacherIds() {
		return teacherIds;
	}

	public AssignClassToModuleRequest teacherIds(List<String> teacherIds) {
		this.teacherIds = teacherIds;
		return this;
	}

	public String getClassId() {
		return classId;
	}

	public AssignClassToModuleRequest classId(String classId) {
		this.classId = classId;
		return this;
	}

}