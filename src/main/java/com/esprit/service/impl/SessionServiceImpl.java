package com.esprit.service.impl;

import org.springframework.stereotype.Service;

import com.esprit.domain.SessionEntity;
import com.esprit.repository.SessionRepository;
import com.esprit.service.SessionService;

@Service
public class SessionServiceImpl implements SessionService {

	private final SessionRepository sessionRepository;

	public SessionServiceImpl(SessionRepository sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	@Override
	public SessionEntity findSessionById(String sessionId) {
		return sessionRepository.getOne(sessionId);
	}

	@Override
	public void updateSemester(SessionEntity sessionEntity) {
		sessionRepository.save(sessionEntity);
	}

}