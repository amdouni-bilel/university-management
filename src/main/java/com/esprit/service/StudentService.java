package com.esprit.service;

import java.security.Principal;
import java.util.List;

import org.mapstruct.Named;

import com.esprit.dto.student.CreateStudentRequest;
import com.esprit.dto.student.StudentDTO;

public interface StudentService {

	void addStudent(CreateStudentRequest createStudentRequest);

	void addAllStudent(List<CreateStudentRequest> createStudentRequest);

	void updateStudent(CreateStudentRequest createStudentRequest);

	void deleteStudent(String studentId);

	StudentDTO findByPrincipal(Principal principal);

	List<StudentDTO> findAll();

	List<StudentDTO> findByClass(String classe);

	@Named("countByClass")
	int countByClass(String classe);
	
	int countByClassAndGroup(String classe, String group);

}