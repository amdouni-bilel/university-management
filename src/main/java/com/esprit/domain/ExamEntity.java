package com.esprit.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "EPREUVE")
public class ExamEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String examId;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date examDate;

	private int examHour;

	private int endHour;

	private double examDuration;

	@ManyToOne(optional = false)
	@JoinColumn(name = "fk_session")
	private SessionEntity session;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "fk_module")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private ModuleEntity module;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "EPREUVE_CLASSE", joinColumns = @JoinColumn(name = "fk_exam"), inverseJoinColumns = @JoinColumn(name = "fk_class"))
	private List<ClassEntity> classes;

	public void addClassEntity(ClassEntity classEntity) {
		if (CollectionUtils.isEmpty(classes)) {
			classes = new ArrayList<>();
		}
		classes.add(classEntity);
		List<ExamEntity> examEntities = classEntity.getExamEntities();
		examEntities.add(this);
	}

	public void removeClassEntity(ClassEntity classEntity) {
		classes.remove(classEntity);
		classEntity.getExamEntities().remove(this);
	}

	public String getExamId() {
		return examId;
	}

	public ExamEntity examId(String examId) {
		this.examId = examId;
		return this;
	}

	public SessionEntity getSession() {
		return session;
	}

	public ExamEntity session(SessionEntity session) {
		this.session = session;
		return this;
	}

	public Date getExamDate() {
		return examDate;
	}

	public ExamEntity examDate(Date examDate) {
		this.examDate = examDate;
		return this;
	}

	public int getExamHour() {
		return examHour;
	}

	public ExamEntity examHour(int examHour) {
		this.examHour = examHour;
		return this;
	}

	public double getExamDuration() {
		return examDuration;
	}

	public ExamEntity examDuration(double examDuration) {
		this.examDuration = examDuration;
		return this;
	}

	public ModuleEntity getModule() {
		return module;
	}

	public ExamEntity module(ModuleEntity module) {
		this.module = module;
		return this;
	}

	public List<ClassEntity> getClasses() {
		return classes;
	}

	public ExamEntity classes(List<ClassEntity> classes) {
		this.classes = classes;
		return this;
	}

	public int getEndHour() {
		return endHour;
	}

	public ExamEntity endHour(int endHour) {
		this.endHour = endHour;
		return this;
	}

}