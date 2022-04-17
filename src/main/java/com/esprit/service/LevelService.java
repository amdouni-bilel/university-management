package com.esprit.service;

import java.util.List;

import com.esprit.domain.LevelEntity;

public interface LevelService {

	void updateLevel(LevelEntity levelEntity);

	void deleteLevel(String levelId);

	List<LevelEntity> findAll();

}