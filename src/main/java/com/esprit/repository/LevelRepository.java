package com.esprit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esprit.domain.LevelEntity;

public interface LevelRepository extends JpaRepository<LevelEntity, String> {

}