package com.esprit.dto.migration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PeriodMigration {

	private String periodId;

	private String code;

	private Date startDate;

	private Date endDate;

	private List<WeekMigration> weeks = new ArrayList<>();

	public String getPeriodId() {
		return periodId;
	}

	public PeriodMigration periodId(String periodId) {
		this.periodId = periodId;
		return this;
	}

	public String getCode() {
		return code;
	}

	public PeriodMigration code(String code) {
		this.code = code;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public PeriodMigration startDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public PeriodMigration endDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

	public List<WeekMigration> getWeeks() {
		return weeks;
	}

	public PeriodMigration weeks(List<WeekMigration> weeks) {
		this.weeks = weeks;
		return this;
	}

}