package com.esprit.dto;

public class GroupDTO {

	private String groupId;

	private String label;

	public String getGroupId() {
		return groupId;
	}

	public GroupDTO groupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public GroupDTO label(String label) {
		this.label = label;
		return this;
	}

}