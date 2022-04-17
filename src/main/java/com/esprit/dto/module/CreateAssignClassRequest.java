package com.esprit.dto.module;

import java.util.List;

public class CreateAssignClassRequest {

	private String classId;

	private double coefficient;

	private String moduleId;

	private double nbrHour;

	private List<String> periodId;

	private String semesterId;

	private List<String> teacherIds;

	private String typeExam;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public double getNbrHour() {
		return nbrHour;
	}

	public void setNbrHour(double nbrHour) {
		this.nbrHour = nbrHour;
	}

	public List<String> getPeriodId() {
		return periodId;
	}

	public void setPeriodId(List<String> periodId) {
		this.periodId = periodId;
	}

	public String getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(String semesterId) {
		this.semesterId = semesterId;
	}

	public List<String> getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(List<String> teacherIds) {
		this.teacherIds = teacherIds;
	}

	public String getTypeExam() {
		return typeExam;
	}

	public void setTypeExam(String typeExam) {
		this.typeExam = typeExam;
	}

}