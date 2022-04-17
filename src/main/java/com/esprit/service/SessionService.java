package com.esprit.service;

import org.mapstruct.Named;

import com.esprit.domain.SessionEntity;

public interface SessionService {

	void updateSemester(SessionEntity sessionEntity);

	@Named("findSession")
	SessionEntity findSessionById(String sessionId);

}