package com.esprit.service.impl;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.stereotype.Service;

import com.esprit.domain.ClassEntity;
import com.esprit.domain.GroupEntity;
import com.esprit.domain.StudentEntity;
import com.esprit.dto.student.CreateStudentRequest;
import com.esprit.dto.student.StudentDTO;
import com.esprit.dto.user.CreateUserRequest;
import com.esprit.error.exception.EntityAlreadyExistsExeption;
import com.esprit.repository.ClassRepository;
import com.esprit.repository.GroupRepository;
import com.esprit.repository.StudentRepository;
import com.esprit.service.IAMService;
import com.esprit.service.StudentService;
import com.esprit.service.mapper.StudentMapper;

@Service
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;

	private final ClassRepository classRepository;

	private final GroupRepository groupRespository;

	private final StudentMapper mapper;

	private final IAMService iamService;

	public StudentServiceImpl(StudentRepository studentRepository, ClassRepository classRepository,
			GroupRepository groupRespository, StudentMapper mapper, IAMService iamService) {
		this.studentRepository = studentRepository;
		this.classRepository = classRepository;
		this.groupRespository = groupRespository;
		this.mapper = mapper;
		this.iamService = iamService;
	}

	@Override
	public void addStudent(CreateStudentRequest createStudentRequest) {
		StudentEntity studentEntity = mapper.createStudentRequestToStudentEntity(createStudentRequest, classRepository);

		if (!StringUtils.isBlank(createStudentRequest.getGroupId())) {
			GroupEntity groupEntity = groupRespository.findByLabelAndClass(createStudentRequest.getGroupId(),
					createStudentRequest.getClassId());
			if (groupEntity != null) {
				studentEntity.group(groupEntity);
			} else {
				GroupEntity group = groupRespository.save(new GroupEntity().label(createStudentRequest.getGroupId())
						.classes(Arrays.asList(classRepository.getOne(createStudentRequest.getClassId()))));
				studentEntity.group(group);
			}
		} else {
			studentEntity.group(groupRespository.findByLabelAndClass("Groupe 1", createStudentRequest.getClassId()));
		}

		if (studentRepository.findByCin(createStudentRequest.getCin()) != null) {
			throw new EntityAlreadyExistsExeption(StudentEntity.class, "cin", createStudentRequest.getCin() + "");
		}
		if (!StringUtils.isBlank(createStudentRequest.getEmail())
				&& studentRepository.findByEmail(createStudentRequest.getEmail()) != null) {
			throw new EntityAlreadyExistsExeption(StudentEntity.class, "email", createStudentRequest.getEmail());
		}
		studentRepository.save(studentEntity);

		String firstName = null;
		String lastName = null;
		if (createStudentRequest.getFirstName().contains(" ")) {
			firstName = createStudentRequest.getFirstName().toLowerCase().replace(" ", "_");
		} else {
			firstName = createStudentRequest.getFirstName().toLowerCase();
		}
		if (createStudentRequest.getLastName().contains(" ")) {
			lastName = createStudentRequest.getLastName().toLowerCase().replace(" ", "_");
		} else {

			lastName = createStudentRequest.getLastName().toLowerCase();
		}
		String username = firstName + "_" + lastName + "_" + createStudentRequest.getClassId().toLowerCase();

		CreateUserRequest createUserRequest = new CreateUserRequest().role("STUDENT").username(username)
				.firstName(createStudentRequest.getFirstName()).lastName(createStudentRequest.getLastName())
				.cin(createStudentRequest.getCin());
		if (!StringUtils.isBlank(createStudentRequest.getEmail())) {
			createUserRequest.email(createStudentRequest.getEmail());
		}
		iamService.addUser(createUserRequest);
	}

	@Override
	public void updateStudent(CreateStudentRequest createStudentRequest) {
		StudentEntity studentEntity = mapper.createStudentRequestToStudentEntity(createStudentRequest, classRepository);
		if (!StringUtils.isBlank(createStudentRequest.getGroupId())) {
			GroupEntity groupEntity = groupRespository.findByLabelAndClass(createStudentRequest.getGroupId(),
					createStudentRequest.getClassId());
			if (groupEntity != null) {
				studentEntity.group(groupEntity);
			} else {
				GroupEntity group = groupRespository.save(new GroupEntity().label(createStudentRequest.getGroupId())
						.classes(Arrays.asList(classRepository.getOne(createStudentRequest.getClassId()))));
				studentEntity.group(group);
			}
		} else {
			studentEntity.group(groupRespository.findByLabelAndClass("Groupe 1", createStudentRequest.getClassId()));
		}
		studentRepository.save(studentEntity);
	}

	@Override
	public List<StudentDTO> findAll() {
		return mapper.studentEntitiesToStudentDTO(studentRepository.findAll());
	}

	@Override
	public void deleteStudent(String studentId) {
		studentRepository.deleteById(studentId);
		iamService.deleteUser(studentId);
	}

	@Override
	public StudentDTO findByPrincipal(Principal principal) {
		StudentDTO result = new StudentDTO();
		if (KeycloakAuthenticationToken.class.isAssignableFrom(principal.getClass())) {
			KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) principal;
			AccessToken token = authToken.getAccount().getKeycloakSecurityContext().getToken();
			result = mapper.studentEntityToStudentDTO(studentRepository.findByEmail(token.getEmail()));
		}
		return result;
	}

	@Override
	public List<StudentDTO> findByClass(String classe) {
		return mapper.studentEntitiesToStudentDTO(studentRepository.findByClass(classe));
	}

	@Override
	public int countByClass(String classe) {
		return studentRepository.countByClass(classe);
	}

	@Override
	public void addAllStudent(List<CreateStudentRequest> createStudentRequest) {
		List<StudentEntity> studentEntities = mapper.createStudentRequestToStudentEntities(createStudentRequest,
				classRepository);
		Map<String, List<StudentEntity>> map = studentEntities.stream().filter(c -> c.getClasss() != null).collect(Collectors.groupingBy(item -> item.getClasss().getClassId()));

		for (Map.Entry<String, List<StudentEntity>> entry : map.entrySet()) {

			List<StudentEntity> result = entry.getValue();
			int nbr = Math.round(result.size() / 2);

			List<StudentEntity> firstSGroup = result.subList(0, nbr);
			for (StudentEntity student : firstSGroup) {
				student.group(groupRespository.findByLabelAndClass("Groupe 1", entry.getKey()));
				studentRepository.save(student);
			}

			ClassEntity currentClassEntity = classRepository.getOne(entry.getKey());
			currentClassEntity.addGroup(new GroupEntity().label("Groupe 2"));
			currentClassEntity.group(true);
			classRepository.save(currentClassEntity);

			List<StudentEntity> secoundtSGroup = result.subList(nbr, result.size());
			for (StudentEntity student : secoundtSGroup) {
				try {
					student.group(groupRespository.findByLabelAndClass("Groupe 2", entry.getKey()));
					studentRepository.save(student);
				} catch (Exception e) {
					System.out.println(e.getMessage() + " ---- " + entry.getKey());
				}
			}
		}
		for (CreateStudentRequest std : createStudentRequest) {
			String firstName = null;
			String lastName = null;
			if (std.getFirstName().contains(" ")) {
				firstName = std.getFirstName().toLowerCase().replace(" ", "_");
			} else {
				firstName = std.getFirstName().toLowerCase();
			}
			if (std.getLastName().contains(" ")) {
				lastName = std.getLastName().toLowerCase().replace(" ", "_");
			} else {

				lastName = std.getLastName().toLowerCase();
			}
			String username = firstName + "_" + lastName + "_" + std.getClassId().toLowerCase();
			CreateUserRequest createUserRequest = new CreateUserRequest().role("STUDENT").firstName(std.getFirstName())
					.lastName(std.getLastName()).username(username).cin(std.getCin());
			if (!StringUtils.isBlank(std.getEmail())) {
				createUserRequest.email(std.getEmail());
			}
			iamService.addUser(createUserRequest);
		}
	}

	@Override
	public int countByClassAndGroup(String classe, String group) {
		return studentRepository.countByClassAndGroup(classe, group);
	}

}