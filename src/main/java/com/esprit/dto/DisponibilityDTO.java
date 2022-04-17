package com.esprit.dto;

import java.util.Date;

public class DisponibilityDTO {

	private String disponibilityId;

	private String motif;

	private Date startDate;

	private Date endDate;

	private int startHour;

	private int endHour;

	private double examDuration;

	private String commentd;

	public String getDisponibilityId() {
		return disponibilityId;
	}

	public void setDisponibilityId(String disponibilityId) {
		this.disponibilityId = disponibilityId;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getStartHour() {
		return startHour;
	}

	public void setStartHour(int startHour) {
		this.startHour = startHour;
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

	public String getCommentd() {
		return commentd;
	}

	public void setCommentd(String commentd) {
		this.commentd = commentd;
	}

}