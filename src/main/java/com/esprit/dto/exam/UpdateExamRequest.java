package com.esprit.dto.exam;

import java.util.List;

public class UpdateExamRequest {

	private String examId;

	private String groupId;

	private List<String> teachers;

	private List<String> rooms;

	private List<String> bloc;

	private List<String> up;

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<String> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<String> teachers) {
		this.teachers = teachers;
	}

	public List<String> getRooms() {
		return rooms;
	}

	public void setRooms(List<String> rooms) {
		this.rooms = rooms;
	}

	public List<String> getBloc() {
		return bloc;
	}

	public void setBloc(List<String> bloc) {
		this.bloc = bloc;
	}

	public List<String> getUp() {
		return up;
	}

	public void setUp(List<String> up) {
		this.up = up;
	}

}