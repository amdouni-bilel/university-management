package com.esprit.dto.teacher;

import java.util.Date;

public class DisponibilityTeachetDTO {

	private String teacherId;

	private String fullName;

	private String disponibilityId;

	private String motif;

	private String commentd;

	private Date startDate;

	private Date endDate;

	private Integer startHour;

	private Integer endHour;

	public DisponibilityTeachetDTO(String teacherId, String fullName, String disponibilityId, Date startDate,
			Date endDate, Integer startHour, Integer endHour, String motif, String commentd) {
		this.teacherId = teacherId;
		this.fullName = fullName;
		this.disponibilityId = disponibilityId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startHour = startHour;
		this.endHour = endHour;
		this.motif = motif;
		this.commentd = commentd;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

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

	public Integer getStartHour() {
		return startHour;
	}

	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}

	public Integer getEndHour() {
		return endHour;
	}

	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}

	public String getCommentd() {
		return commentd;
	}

	public void setCommentd(String commentd) {
		this.commentd = commentd;
	}

}