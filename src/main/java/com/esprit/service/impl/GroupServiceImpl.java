package com.esprit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.esprit.dto.GroupDTO;
import com.esprit.repository.GroupRepository;
import com.esprit.service.GroupService;
import com.esprit.service.mapper.GroupMapper;

@Service
public class GroupServiceImpl implements GroupService {

	private final GroupRepository repository;

	private final GroupMapper mapper;

	public GroupServiceImpl(GroupRepository repository, GroupMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public void addGroup(GroupDTO groupDTO) {
		repository.save(mapper.groupDTOToGroupEntity(groupDTO));
	}

	@Override
	public List<GroupDTO> findAll() {
		return mapper.groupEntitiesToGroupDTO(repository.findAll());
	}

}