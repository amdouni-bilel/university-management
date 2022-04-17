package com.esprit.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class LevelEntity {

	@Id
	@GeneratedValue(generator = "level-uuid")
	@GenericGenerator(name = "level-uuid", strategy = "uuid2")
	private String id;

	private String label;

	@OneToMany(mappedBy = "level", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SessionEntity> session;

	public void addSession(List<SessionEntity> sessionEntities) {
		if (CollectionUtils.isEmpty(session)) {
			session = new ArrayList<>();
			session.addAll(sessionEntities);
			sessionEntities.forEach(item -> item.level(this));
		} else {
			session.forEach(item -> item.level(this));
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<SessionEntity> getSession() {
		return session;
	}

	public void setSession(List<SessionEntity> session) {
		this.session = session;
	}

}