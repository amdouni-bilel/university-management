package com.esprit.service;

import java.util.List;

import com.esprit.domain.ExamEntity;
import com.esprit.dto.exam.CreateExamRequest;
import com.esprit.dto.exam.DeleteExamDTO;
import com.esprit.dto.exam.ExamInfoDTO;
import com.esprit.dto.exam.UpdateExamRequest;

public interface ExamService {

	void addExam(CreateExamRequest createExamRequest);

	void updateExam(UpdateExamRequest updateExamRequest);

	void deleteExam(DeleteExamDTO deleteExamDTO);

	void deleteExamById(String examId);

	List<ExamEntity> findExams();

	List<String> findExamsBySession(String session);
	
	List<ExamInfoDTO> getExamInfoByClass(String classId);

}