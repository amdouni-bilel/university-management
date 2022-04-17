package com.esprit.dto.migration;

import java.util.Date;
import java.util.List;

public class SemesterMigration {

	private String semestreId;

	private String code;

	private Date startDate;

	private Date endDate;

	private List<PeriodMigration> periods;

	public String getSemestreId() {
		return semestreId;
	}

	public SemesterMigration semestreId(String semestreId) {
		this.semestreId = semestreId;
		return this;
	}

	public String getCode() {
		return code;
	}

	public SemesterMigration code(String code) {
		this.code = code;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public SemesterMigration startDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public SemesterMigration endDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

	public List<PeriodMigration> getPeriods() {
		return periods;
	}

	public SemesterMigration periods(List<PeriodMigration> periods) {
		this.periods = periods;
		return this;
	}

}