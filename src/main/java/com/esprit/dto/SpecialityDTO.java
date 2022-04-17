package com.esprit.dto;

public class SpecialityDTO {

	private String specialityId;

	private String label;

	public String getSpecialityId() {
		return specialityId;
	}

	public SpecialityDTO specialityId(String specialityId) {
		this.specialityId = specialityId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public SpecialityDTO label(String label) {
		this.label = label;
		return this;
	}

}