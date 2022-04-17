package com.esprit.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.esprit.domain.ExamEntity;
import com.esprit.dto.exam.CreateExamRequest;
import com.esprit.service.ClassService;
import com.esprit.service.ModuleService;
import com.esprit.service.SessionService;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { ModuleService.class, ClassService.class,
		SessionService.class })
public interface ExamMapper {

	ExamMapper INSTANCE = Mappers.getMapper(ExamMapper.class);

	@Mapping(source = "module", target = "module", qualifiedByName = "findModuleById")
	@Mapping(source = "classes", target = "classes", qualifiedByName = "findClassesByIds")
	ExamEntity createExamRequestToExamEntity(CreateExamRequest createExamRequest);

}