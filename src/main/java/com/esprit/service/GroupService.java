package com.esprit.service;

import java.util.List;

import com.esprit.dto.GroupDTO;

public interface GroupService {
	
	void addGroup(GroupDTO groupDTO);
	
	List<GroupDTO> findAll();

}