package com.esprit.service.impl;

import java.util.List;
import java.util.Optional;

import com.esprit.repository.ModuleRepository;
import org.springframework.stereotype.Service;

import com.esprit.domain.ClassEntity;
import com.esprit.domain.GroupEntity;
import com.esprit.domain.StudentEntity;
import com.esprit.dto.classes.ClassDTO;
import com.esprit.dto.classes.CreateClassRequest;
import com.esprit.error.exception.EntityNotFoundException;
import com.esprit.repository.ClassRepository;
import com.esprit.repository.StudentRepository;
import com.esprit.service.ClassService;
import com.esprit.service.mapper.ClassMapper;

@Service
public class ClassServiceImpl implements ClassService {

	private final ClassRepository classRepository;

	private final ModuleRepository moduleRepository;

	private final StudentRepository studentRepository;

	private final ClassMapper mapper;

	public ClassServiceImpl(ClassRepository classRepository, ModuleRepository moduleRepository,
							StudentRepository studentRepository, ClassMapper mapper) {
		this.classRepository = classRepository;
		this.moduleRepository = moduleRepository;
		this.studentRepository = studentRepository;
		this.mapper = mapper;
	}

	@Override
	public void addClass(CreateClassRequest createClassRequest) {
		ClassEntity classEntity = mapper.createClassRequestToClassEntity(createClassRequest);
		classEntity.addGroup(new GroupEntity().label("Groupe 1"));
		classRepository.save(classEntity);
	}

	@Override
	public void updateClass(CreateClassRequest createClassRequest) {
		Optional<ClassEntity> classEntityOptional = classRepository.findById(createClassRequest.getClassId());
		if (classEntityOptional.isPresent()) {
			ClassEntity currentClassEntity = classEntityOptional.get();

			ClassEntity classEntity = mapper.createClassRequestToClassEntity(createClassRequest);
			currentClassEntity.label(classEntity.getLabel()).email(classEntity.getEmail()).speciality(classEntity.getSpeciality()).category(classEntity.getCategory());

			if (createClassRequest.isGroup() && !currentClassEntity.isGroup()) {
				currentClassEntity.addGroup(new GroupEntity().label("Groupe 2"));
				currentClassEntity.group(createClassRequest.isGroup());
				classRepository.save(currentClassEntity);

				List<StudentEntity> studentEntities = studentRepository.findByClass(createClassRequest.getClassId());
				int nbr = Math.round(studentEntities.size() / 2);

				List<StudentEntity> firstSGroup = studentEntities.subList(0, nbr);
				firstSGroup.forEach(student -> {
					student.group(currentClassEntity.getGroups().get(0));
					studentRepository.save(student);
				});
				List<StudentEntity> secoundtSGroup = studentEntities.subList(nbr, studentEntities.size());
				secoundtSGroup.forEach(student -> {
					student.group(currentClassEntity.getGroups().get(1));
					studentRepository.save(student);
				});
			} else {
				classRepository.save(currentClassEntity);
			}
		} else {
			throw new EntityNotFoundException(ClassEntity.class, "Id", createClassRequest.getClassId());
		}
	}

	@Override
	public void deleteClass(String classId) {
		if (findClass(classId) != null) {
			classRepository.deleteById(classId);
		}
	}

	@Override
	public ClassDTO findClass(String classId) {
		ClassDTO result = null;
		Optional<ClassEntity> classEntityOptional = classRepository.findById(classId);
		if (classEntityOptional.isPresent()) {
			result = mapper.classEntityToClassDTO(classEntityOptional.get(), moduleRepository, studentRepository);
		} else {
			throw new EntityNotFoundException(ClassEntity.class, "id", classId);
		}
		return result;
	}

	@Override
	public List<ClassDTO> findClasses() {
		return mapper.classEntitiesToClassDTO(classRepository.findAllByOrderByClassIdAsc(), moduleRepository,
				studentRepository);
	}

	@Override
	public List<ClassDTO> searchClasses(String name, String email, String speciality) {
		return mapper.classEntitiesToClassDTO(classRepository.searchConventions(name, email, speciality),
				moduleRepository, studentRepository);
	}

	@Override
	public List<ClassEntity> findClassesByIds(List<String> classes) {
		return classRepository.findByClassIdIn(classes);
	}

	@Override
	public List<ClassDTO> findByModule(String module) {
		return mapper.classEntitiesToClassDTO(classRepository.findByModule(module),
				moduleRepository, studentRepository);
	}

	@Override
	public ClassEntity findClassesByIds(String classId) {
		return classRepository.getOne(classId);
	}

}