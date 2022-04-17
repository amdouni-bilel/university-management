package com.esprit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.esprit.domain.GroupEntity;

public interface GroupRepository extends JpaRepository<GroupEntity, String> {

	@Query(nativeQuery = true, value = "SELECT * FROM groupe gr JOIN CLASSE_GROUP grp on grp.FK_GROUP = gr.groupId AND grp.FK_CLASS = ?2 WHERE gr.label = ?1")
	GroupEntity findByLabelAndClass(String label, String classId);

	@Query(nativeQuery = true, value = "SELECT * FROM groupe gr JOIN CLASSE_GROUP grp on grp.FK_GROUP = gr.groupId AND grp.FK_CLASS = ?1")
	List<GroupEntity> findByClass(String classId);

	@Query(nativeQuery = true, value = "SELECT * FROM groupe gr JOIN CLASSE_GROUP grp on grp.FK_GROUP = gr.groupId AND grp.FK_CLASS = ?1 WHERE gr.label ='Groupe 1'")
	List<GroupEntity> findByClassG1(String classId);

	@Query(nativeQuery = true, value = "SELECT * FROM groupe gr JOIN CLASSE_GROUP grp on grp.FK_GROUP = gr.groupId AND grp.FK_CLASS = ?1 WHERE gr.label ='Groupe 2'")
	List<GroupEntity> findByClassG2(String classId);

	@Query(nativeQuery = true, value = "SELECT * FROM groupe gr JOIN CLASSE_GROUP grp on grp.FK_GROUP = gr.groupId AND grp.FK_CLASS IN ?1 WHERE gr.label ='Groupe 1'")
	List<GroupEntity> findByClassesG1(List<String> classIds);

	@Query(nativeQuery = true, value = "SELECT * FROM groupe gr JOIN CLASSE_GROUP grp on grp.FK_GROUP = gr.groupId AND grp.FK_CLASS IN ?1 WHERE gr.label ='Groupe 2'")
	List<GroupEntity> findByClassesG2(List<String> classIds);

	@Query(nativeQuery = true, value = "SELECT * FROM groupe gr JOIN CLASSE_GROUP grp on grp.FK_GROUP = gr.groupId AND grp.FK_CLASS IN ?1")
	List<GroupEntity> findByClasses(List<String> classIds);

}