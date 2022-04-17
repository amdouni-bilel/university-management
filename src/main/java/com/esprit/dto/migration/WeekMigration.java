package com.esprit.dto.migration;

import java.util.Date;

public class WeekMigration {

	private String weekId;

	private Date startDate;

	private Date endDate;

	private String semesterCode;

	private String periodCode;

	public String getWeekId() {
		return weekId;
	}

	public WeekMigration weekId(String weekId) {
		this.weekId = weekId;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public WeekMigration startDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public WeekMigration endDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

	public String getSemesterCode() {
		return semesterCode;
	}

	public WeekMigration semesterCode(String semesterCode) {
		this.semesterCode = semesterCode;
		return this;
	}

	public String getPeriodCode() {
		return periodCode;
	}

	public WeekMigration periodCode(String periodCode) {
		this.periodCode = periodCode;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((weekId == null) ? 0 : weekId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeekMigration other = (WeekMigration) obj;
		if (weekId == null) {
			if (other.weekId != null)
				return false;
		} else if (!weekId.equals(other.weekId))
			return false;
		return true;
	}

}