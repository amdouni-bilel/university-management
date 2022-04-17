package com.esprit.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CLASSE")
public class ClassEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String classId;

	private String label;

	private String category;

	private String email;

	@Column(name = "ISGROUPED")
	private boolean group = false;

	@ManyToOne(optional = true)
	@JoinColumn(name = "fk_speciality")
	private SpecialityEntity speciality;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "CLASSE_GROUP", joinColumns = @JoinColumn(name = "fk_class"), inverseJoinColumns = @JoinColumn(name = "fk_group"))
	private List<GroupEntity> groups;

	@JsonIgnore
	@ManyToMany(mappedBy = "classes")
	private List<ExamEntity> examEntities = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "classs", cascade = CascadeType.ALL)
	private List<AssignClassModuleEntity> assignClassModules;

	public void addGroup(GroupEntity groupEntity) {
		if (CollectionUtils.isEmpty(groups)) {
			groups = new ArrayList<>();
		}
		groups.add(groupEntity);
		groupEntity.getClasses().add(this);
	}

	public void addGroups(List<GroupEntity> groupEntities) {
		if (CollectionUtils.isEmpty(groups)) {
			groups = new ArrayList<>();
		}
		groups.addAll(groupEntities);
		groupEntities.forEach(item -> item.getClasses().add(this));
	}

	public void removeGroup(GroupEntity groupEntity) {
		groups.remove(groupEntity);
		groupEntity.getClasses().add(null);
	}

	public String getClassId() {
		return classId;
	}

	public ClassEntity classId(String classId) {
		this.classId = classId;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public ClassEntity label(String label) {
		this.label = label;
		return this;
	}

	public SpecialityEntity getSpeciality() {
		return speciality;
	}

	public ClassEntity speciality(SpecialityEntity speciality) {
		this.speciality = speciality;
		return this;
	}

	public String getCategory() {
		return category;
	}

	public ClassEntity category(String category) {
		this.category = category;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public ClassEntity email(String email) {
		this.email = email;
		return this;
	}

	public List<ExamEntity> getExamEntities() {
		return examEntities;
	}

	public ClassEntity examEntities(List<ExamEntity> examEntities) {
		this.examEntities = examEntities;
		return this;
	}

	public List<GroupEntity> getGroups() {
		return groups;
	}

	public ClassEntity groups(List<GroupEntity> groups) {
		this.groups = groups;
		return this;
	}

	public boolean isGroup() {
		return group;
	}

	public ClassEntity group(boolean group) {
		this.group = group;
		return this;
	}

	public List<AssignClassModuleEntity> getAssignClassModules() {
		return assignClassModules;
	}

	public ClassEntity assignClassModules(List<AssignClassModuleEntity> assignClassModules) {
		this.assignClassModules = assignClassModules;
		return this;
	}

}