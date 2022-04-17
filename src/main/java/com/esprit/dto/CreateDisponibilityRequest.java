package com.esprit.dto;

import java.util.Date;

public class CreateDisponibilityRequest {

	private String disponibilityId;

	private String day;

	private String seanceId;

	private String weekId;

	private String semesterId;

	private String year;

	private String periodId;

	private String motif;

	private int startHour;

	private int endHour;

	private Date startDate;

	private Date endDate;

	private String commentd;

	public String getDisponibilityId() {
		return disponibilityId;
	}

	public CreateDisponibilityRequest disponibilityId(String disponibilityId) {
		this.disponibilityId = disponibilityId;
		return this;
	}

	public String getDay() {
		return day;
	}

	public CreateDisponibilityRequest day(String day) {
		this.day = day;
		return this;
	}

	public String getSeanceId() {
		return seanceId;
	}

	public CreateDisponibilityRequest seanceId(String seanceId) {
		this.seanceId = seanceId;
		return this;
	}

	public String getWeekId() {
		return weekId;
	}

	public CreateDisponibilityRequest weekId(String weekId) {
		this.weekId = weekId;
		return this;
	}

	public String getSemesterId() {
		return semesterId;
	}

	public CreateDisponibilityRequest semesterId(String semesterId) {
		this.semesterId = semesterId;
		return this;
	}

	public String getYear() {
		return year;
	}

	public CreateDisponibilityRequest year(String year) {
		this.year = year;
		return this;
	}

	public String getPeriodId() {
		return periodId;
	}

	public CreateDisponibilityRequest periodId(String periodId) {
		this.periodId = periodId;
		return this;
	}

	public String getMotif() {
		return motif;
	}

	public CreateDisponibilityRequest motif(String motif) {
		this.motif = motif;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public CreateDisponibilityRequest startDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public CreateDisponibilityRequest endDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

	public int getStartHour() {
		return startHour;
	}

	public CreateDisponibilityRequest startHour(int startHour) {
		this.startHour = startHour;
		return this;
	}

	public int getEndHour() {
		return endHour;
	}

	public CreateDisponibilityRequest endHour(int endHour) {
		this.endHour = endHour;
		return this;
	}

	public String getCommentd() {
		return commentd;
	}

	public CreateDisponibilityRequest commentd(String commentd) {
		this.commentd = commentd;
		return this;
	}

}