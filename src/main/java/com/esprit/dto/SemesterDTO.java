package com.esprit.dto;

import java.util.Date;
import java.util.List;

public class SemesterDTO {

	private String semestreId;

	private String code;

	private Date startDate;

	private Date endDate;

	private List<PeriodDTO> periods;

	public String getSemestreId() {
		return semestreId;
	}

	public SemesterDTO semestreId(String semestreId) {
		this.semestreId = semestreId;
		return this;
	}

	public String getCode() {
		return code;
	}

	public SemesterDTO code(String code) {
		this.code = code;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public SemesterDTO startDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public SemesterDTO endDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

	public List<PeriodDTO> getPeriods() {
		return periods;
	}

	public SemesterDTO periods(List<PeriodDTO> periods) {
		this.periods = periods;
		return this;
	}

}