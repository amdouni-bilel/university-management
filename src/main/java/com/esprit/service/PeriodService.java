package com.esprit.service;

import java.util.List;

import org.mapstruct.Named;

import com.esprit.domain.PeriodEntity;

public interface PeriodService {

	@Named("findPeriod")
	PeriodEntity findPeriod(String periodId);
	
	@Named("findPeriods")
	List<PeriodEntity> findPeriods(List<String> periodIds);

}