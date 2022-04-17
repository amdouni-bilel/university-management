package com.esprit.dto;

import java.util.Date;
import java.util.List;

public class PeriodDTO {

	private String periodId;

	private String code;

	private Date startDate;

	private Date endDate;

	private List<WeekDTO> weeks;

	public String getPeriodId() {
		return periodId;
	}

	public PeriodDTO periodId(String periodId) {
		this.periodId = periodId;
		return this;
	}

	public String getCode() {
		return code;
	}

	public PeriodDTO code(String code) {
		this.code = code;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public PeriodDTO startDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public PeriodDTO endDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

	public List<WeekDTO> getWeeks() {
		return weeks;
	}

	public PeriodDTO weeks(List<WeekDTO> weeks) {
		this.weeks = weeks;
		return this;
	}

}