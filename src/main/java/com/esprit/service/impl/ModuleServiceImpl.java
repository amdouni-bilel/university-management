package com.esprit.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.esprit.domain.AssignClassModuleEntity;
import com.esprit.domain.ModuleEntity;
import com.esprit.dto.module.CreateAssignClassRequest;
import com.esprit.dto.module.CreateModuleRequest;
import com.esprit.dto.module.ModuleDTO;
import com.esprit.dto.module.UpdateAssignClassRequest;
import com.esprit.error.exception.EntityAlreadyExistsExeption;
import com.esprit.error.exception.EntityNotFoundException;
import com.esprit.repository.AssignClassModuleRepository;
import com.esprit.repository.ClassRepository;
import com.esprit.repository.ModuleRepository;
import com.esprit.repository.PeriodRepository;
import com.esprit.repository.SemesterRepository;
import com.esprit.repository.TeacherRepository;
import com.esprit.service.ModuleService;
import com.esprit.service.mapper.AssignClassModuleMapper;
import com.esprit.service.mapper.ModuleMapper;

@Service
public class ModuleServiceImpl implements ModuleService {

	private final ModuleRepository moduleRepository;

	private final TeacherRepository teacherRepository;

	private final ClassRepository classRepository;

	private final SemesterRepository semesterRepository;

	private final PeriodRepository periodRepository;

	private final AssignClassModuleRepository assignClassModuleRepository;

	private final ModuleMapper mapper;

	private final AssignClassModuleMapper assignClassModuleMapper;

	public ModuleServiceImpl(ModuleRepository moduleRepository, TeacherRepository teacherRepository,
			ClassRepository classRepository, SemesterRepository semesterRepository, PeriodRepository periodRepository,
			AssignClassModuleRepository assignClassModuleRepository, ModuleMapper mapper,
			AssignClassModuleMapper assignClassModuleMapper) {
		this.moduleRepository = moduleRepository;
		this.teacherRepository = teacherRepository;
		this.classRepository = classRepository;
		this.semesterRepository = semesterRepository;
		this.periodRepository = periodRepository;
		this.assignClassModuleRepository = assignClassModuleRepository;
		this.mapper = mapper;
		this.assignClassModuleMapper = assignClassModuleMapper;
	}

	@Override
	public void addModule(CreateModuleRequest createModuleRequest) {
		ModuleEntity moduleEntity = mapper.createModuleRequestToModuleEntity(createModuleRequest, teacherRepository,
				classRepository, semesterRepository, periodRepository);
		if (CollectionUtils.isNotEmpty(moduleEntity.getAssignClasses())) {
			moduleEntity.addAssignClasses(moduleEntity.getAssignClasses());
		}
		if (moduleRepository.findById(createModuleRequest.getModuleId()).isPresent()) {
			throw new EntityAlreadyExistsExeption(ModuleEntity.class, "module", createModuleRequest.getModuleId());
		}
		moduleRepository.save(moduleEntity);
	}

	@Override
	public void deleteModule(String moduleId) {
		Optional<ModuleEntity> moduleEntityOptional = moduleRepository.findById(moduleId);
		if (moduleEntityOptional.isPresent()) {
			ModuleEntity moduleEntity = moduleEntityOptional.get();
			/*for (AssignClassModuleEntity assign : moduleEntity.getAssignClasses()) {
				assignClassModuleRepository.deleteById(assign.getAssignId());
			}*/
			//moduleEntity.removeAssignClasses(moduleEntity.getAssignClasses());
			moduleRepository.deleteById(moduleEntity.getModuleId());
		} else {
			throw new EntityNotFoundException(ModuleEntity.class, "id", moduleId);
		}
	}

	@Override
	public ModuleDTO findModule(String moduleId) {
		ModuleDTO result = null;
		Optional<ModuleEntity> moduleEntityOptional = moduleRepository.findById(moduleId);
		if (moduleEntityOptional.isPresent()) {
			result = mapper.moduleEntityToModuleDTO(moduleEntityOptional.get());
		} else {
			throw new EntityNotFoundException(ModuleEntity.class, "id", moduleId);
		}
		return result;
	}

	@Override
	public Map<String, Object> findModules(Pageable paging) {
		Page<ModuleEntity> moduleEntities = moduleRepository.findAll(paging);
		Map<String, Object> response = new HashMap<>();
		response.put("body", moduleEntities);
		response.put("currentPage", moduleEntities.getNumber());
		response.put("totalItems", moduleEntities.getTotalElements());
		response.put("totalPages", moduleEntities.getTotalPages());
		return response;
	}

	@Override
	public Map<String, Object> findModulesWithFilter(String filter, Pageable paging) {
		Page<ModuleEntity> moduleEntities = moduleRepository.findAllWithFilter(filter, paging);
		Map<String, Object> response = new HashMap<>();
		response.put("body", moduleEntities);
		response.put("currentPage", moduleEntities.getNumber());
		response.put("totalItems", moduleEntities.getTotalElements());
		response.put("totalPages", moduleEntities.getTotalPages());
		return response;
	}

	@Override
	public List<ModuleDTO> findModulesByClass(String classId) {
		return null;
	}

	@Override
	public List<ModuleDTO> findAllWithoutAssignClasses() {
		return mapper.moduleWithoutAssignClassesDTOsToModuleResponse(moduleRepository.findAllWithoutAssignClasses());
	}

	@Override
	public ModuleEntity findModuleById(String moduleId) {
		ModuleEntity result = null;
		if (!StringUtils.isBlank(moduleId)) {
			Optional<ModuleEntity> moduleEntityOptional = moduleRepository.findById(moduleId);
			if (moduleEntityOptional.isPresent()) {
				result = moduleEntityOptional.get();
			}
		}
		return result;
	}

	@Override
	public List<ModuleEntity> findModuleWithClassList(Set<String> levels) {
		return moduleRepository.findByLavels(levels);
	}

	@Override
	public void addAssignClass(CreateAssignClassRequest createAssignClassRequest) {
		Optional<ModuleEntity> moduleEntity = moduleRepository.findById(createAssignClassRequest.getModuleId());
		if (moduleRepository.findById(createAssignClassRequest.getModuleId()).isPresent()) {
			AssignClassModuleEntity assignClassModuleEntity = assignClassModuleMapper
					.createAssignClassRequestToAssignClassModuleEntity(createAssignClassRequest);
			assignClassModuleEntity.module(moduleEntity.get());
			assignClassModuleRepository.save(assignClassModuleEntity);
		}
	}

	@Override
	public void deleteAssignClass(String assignId) {
		Optional<AssignClassModuleEntity> assignClassModuleEntity = assignClassModuleRepository.findById(assignId);
		if (assignClassModuleEntity.isPresent()) {
			assignClassModuleRepository.deleteById(assignId);
		}
	}

	@Override
	public void updateAssignClass(UpdateAssignClassRequest updateAssignClassRequest) {
		Optional<AssignClassModuleEntity> assignClassModuleEntityOptional = assignClassModuleRepository
				.findById(updateAssignClassRequest.getAssignId());
		if (assignClassModuleEntityOptional.isPresent()) {
			AssignClassModuleEntity assignClassModuleEntity = assignClassModuleMapper
					.updateAssignClassRequestToAssignClassModuleEntity(updateAssignClassRequest);
			assignClassModuleEntity.module(assignClassModuleEntityOptional.get().getModule());
			assignClassModuleRepository.save(assignClassModuleEntity);
		}

	}

}