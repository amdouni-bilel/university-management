package com.esprit.service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mapstruct.Named;
import org.springframework.data.domain.Pageable;

import com.esprit.domain.TeacherEntity;
import com.esprit.dto.teacher.CreateTeacherRequest;
import com.esprit.dto.teacher.FilterAvailableTeacherDTO;
import com.esprit.dto.teacher.TeacherDTO;
import com.esprit.dto.teacher.TeacherWithoutDisponibilityDTO;

public interface TeacherService {

	void addTeacher(CreateTeacherRequest createTeacherRequest);

	TeacherDTO findBydId(String userId);

	@Named("findTeachersByIds")
	List<TeacherEntity> findTeachersByIds(List<String> teachers);

	List<TeacherDTO> findTeachers();

	TeacherDTO findByPrincipal(Principal principal);

	List<TeacherDTO> findAllByUps(FilterAvailableTeacherDTO filterAvailableTeacherDTO);

	List<TeacherDTO> searcheachers(String teacherId, int cin, String departement, String classs);

	void addDisponibilityToTeacher(String teacherId, Date supervisionDate, int supervisionHour,
			int supervisionDuration);

	List<String> findUpByTeachers(List<String> teachers);

	Map<String, Object> findTeachersWithDisponibility(Pageable paging);

	Map<String, Object> findTeachersWithDisponibilityWithFilter(String filter, Pageable paging);
	
	List<TeacherWithoutDisponibilityDTO> findTeachersWithoutDisponibilities();

}