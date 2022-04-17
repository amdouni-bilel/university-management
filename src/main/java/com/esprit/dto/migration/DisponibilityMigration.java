package com.esprit.dto.migration;

public class DisponibilityMigration {

	private String day;

	private String seanceId;

	private String weekId;

	private String semesterId;

	private String year;

	private String periodId;

	private String motif;

	public String getDay() {
		return day;
	}

	public DisponibilityMigration day(String day) {
		this.day = day;
		return this;
	}

	public String getSeanceId() {
		return seanceId;
	}

	public DisponibilityMigration seanceId(String seanceId) {
		this.seanceId = seanceId;
		return this;
	}

	public String getWeekId() {
		return weekId;
	}

	public DisponibilityMigration weekId(String weekId) {
		this.weekId = weekId;
		return this;
	}

	public String getSemesterId() {
		return semesterId;
	}

	public DisponibilityMigration semesterId(String semesterId) {
		this.semesterId = semesterId;
		return this;
	}

	public String getYear() {
		return year;
	}

	public DisponibilityMigration year(String year) {
		this.year = year;
		return this;
	}

	public String getPeriodId() {
		return periodId;
	}

	public DisponibilityMigration periodId(String periodId) {
		this.periodId = periodId;
		return this;
	}

	public String getMotif() {
		return motif;
	}

	public DisponibilityMigration motif(String motif) {
		this.motif = motif;
		return this;
	}

}