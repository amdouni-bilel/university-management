package com.esprit.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esprit.dto.teacher.CreateTeacherRequest;
import com.esprit.dto.teacher.FilterAvailableTeacherDTO;
import com.esprit.dto.teacher.SearchTeacherDTO;
import com.esprit.dto.teacher.TeacherDTO;
import com.esprit.dto.teacher.TeacherWithoutDisponibilityDTO;
import com.esprit.service.TeacherService;

@RestController
@RequestMapping("teacher")
public class TeacherController {

	private final TeacherService service;

	public TeacherController(TeacherService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Void> createTeacher(@RequestBody @Valid CreateTeacherRequest createTeacherRequest) {
		service.addTeacher(createTeacherRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("all")
	public ResponseEntity<Void> createAllTeacher(@RequestBody @Valid List<CreateTeacherRequest> createTeacherRequests) {
		createTeacherRequests.forEach(item -> service.addTeacher(item));
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping("{userId}")
	public ResponseEntity<TeacherDTO> findTeacher(@PathVariable String userId) {
		return new ResponseEntity<>(service.findBydId(userId), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<TeacherDTO>> findTeachers() {
		return new ResponseEntity<>(service.findTeachers(), HttpStatus.OK);
	}

	@GetMapping("without-disponibilities")
	public ResponseEntity<List<TeacherWithoutDisponibilityDTO>> findTeachersWithoutDisponibilities() {
		return new ResponseEntity<>(service.findTeachersWithoutDisponibilities(), HttpStatus.OK);
	}

	@GetMapping("/current-teacher")
	public ResponseEntity<TeacherDTO> findCurrentStudent(Principal principal) {
		return new ResponseEntity<>(service.findByPrincipal(principal), HttpStatus.OK);
	}

	@GetMapping("disponibilities")
	public ResponseEntity<Map<String, Object>> findTeachersWithDisponibility(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int size, @RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortType, @RequestParam(required = false) String filter) {
		Map<String, Object> result;
		if (!StringUtils.isBlank(sortField) && !StringUtils.isBlank(sortField)) {
			Sort sort = null;
			if (sortType.equals("ASC")) {
				sort = Sort.by(sortField).ascending();
			} else if (sortType.equals("DESC")) {
				sort = Sort.by(sortField).descending();
			}
			result = service.findTeachersWithDisponibility(PageRequest.of(page, size, sort));
		} else if (!StringUtils.isBlank(filter)) {
			result = service.findTeachersWithDisponibilityWithFilter(filter, PageRequest.of(page, size));
		} else if (!StringUtils.isBlank(filter) && !StringUtils.isBlank(sortField) && !StringUtils.isBlank(sortField)) {
			result = service.findTeachersWithDisponibility(PageRequest.of(page, size));
		} else {
			result = service.findTeachersWithDisponibility(PageRequest.of(page, size));
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("up")
	public ResponseEntity<List<String>> findUpByTeachers(@RequestBody List<String> teachers) {
		return new ResponseEntity<>(service.findUpByTeachers(teachers), HttpStatus.OK);
	}

	@PostMapping("ups")
	public ResponseEntity<List<TeacherDTO>> findAllByUps(
			@RequestBody FilterAvailableTeacherDTO filterAvailableTeacherDTO) {
		return new ResponseEntity<>(service.findAllByUps(filterAvailableTeacherDTO), HttpStatus.OK);
	}

	@PostMapping("search")
	public ResponseEntity<List<TeacherDTO>> searcheachers(@RequestBody SearchTeacherDTO searchTeacherDTO) {
		return new ResponseEntity<>(service.searcheachers(searchTeacherDTO.getTeacherId(), searchTeacherDTO.getCin(),
				searchTeacherDTO.getDepartement(), searchTeacherDTO.getClasss()), HttpStatus.OK);
	}

}