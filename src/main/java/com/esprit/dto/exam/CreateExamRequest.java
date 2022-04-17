package com.esprit.dto.exam;

import java.util.Date;
import java.util.List;

import com.esprit.dto.SessionDTO;

public class CreateExamRequest {

	private String examId;

	private SessionDTO session;

	private Date examDate;

	private int examHour;

	private int endHour;

	private double examDuration;

	private String module;

	private List<String> classes;

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

	public SessionDTO getSession() {
		return session;
	}

	public void setSession(SessionDTO session) {
		this.session = session;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public List<String> getClasses() {
		return classes;
	}

	public void setClasses(List<String> classes) {
		this.classes = classes;
	}

	public List<String> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<String> teachers) {
		this.teachers = teachers;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public int getExamHour() {
		return examHour;
	}

	public void setExamHour(int examHour) {
		this.examHour = examHour;
	}

	public int getEndHour() {
		return endHour;
	}

	public void setEndHour(int endHour) {
		this.endHour = endHour;
	}

	public double getExamDuration() {
		return examDuration;
	}

	public void setExamDuration(double examDuration) {
		this.examDuration = examDuration;
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

	public class SessionExamRequest {

		private String idSession;

		private String labelSession;

		public String getIdSession() {
			return idSession;
		}

		public void setIdSession(String idSession) {
			this.idSession = idSession;
		}

		public String getLabelSession() {
			return labelSession;
		}

		public void setLabelSession(String labelSession) {
			this.labelSession = labelSession;
		}

	}

}