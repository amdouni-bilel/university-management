package com.esprit.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esprit.domain.ModuleEntity;
import com.esprit.dto.module.CreateAssignClassRequest;
import com.esprit.dto.module.CreateModuleRequest;
import com.esprit.dto.module.ModuleDTO;
import com.esprit.dto.module.UpdateAssignClassRequest;
import com.esprit.service.ModuleService;

@RestController
@RequestMapping("module")
public class ModuleController {

	private final ModuleService service;

	public ModuleController(ModuleService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Void> createModule(@RequestBody @Valid CreateModuleRequest createModuleRequest) {
		service.addModule(createModuleRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("assign")
	public ResponseEntity<Void> createAssignClass(
			@RequestBody @Valid CreateAssignClassRequest createAssignClassRequest) {
		service.addAssignClass(createAssignClassRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping("assign")
	public ResponseEntity<Void> updateAssignClass(
			@RequestBody @Valid UpdateAssignClassRequest updateAssignClassRequest) {
		service.updateAssignClass(updateAssignClassRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@DeleteMapping("assign/{assignId}")
	public ResponseEntity<Void> deleteAssignClass(@PathVariable String assignId) {
		service.deleteAssignClass(assignId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("{moduleId}")
	public ResponseEntity<Void> deleteModule(@PathVariable String moduleId) {
		service.deleteModule(moduleId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("{moduleId}")
	public ResponseEntity<ModuleDTO> findModule(@PathVariable String moduleId) {
		return new ResponseEntity<>(service.findModule(moduleId), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> findModules(@RequestParam(defaultValue = "0") int page,
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
			result = service.findModules(PageRequest.of(page, size, sort));
		} else if (!StringUtils.isBlank(filter)) {
			result = service.findModulesWithFilter(filter, PageRequest.of(page, size));
		} else if (!StringUtils.isBlank(filter) && !StringUtils.isBlank(sortField) && !StringUtils.isBlank(sortField)) {
			result = service.findModules(PageRequest.of(page, size));
		} else {
			result = service.findModules(PageRequest.of(page, size));
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("with-classes")
	public ResponseEntity<List<ModuleEntity>> findModuleWithClassList(@RequestBody Set<String> levels) {
		return new ResponseEntity<>(service.findModuleWithClassList(levels), HttpStatus.OK);
	}

	@GetMapping("without-classes")
	public ResponseEntity<List<ModuleDTO>> findAllWithoutAssignClasses() {
		return new ResponseEntity<>(service.findAllWithoutAssignClasses(), HttpStatus.OK);
	}

	@GetMapping("classId/{classId}")
	public ResponseEntity<List<ModuleDTO>> findModulesByClass(@PathVariable String classId) {
		return new ResponseEntity<>(service.findModulesByClass(classId), HttpStatus.OK);
	}

}