package com.esprit.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SALLE")
public class RoomEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String classRoomId;

	private String label;

	private int capacity;

	private String pole;

	private String bloc;
	
	@ElementCollection
	private Set<String> examIds = new HashSet<>();

	@OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<DisponibilityEntity> disponibilities;

	@JsonIgnore
	@ManyToMany(mappedBy = "teachers")
	private List<GroupEntity> rooms;

	public void addDisponibilities(List<DisponibilityEntity> disponibilityEntities) {
		if (CollectionUtils.isEmpty(disponibilities)) {
			disponibilities = new ArrayList<>();
			disponibilities.addAll(disponibilityEntities);
			disponibilityEntities.forEach(item -> item.room(this));
		} else {
			disponibilities.forEach(item -> item.room(this));
		}
	}

	public void addDisponibility(DisponibilityEntity disponibilityEntity) {
		if (CollectionUtils.isEmpty(disponibilities)) {
			disponibilities = new ArrayList<>();
		}
		disponibilities.add(disponibilityEntity);
		disponibilityEntity.room(this);
	}

	public void removeDisponibilities(List<DisponibilityEntity> disponibilityEntities) {
		disponibilities.removeAll(disponibilityEntities);
		disponibilityEntities.forEach(item -> item.room(null));
	}

	public void removeDisponibility(DisponibilityEntity disponibilityEntity) {
		disponibilities.remove(disponibilityEntity);
		disponibilityEntity.room(null);
	}

	public String getClassRoomId() {
		return classRoomId;
	}

	public RoomEntity classRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public RoomEntity label(String label) {
		this.label = label;
		return this;
	}

	public int getCapacity() {
		return capacity;
	}

	public RoomEntity capacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	public String getPole() {
		return pole;
	}

	public RoomEntity pole(String pole) {
		this.pole = pole;
		return this;
	}

	public String getBloc() {
		return bloc;
	}

	public RoomEntity bloc(String bloc) {
		this.bloc = bloc;
		return this;
	}

	public List<DisponibilityEntity> getDisponibilities() {
		return disponibilities;
	}

	public RoomEntity disponibilities(List<DisponibilityEntity> disponibilities) {
		this.disponibilities = disponibilities;
		return this;
	}

	public List<GroupEntity> getRooms() {
		return rooms;
	}

	public RoomEntity rooms(List<GroupEntity> rooms) {
		this.rooms = rooms;
		return this;
	}

	public Set<String> getExamIds() {
		return examIds;
	}

	public RoomEntity examIds(Set<String> examIds) {
		this.examIds = examIds;
		return this;
	}

}