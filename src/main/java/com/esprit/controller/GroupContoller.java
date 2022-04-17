package com.esprit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esprit.dto.GroupDTO;
import com.esprit.service.GroupService;

@RestController
@RequestMapping("group")
public class GroupContoller {

	private final GroupService service;

	public GroupContoller(GroupService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<GroupDTO>> findAllGroup() {
		return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

}