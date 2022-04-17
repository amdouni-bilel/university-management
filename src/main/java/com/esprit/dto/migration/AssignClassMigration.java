package com.esprit.dto.migration;

import java.util.List;

public class AssignClassMigration {

	private String classId;

	private int coefficient;

	private int nbrHour;

	private String semesterId;

	private List<String> periodId;

	private String typeExam;

	private List<String> teacherIds;

	public String getClassId() {
		return classId;
	}

	public AssignClassMigration classId(String classId) {
		this.classId = classId;
		return this;
	}

	public int getCoefficient() {
		return coefficient;
	}

	public AssignClassMigration coefficient(int coefficient) {
		this.coefficient = coefficient;
		return this;
	}

	public int getNbrHour() {
		return nbrHour;
	}

	public AssignClassMigration nbrHour(int nbrHour) {
		this.nbrHour = nbrHour;
		return this;
	}

	public String getSemesterId() {
		return semesterId;
	}

	public AssignClassMigration semesterId(String semesterId) {
		this.semesterId = semesterId;
		return this;
	}

	public List<String> getPeriodId() {
		return periodId;
	}

	public AssignClassMigration periodId(List<String> periodId) {
		this.periodId = periodId;
		return this;
	}

	public String getTypeExam() {
		return typeExam;
	}

	public AssignClassMigration typeExam(String typeExam) {
		this.typeExam = typeExam;
		return this;
	}

	public List<String> getTeacherIds() {
		return teacherIds;
	}

	public AssignClassMigration teacherIds(List<String> teacherIds) {
		this.teacherIds = teacherIds;
		return this;
	}

}