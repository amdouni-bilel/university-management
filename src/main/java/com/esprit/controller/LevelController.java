package com.esprit.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esprit.domain.LevelEntity;
import com.esprit.service.LevelService;

@RestController
@RequestMapping("level")
public class LevelController {

	private final LevelService service;

	public LevelController(LevelService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Void> createLevel(@RequestBody @Valid LevelEntity levelEntity) {
		service.updateLevel(levelEntity);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<LevelEntity>> findLevels() {
		return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

	@DeleteMapping("{levelId}")
	public ResponseEntity<Void> deleteLevel(@PathVariable String levelId) {
		service.deleteLevel(levelId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}