package com.esprit.dto.migration;

public class DayMigration {

	private String dayId;

	private String label;

	public String getDayId() {
		return dayId;
	}

	public DayMigration dayId(String dayId) {
		this.dayId = dayId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public DayMigration label(String label) {
		this.label = label;
		return this;
	}

}
