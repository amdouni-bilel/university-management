package com.esprit.dto.module;

import java.util.Set;

public class ModuleWithClassListDTO {

	private String moduleId;

	private String designation;

	private Set<String> classes;

	public String getModuleId() {
		return moduleId;
	}

	public ModuleWithClassListDTO moduleId(String moduleId) {
		this.moduleId = moduleId;
		return this;
	}

	public String getDesignation() {
		return designation;
	}

	public ModuleWithClassListDTO designation(String designation) {
		this.designation = designation;
		return this;
	}

	public Set<String> getClasses() {
		return classes;
	}

	public ModuleWithClassListDTO classes(Set<String> classes) {
		this.classes = classes;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((moduleId == null) ? 0 : moduleId.hashCode());
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
		ModuleWithClassListDTO other = (ModuleWithClassListDTO) obj;
		if (moduleId == null) {
			if (other.moduleId != null)
				return false;
		} else if (!moduleId.equals(other.moduleId))
			return false;
		return true;
	}

}