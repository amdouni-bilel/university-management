package com.esprit.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esprit.domain.SessionEntity;
import com.esprit.service.SessionService;

@RestController
@RequestMapping("session")
public class SessionController {

	private final SessionService service;

	public SessionController(SessionService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Void> createSpeciality(@RequestBody @Valid SessionEntity sessionEntity) {
		service.updateSemester(sessionEntity);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}