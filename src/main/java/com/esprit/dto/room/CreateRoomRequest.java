package com.esprit.dto.room;

import java.util.List;

import com.esprit.dto.CreateDisponibilityRequest;

public class CreateRoomRequest {

	private String classRoomId;

	private String label;

	private int capacity;

	private String pole;

	private String bloc;

	private List<CreateDisponibilityRequest> disponibilities;

	public String getClassRoomId() {
		return classRoomId;
	}

	public CreateRoomRequest classRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public CreateRoomRequest label(String label) {
		this.label = label;
		return this;
	}

	public int getCapacity() {
		return capacity;
	}

	public CreateRoomRequest capacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	public String getPole() {
		return pole;
	}

	public CreateRoomRequest pole(String pole) {
		this.pole = pole;
		return this;
	}

	public String getBloc() {
		return bloc;
	}

	public CreateRoomRequest bloc(String bloc) {
		this.bloc = bloc;
		return this;
	}

	public List<CreateDisponibilityRequest> getDisponibilities() {
		return disponibilities;
	}

	public CreateRoomRequest disponibilities(List<CreateDisponibilityRequest> disponibilities) {
		this.disponibilities = disponibilities;
		return this;
	}

}