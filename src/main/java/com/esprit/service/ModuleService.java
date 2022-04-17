package com.esprit.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mapstruct.Named;
import org.springframework.data.domain.Pageable;

import com.esprit.domain.ModuleEntity;
import com.esprit.dto.module.CreateAssignClassRequest;
import com.esprit.dto.module.CreateModuleRequest;
import com.esprit.dto.module.ModuleDTO;
import com.esprit.dto.module.UpdateAssignClassRequest;

public interface ModuleService {

	void addModule(CreateModuleRequest createModuleRequest);

	void addAssignClass(CreateAssignClassRequest createAssignClassRequest);

	void deleteModule(String moduleId);

	void deleteAssignClass(String assignId);

	ModuleDTO findModule(String moduleId);

	void updateAssignClass(UpdateAssignClassRequest updateAssignClassRequest);

	@Named("findModuleById")
	ModuleEntity findModuleById(String moduleId);

	Map<String, Object> findModules(Pageable paging);

	Map<String, Object> findModulesWithFilter(String filter, Pageable paging);

	List<ModuleDTO> findAllWithoutAssignClasses();

	List<ModuleEntity> findModuleWithClassList(Set<String> levels);

	List<ModuleDTO> findModulesByClass(String classId);

}