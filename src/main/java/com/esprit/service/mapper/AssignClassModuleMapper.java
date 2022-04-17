package com.esprit.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.esprit.domain.AssignClassModuleEntity;
import com.esprit.dto.module.AssignClassModuleDTO;
import com.esprit.dto.module.CreateAssignClassRequest;
import com.esprit.dto.module.UpdateAssignClassRequest;
import com.esprit.service.ClassService;
import com.esprit.service.PeriodService;
import com.esprit.service.SemesterService;
import com.esprit.service.TeacherService;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { SemesterMapper.class, PeriodMapper.class,
		TeacherService.class, PeriodService.class, SemesterService.class, ClassService.class })
public interface AssignClassModuleMapper {

	AssignClassModuleMapper INSTANCE = Mappers.getMapper(AssignClassModuleMapper.class);

	@Mapping(source = "semesterId", target = "semester", qualifiedByName = "findSemester")
	@Mapping(source = "periodId", target = "periods", qualifiedByName = "findPeriods")
	@Mapping(source = "classId", target = "classs", qualifiedByName = "findClass")
	@Mapping(source = "teacherIds", target = "teachers", qualifiedByName = "findTeachersByIds")
	AssignClassModuleEntity createAssignClassRequestToAssignClassModuleEntity(
			CreateAssignClassRequest createAssignClassRequest);

	@Mapping(source = "semesterId", target = "semester", qualifiedByName = "findSemester")
	@Mapping(source = "periodId", target = "periods", qualifiedByName = "findPeriods")
	@Mapping(source = "classId", target = "classs", qualifiedByName = "findClass")
	@Mapping(source = "teacherIds", target = "teachers", qualifiedByName = "findTeachersByIds")
	AssignClassModuleEntity updateAssignClassRequestToAssignClassModuleEntity(
			UpdateAssignClassRequest updateAssignClassRequest);

	AssignClassModuleDTO assignClassModuleEntityAssignClassModuleDTO(AssignClassModuleEntity assignClassModuleEntity);

	List<AssignClassModuleDTO> assignClassModuleAssignClassModuleDTO(
			List<AssignClassModuleEntity> assignClassModuleEntities);

}