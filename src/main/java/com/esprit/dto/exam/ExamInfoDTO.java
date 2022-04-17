package com.esprit.dto.exam;

import java.util.Date;

public class ExamInfoDTO {

	private Date examDate;

	private int examHour;

	private double examDuration;

	public ExamInfoDTO() {
	}

	public ExamInfoDTO(Date examDate, int examHour, double examDuration) {
		this.examDate = examDate;
		this.examHour = examHour;
		this.examDuration = examDuration;
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

	public double getExamDuration() {
		return examDuration;
	}

	public void setExamDuration(double examDuration) {
		this.examDuration = examDuration;
	}

}