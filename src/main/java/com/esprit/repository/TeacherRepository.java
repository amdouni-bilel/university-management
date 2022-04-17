package com.esprit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.esprit.domain.TeacherEntity;
import com.esprit.dto.teacher.DisponibilityTeachetDTO;
import com.esprit.dto.teacher.TeacherWithoutDisponibilityDTO;
import com.esprit.repository.search.TeacherRepositoryCustom;

public interface TeacherRepository extends JpaRepository<TeacherEntity, String>, TeacherRepositoryCustom {

	List<TeacherEntity> findAllByOrderByFullNameAsc();
	TeacherEntity findByEmail(String email);

	public static String query = "SELECT * FROM ESP_ENSEIGNANT es where es.teacherId NOT IN\n"
			+ "(SELECT disp.fk_room FROM DISPONIBILITY disp where ?1 BETWEEN disp.STARTDATE and disp.ENDDATE and ?2 >= disp.startHour and ?2 < disp.endHour )\n"
			+ "AND es.up IN ?3 AND es.type = 'P'";

	@Query(nativeQuery = true, value = "SELECT * FROM ESP_ENSEIGNANT es where LOWER(es.up) IN ?1 AND es.type = 'P'")
	List<TeacherEntity> findAllByUps(List<String> ups);

	@Query("SELECT new com.esprit.dto.teacher.TeacherWithoutDisponibilityDTO(t.teacherId, t.fullName) FROM TeacherEntity as t")
	List<TeacherWithoutDisponibilityDTO> findTeachersWithoutDisponibilities();

	List<TeacherEntity> findByTeacherIdIn(List<String> teacherIds);

	@Query(nativeQuery = true, value = "SELECT * FROM ESP_ENSEIGNANT es JOIN TEACHERENTITY_EXAMIDS tex ON tex.TEACHERENTITY_TEACHERID = es.TEACHERID where es.teacherId IN ?1 AND ?2 = tex.EXAMIDS")
	List<TeacherEntity> findByTeacherIdsAndExamId(List<String> teacherIds, String examId);

	@Query(nativeQuery = true, value = "select DISTINCT up from esp_enseignant where teacherId IN ?1")
	List<String> findUpByTeachers(List<String> teachers);

	@Query("select "
			+ "new com.esprit.dto.teacher.DisponibilityTeachetDTO(es.teacherId, es.fullName, d.disponibilityId, d.startDate, d.endDate, d.startHour, d.endHour, d.motif, d.commentd) "
			+ "from TeacherEntity es LEFT JOIN es.disponibilities d WHERE d.disponibilityId IS NOT NULL")
	Page<DisponibilityTeachetDTO> findTeachersWithDisponibilityWithPage(Pageable pageable);

	@Query("select "
			+ "new com.esprit.dto.teacher.DisponibilityTeachetDTO(es.teacherId, es.fullName, d.disponibilityId, d.startDate, d.endDate, d.startHour, d.endHour, d.motif, d.commentd) "
			+ "from TeacherEntity es LEFT JOIN es.disponibilities d " + "WHERE d.disponibilityId IS NOT NULL AND "
			+ "  ( UPPER(es.fullName) like '%'||UPPER(?1)||'%' OR UPPER(d.motif) like '%'||UPPER(?1)||'%' OR UPPER(d.commentd) like '%'||UPPER(?1)||'%' OR UPPER(d.startHour) like '%'||UPPER(?1)||'%' OR UPPER(d.endHour) like '%'||UPPER(?1)||'%' OR UPPER(d.startDate) like '%'||UPPER(?1)||'%' OR UPPER(d.endDate) like '%'||UPPER(?1)||'%' ) ")
	Page<DisponibilityTeachetDTO> findTeachersWithDisponibilityWithFilter(String filter, Pageable pageable);

}