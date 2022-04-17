package com.esprit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esprit.domain.SessionEntity;

public interface SessionRepository extends JpaRepository<SessionEntity, String> {

}