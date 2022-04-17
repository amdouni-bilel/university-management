package com.esprit.service.mapper;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.esprit.domain.ClassEntity;
import com.esprit.domain.StudentEntity;
import com.esprit.dto.classes.ClassDTO;
import com.esprit.dto.student.CreateStudentRequest;
import com.esprit.dto.student.StudentDTO;
import com.esprit.repository.ClassRepository;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

	StudentEntity createStudentRequestToStudentEntity(CreateStudentRequest createStudentRequest,
			@Context ClassRepository classRepository);

	List<StudentEntity> createStudentRequestToStudentEntities(List<CreateStudentRequest> createStudentRequest,
			@Context ClassRepository classRepository);

	StudentDTO studentEntityToStudentDTO(StudentEntity studentEntity);

	ClassDTO classEntityToClassDTO(ClassEntity classEntity);

	List<StudentDTO> studentEntitiesToStudentDTO(List<StudentEntity> studentEntity);

	@AfterMapping
	static void after(CreateStudentRequest source, @MappingTarget StudentEntity target,
			@Context ClassRepository classRepository) {
		target.classs(classRepository.getOne(source.getClassId()));
	}

}