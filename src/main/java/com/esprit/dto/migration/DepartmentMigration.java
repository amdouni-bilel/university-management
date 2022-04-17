package com.esprit.dto.migration;

import java.util.List;

public class DepartmentMigration {

	private String departmenetId;

	private String label;

	private List<String> up;

	public String getDepartmenetId() {
		return departmenetId;
	}

	public DepartmentMigration departmenetId(String departmenetId) {
		this.departmenetId = departmenetId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public DepartmentMigration label(String label) {
		this.label = label;
		return this;
	}

	public List<String> getUp() {
		return up;
	}

	public DepartmentMigration up(List<String> up) {
		this.up = up;
		return this;
	}

}