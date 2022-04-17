package com.esprit.dto;

import java.util.Date;

public class WeekDTO {

	private String weekId;

	private Date startDate;

	private Date endDate;

	public String getWeekId() {
		return weekId;
	}

	public WeekDTO weekId(String weekId) {
		this.weekId = weekId;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public WeekDTO startDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public WeekDTO endDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

}