package com.esprit.dto.seance;

public class CreateSeanceRequest {

	private String seanceId;

	private int startHour;

	private int endHour;

	public String getSeanceId() {
		return seanceId;
	}

	public CreateSeanceRequest seanceId(String seanceId) {
		this.seanceId = seanceId;
		return this;
	}

	public int getStartHour() {
		return startHour;
	}

	public CreateSeanceRequest startHour(int startHour) {
		this.startHour = startHour;
		return this;
	}

	public int getEndHour() {
		return endHour;
	}

	public CreateSeanceRequest endHour(int endHour) {
		this.endHour = endHour;
		return this;
	}

}