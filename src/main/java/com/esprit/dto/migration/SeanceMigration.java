package com.esprit.dto.migration;

public class SeanceMigration {

	private String seanceId;

	private int startHour;

	private int endHour;

	public String getSeanceId() {
		return seanceId;
	}

	public SeanceMigration seanceId(String seanceId) {
		this.seanceId = seanceId;
		return this;
	}

	public int getStartHour() {
		return startHour;
	}

	public SeanceMigration startHour(int startHour) {
		this.startHour = startHour;
		return this;
	}

	public int getEndHour() {
		return endHour;
	}

	public SeanceMigration endHour(int endHour) {
		this.endHour = endHour;
		return this;
	}

}