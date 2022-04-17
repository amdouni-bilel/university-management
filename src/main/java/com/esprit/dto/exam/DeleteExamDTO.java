package com.esprit.dto.exam;

import java.util.List;

public class DeleteExamDTO {

	private String examId;

	private String classe;

	private String groupId;

	private List<String> teacherIds;

	private List<String> roomIds;

	public String getExamId() {
		return examId;
	}

	public DeleteExamDTO examId(String examId) {
		this.examId = examId;
		return this;
	}

	public String getClasse() {
		return classe;
	}

	public DeleteExamDTO classe(String classe) {
		this.classe = classe;
		return this;
	}

	public List<String> getTeacherIds() {
		return teacherIds;
	}

	public DeleteExamDTO teacherIds(List<String> teacherIds) {
		this.teacherIds = teacherIds;
		return this;
	}

	public List<String> getRoomIds() {
		return roomIds;
	}

	public DeleteExamDTO roomIds(List<String> roomIds) {
		this.roomIds = roomIds;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

}