package com.esprit.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Groupe")
public class GroupEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "group-uuid")
	@GenericGenerator(name = "group-uuid", strategy = "uuid2")
	private String groupId;

	private String label;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "SUPERVISION_TEACHER", joinColumns = { @JoinColumn(name = "fk_group") }, inverseJoinColumns = {
			@JoinColumn(name = "fk_teacher") })
	private List<TeacherEntity> teachers;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "SUPERVISION_ROOM", joinColumns = { @JoinColumn(name = "fk_group") }, inverseJoinColumns = {
			@JoinColumn(name = "fk_room") })
	private List<RoomEntity> rooms;

	@JsonIgnore
	@ManyToMany(mappedBy = "groups")
	private List<ClassEntity> classes = new ArrayList<>();

	public void addTeacher(TeacherEntity teacherEntity) {
		if (CollectionUtils.isEmpty(teachers)) {
			teachers = new ArrayList<>();
		}
		teachers.add(teacherEntity);
		teacherEntity.getGroups().add(this);
	}

	public void addTeachers(List<TeacherEntity> teacherEntities) {
		if (CollectionUtils.isEmpty(teachers)) {
			teachers = new ArrayList<>();
		}
		teachers.addAll(teacherEntities);
		teacherEntities.forEach(item -> item.getGroups().add(this));
	}

	public void removeTeacherEntity(TeacherEntity teacherEntity) {
		teachers.remove(teacherEntity);
		teacherEntity.getGroups().remove(this);
	}

	public void addRoom(RoomEntity roomEntity) {
		if (CollectionUtils.isEmpty(rooms)) {
			rooms = new ArrayList<>();
		}
		rooms.add(roomEntity);
		roomEntity.getRooms().add(this);
	}

	public void addRooms(List<RoomEntity> roomEntities) {
		if (CollectionUtils.isEmpty(rooms)) {
			rooms = new ArrayList<>();
		}
		rooms.addAll(roomEntities);
		roomEntities.forEach(item -> item.getRooms().add(this));
	}

	public void removeRoomEntity(RoomEntity roomEntity) {
		rooms.remove(roomEntity);
		roomEntity.getRooms().remove(this);
	}

	public String getGroupId() {
		return groupId;
	}

	public GroupEntity groupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public GroupEntity label(String label) {
		this.label = label;
		return this;
	}

	public List<TeacherEntity> getTeachers() {
		return teachers;
	}

	public GroupEntity teachers(List<TeacherEntity> teachers) {
		this.teachers = teachers;
		return this;
	}

	public List<RoomEntity> getRooms() {
		return rooms;
	}

	public GroupEntity rooms(List<RoomEntity> rooms) {
		this.rooms = rooms;
		return this;
	}

	public List<ClassEntity> getClasses() {
		return classes;
	}

	public GroupEntity classes(List<ClassEntity> classes) {
		this.classes = classes;
		return this;
	}

}