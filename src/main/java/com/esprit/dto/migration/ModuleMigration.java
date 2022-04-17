package com.esprit.dto.migration;

import java.util.List;

public class ModuleMigration {

	private String moduleId;

	private String designation;

	private String up;

	private List<AssignClassMigration> assignClasses;

	public String getModuleId() {
		return moduleId;
	}

	public ModuleMigration moduleId(String moduleId) {
		this.moduleId = moduleId;
		return this;
	}

	public String getDesignation() {
		return designation;
	}

	public ModuleMigration designation(String designation) {
		this.designation = designation;
		return this;
	}

	public String getUp() {
		return up;
	}

	public ModuleMigration up(String up) {
		this.up = up;
		return this;
	}

	public List<AssignClassMigration> getAssignClasses() {
		return assignClasses;
	}

	public ModuleMigration assignClasses(List<AssignClassMigration> assignClasses) {
		this.assignClasses = assignClasses;
		return this;
	}

}