package com.esprit.service.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.esprit.domain.AssignClassModuleEntity;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.esprit.domain.ClassEntity;
import com.esprit.dto.SpecialityDTO;
import com.esprit.dto.classes.ClassDTO;
import com.esprit.dto.classes.CreateClassRequest;
import com.esprit.dto.classes.ModuleDTO;
import com.esprit.repository.ModuleRepository;
import com.esprit.repository.StudentRepository;
import com.esprit.service.SpecialityService;
import com.esprit.service.StudentService;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {SpecialityService.class, StudentService.class})
public interface ClassMapper {

    ClassMapper INSTANCE = Mappers.getMapper(ClassMapper.class);

    @Mapping(source = "speciality", target = "speciality", qualifiedByName = "findSpeciality")
    ClassEntity createClassRequestToClassEntity(CreateClassRequest createClassRequest);

    @Mapping(target = "nbrStudents", ignore = true)
    @Mapping(target = "modules", ignore = true)
    ClassDTO classEntityToClassDTO(ClassEntity classEntity, @Context ModuleRepository moduleRepository,
                                   @Context StudentRepository studentRepository);

    List<ClassDTO> classEntitiesToClassDTO(List<ClassEntity> classEntities, @Context ModuleRepository moduleRepository,
                                           @Context StudentRepository studentRepository);

    @AfterMapping
    default void convert(ClassEntity classEntity, @MappingTarget ClassDTO classDTO,
                         @Context ModuleRepository moduleRepository, @Context StudentRepository studentRepository) {

        List<ModuleDTO> modules = moduleRepository.findAllWithClass(classEntity.getClassId()).stream()
                .map(item -> {
                            ModuleDTO data = new ModuleDTO().moduleId(item.getModuleId()).designation(item.getDesignation());

                            Optional<AssignClassModuleEntity> assignClassModuleEntityOptional = item.getAssignClasses().stream().filter(x -> x.getClasss().getClassId().equals(classEntity.getClassId())).findFirst();
                            String semester = null;
                            String period = null;
                            if (assignClassModuleEntityOptional.isPresent()) {
                                AssignClassModuleEntity assignClassModuleEntity = assignClassModuleEntityOptional.get();
                                semester = "S" + assignClassModuleEntity.getSemester().getCode();
                                period = assignClassModuleEntity.getPeriods().stream().map(y -> "P" + y.getCode()).map(Object::toString).collect(Collectors.joining(","));
                            }
                            if (!StringUtils.isBlank(semester) && StringUtils.isBlank(period)) {
                                data.session(semester);
                            } else if (!StringUtils.isBlank(semester) && !StringUtils.isBlank(period)) {
                                data.session(semester + "," + period);
                            }
                            return data;
                        }
                )
                .collect(Collectors.toList());
//.session(item.getAssignClasses().stream().filter(x -> x.getClasss().getClassId().equals(classEntity.getClassId())).findFirst().get())
        classDTO.nbrStudents(studentRepository.countByClass(classEntity.getClassId()));
        classDTO.category(classEntity.getCategory());
        classDTO.classId(classEntity.getClassId());
        classDTO.email(classEntity.getEmail());
        classDTO.group(classEntity.isGroup());
        classDTO.label(classEntity.getLabel());
        classDTO.modules(modules);
        if (classEntity.getSpeciality() != null) {
            classDTO.speciality(new SpecialityDTO().label(classEntity.getSpeciality().getLabel())
                    .specialityId(classEntity.getSpeciality().getSpecialityId()));
        }
    }

}