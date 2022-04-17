package com.esprit.dto.module;

import java.util.List;

public class CreateModuleRequest {

	private String moduleId;

	private String designation;

	private String up;

	private List<AssignClassToModuleRequest> assignClasses;

	public String getModuleId() {
		return moduleId;
	}

	public CreateModuleRequest moduleId(String moduleId) {
		this.moduleId = moduleId;
		return this;
	}

	public String getDesignation() {
		return designation;
	}

	public CreateModuleRequest designation(String designation) {
		this.designation = designation;
		return this;
	}

	public String getUp() {
		return up;
	}

	public CreateModuleRequest up(String up) {
		this.up = up;
		return this;
	}

	public List<AssignClassToModuleRequest> getAssignClasses() {
		return assignClasses;
	}

	public CreateModuleRequest assignClasses(List<AssignClassToModuleRequest> assignClasses) {
		this.assignClasses = assignClasses;
		return this;
	}

}