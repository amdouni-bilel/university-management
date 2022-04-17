package com.esprit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.esprit.domain.LevelEntity;
import com.esprit.repository.LevelRepository;
import com.esprit.service.LevelService;

@Service
public class LevelServiceImpl implements LevelService {

	private final LevelRepository repository;

	public LevelServiceImpl(LevelRepository repository) {
		this.repository = repository;
	}

	@Override
	public void updateLevel(LevelEntity levelEntity) {
		LevelEntity level = new LevelEntity();
		level.setLabel(levelEntity.getLabel());
		level.addSession(levelEntity.getSession());
		repository.save(level);
	}

	@Override
	public List<LevelEntity> findAll() {
		return repository.findAll();
	}

	@Override
	public void deleteLevel(String levelId) {
		repository.deleteById(levelId);
	}

}