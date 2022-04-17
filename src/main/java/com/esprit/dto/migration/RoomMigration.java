package com.esprit.dto.migration;

import java.util.List;

public class RoomMigration {

	private String classRoomId;

	private String label;

	private String pole;

	private List<DisponibilityMigration> disponibilities;

	public String getClassRoomId() {
		return classRoomId;
	}

	public RoomMigration classRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public RoomMigration label(String label) {
		this.label = label;
		return this;
	}

	public String getPole() {
		return pole;
	}

	public RoomMigration pole(String pole) {
		this.pole = pole;
		return this;
	}

	public List<DisponibilityMigration> getDisponibilities() {
		return disponibilities;
	}

	public RoomMigration disponibilities(List<DisponibilityMigration> disponibilities) {
		this.disponibilities = disponibilities;
		return this;
	}

}