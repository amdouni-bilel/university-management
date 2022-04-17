package com.esprit.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class SessionEntity {

	@Id
	@GeneratedValue(generator = "session-uuid")
	@GenericGenerator(name = "session-uuid", strategy = "uuid2")
	private String sessionId;

	private String label;

	private Date startDate;

	private Date endDate;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_level")
	private LevelEntity level;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLabel() {
		return label;
	}

	public SessionEntity label(String label) {
		this.label = label;
		return this;
	}

	public Date getStartDate() {
		return startDate;
	}

	public SessionEntity startDate(Date startDate) {
		this.startDate = startDate;
		return this;
	}

	public Date getEndDate() {
		return endDate;
	}

	public SessionEntity endDate(Date endDate) {
		this.endDate = endDate;
		return this;
	}

	public LevelEntity getLevel() {
		return level;
	}

	public SessionEntity level(LevelEntity level) {
		this.level = level;
		return this;
	}

}