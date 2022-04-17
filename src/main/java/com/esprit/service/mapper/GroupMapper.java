package com.esprit.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.esprit.domain.GroupEntity;
import com.esprit.dto.GroupDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

	GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

	GroupEntity groupDTOToGroupEntity(GroupDTO groupDTO);

	GroupDTO groupEntityToGroupDTO(GroupEntity groupEntity);

	List<GroupDTO> groupEntitiesToGroupDTO(List<GroupEntity> groupEntities);

}