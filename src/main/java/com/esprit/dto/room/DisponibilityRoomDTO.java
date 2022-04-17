package com.esprit.dto.room;

import java.util.Date;

public class DisponibilityRoomDTO {

	private String classRoomId;

	private String disponibilityId;

	private String motif;

	private String commentd;

	private Date startDate;

	private Date endDate;

	private Integer startHour;

	private Integer endHour;

	public DisponibilityRoomDTO(String classRoomId, String disponibilityId, Date startDate, Date endDate, Integer startHour,
			Integer endHour, String motif, String commentd) {
		this.classRoomId = classRoomId;
		this.disponibilityId = disponibilityId;
		this.motif = motif;
		this.commentd = commentd;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startHour = startHour;
		this.endHour = endHour;
	}

	public String getClassRoomId() {
		return classRoomId;
	}

	public void setClassRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
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

	public String getCommentd() {
		return commentd;
	}

	public void setCommentd(String commentd) {
		this.commentd = commentd;
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

}