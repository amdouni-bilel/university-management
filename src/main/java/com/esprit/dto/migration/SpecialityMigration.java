package com.esprit.dto.migration;

public class SpecialityMigration {

	private String specialityId;

	private String label;

	public String getSpecialityId() {
		return specialityId;
	}

	public SpecialityMigration specialityId(String specialityId) {
		this.specialityId = specialityId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public SpecialityMigration label(String label) {
		this.label = label;
		return this;
	}

}