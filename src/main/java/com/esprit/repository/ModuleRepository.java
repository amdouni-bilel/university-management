package com.esprit.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.esprit.domain.ModuleEntity;
import com.esprit.dto.module.ModuleWithoutAssignClassesDTO;

public interface ModuleRepository extends JpaRepository<ModuleEntity, String> {

	@Query("SELECT new com.esprit.dto.module.ModuleWithoutAssignClassesDTO(m.moduleId, m.designation) FROM ModuleEntity as m ORDER BY m.moduleId ASC")
	List<ModuleWithoutAssignClassesDTO> findAllWithoutAssignClasses();

	@Query(nativeQuery = true, value = "select * from ESP_MODULE md LEFT JOIN ESP_MODULE_CLASS mc ON md.moduleId=mc.fk_module WHERE mc.fk_class=?1 ORDER BY md.moduleId ASC")
	List<ModuleEntity> findAllWithClass(String classe);

	@Query(nativeQuery = true, value = "select * from ESP_MODULE md where exists (select 1 from ESP_MODULE_CLASS mc where mc.fk_module = md.moduleId and SUBSTR(mc.fk_class, 1, 1) IN ?1) ORDER BY md.moduleId ASC")
	List<ModuleEntity> findByLavels(Set<String> levels);

	@Query("From ModuleEntity m WHERE UPPER(m.designation) like '%'||UPPER(?1)||'%' OR UPPER(m.moduleId) like '%'||UPPER(?1)||'%' ORDER BY m.moduleId ASC")
	Page<ModuleEntity> findAllWithFilter(String filter, Pageable pageable);

}