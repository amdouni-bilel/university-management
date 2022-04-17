package com.esprit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.esprit.domain.StudentEntity;

public interface StudentRepository extends JpaRepository<StudentEntity, String> {
	
	StudentEntity findByCin(int cin);
	
	StudentEntity findByEmail(String email);
	
	@Query(nativeQuery = true, value = "select * from ESP_ETUDIANT where fk_class = ?1")
	List<StudentEntity> findByClass(String classe);
	
	@Query(nativeQuery = true, value = "select * from ESP_ETUDIANT ed \n" + 
			"inner join GROUPE g ON ed.fk_group = g.groupid\n" + 
			"inner join CLASSE cl ON ed.fk_class = cl.classId\n" + 
			"WHERE cl.classId = ?1 AND g.groupid = ?2")
	List<StudentEntity> findByClassAndGroup(String classId, String GroupId);
	
	@Query(nativeQuery = true, value = "select count(*) from ESP_ETUDIANT where fk_class = ?1")
	int countByClass(String classe);
	
	@Query(nativeQuery = true, value = "SELECT count(*) FROM esp_etudiant etd "
			+ "JOIN CLASSE_GROUP grp on grp.FK_GROUP = etd.fk_group AND grp.FK_CLASS = etd.fk_class "
			+ "WHERE grp.FK_GROUP = ?2 AND etd.FK_CLASS = ?1")
	int countByClassAndGroup(String classe, String group);

}