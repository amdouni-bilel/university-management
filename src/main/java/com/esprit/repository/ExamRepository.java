package com.esprit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.esprit.domain.ExamEntity;
import com.esprit.dto.exam.ExamInfoDTO;

public interface ExamRepository extends JpaRepository<ExamEntity, String> {

	@Query(value = "select * from EPREUVE pv LEFT JOIN SESSIONENTITY s ON pv.FK_SESSION = s.SESSIONID WHERE s.LABEL = ?1", nativeQuery = true)
	List<ExamEntity> findBySession(String session);

	@Query("select new com.esprit.dto.exam.ExamInfoDTO(pv.examDate, pv.examHour, pv.examDuration) from ExamEntity pv inner join pv.classes pvc where pvc.classId = ?1")
	List<ExamInfoDTO> getExamInfoByClass(String classId);

}