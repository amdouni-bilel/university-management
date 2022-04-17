package com.esprit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esprit.domain.PeriodEntity;

public interface PeriodRepository extends JpaRepository<PeriodEntity, String> {

	List<PeriodEntity> findByPeriodIdIn(List<String> periodIds);

}