package com.esprit.service.impl;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.esprit.domain.PeriodEntity;
import com.esprit.domain.SeanceEntity;
import com.esprit.domain.SemesterEntity;
import com.esprit.domain.TeacherEntity;
import com.esprit.domain.WeekEntity;
import com.esprit.dto.CreateDisponibilityRequest;
import com.esprit.dto.teacher.CreateTeacherRequest;
import com.esprit.dto.teacher.DisponibilityTeachetDTO;
import com.esprit.dto.teacher.FilterAvailableTeacherDTO;
import com.esprit.dto.teacher.TeacherDTO;
import com.esprit.dto.teacher.TeacherWithoutDisponibilityDTO;
import com.esprit.error.exception.EntityNotFoundException;
import com.esprit.error.exception.UnicityException;
import com.esprit.repository.DisponibilityRepository;
import com.esprit.repository.PeriodRepository;
import com.esprit.repository.SeanceRepository;
import com.esprit.repository.SemesterRepository;
import com.esprit.repository.TeacherRepository;
import com.esprit.repository.WeekRepository;
import com.esprit.service.TeacherService;
import com.esprit.service.mapper.TeacherMapper;

@Service
public class TeacherServiceImpl implements TeacherService {

	private final DisponibilityRepository disponibilityRepository;

	private final TeacherRepository teacherRepository;

	private final WeekRepository weekRepository;

	private final PeriodRepository periodRepository;

	private final SemesterRepository semesterRepository;

	private final SeanceRepository seanceRepository;

	private final TeacherMapper mapper;

	public TeacherServiceImpl(DisponibilityRepository disponibilityRepository, TeacherRepository teacherRepository,
			WeekRepository weekRepository, PeriodRepository periodRepository, SemesterRepository semesterRepository,
			SeanceRepository seanceRepository, TeacherMapper mapper) {
		this.disponibilityRepository = disponibilityRepository;
		this.teacherRepository = teacherRepository;
		this.weekRepository = weekRepository;
		this.periodRepository = periodRepository;
		this.semesterRepository = semesterRepository;
		this.seanceRepository = seanceRepository;
		this.mapper = mapper;
	}

	@Override
	public void addTeacher(CreateTeacherRequest createTeacherRequest) {
		if (CollectionUtils.isNotEmpty(createTeacherRequest.getDisponibilities())) {
			Map<String, Integer> namesMap = new HashMap<>(6);
			namesMap.put("lundi", 0);
			namesMap.put("mardi", 1);
			namesMap.put("mercredi", 2);
			namesMap.put("jeudi", 3);
			namesMap.put("vendredi", 4);
			namesMap.put("samedi", 5);
			for (CreateDisponibilityRequest disp : createTeacherRequest.getDisponibilities()) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
				int n = disponibilityRepository.checkUnicityTeacher(createTeacherRequest.getTeacherId(),
						disp.getStartHour(), disp.getEndHour(), dateFormat.format(disp.getStartDate()),
						dateFormat.format(disp.getEndDate()));
				if (n > 0) {
					throw new UnicityException();
				} else {
					if (!StringUtils.isBlank(disp.getWeekId()) && !StringUtils.isBlank(disp.getDay())) {
						Optional<WeekEntity> weekOptional = weekRepository.findById(disp.getWeekId());
						if (weekOptional.isPresent()) {
							disp.getDay().toUpperCase();
							Calendar c = Calendar.getInstance();
							c.setTime(weekOptional.get().getStartDate());
							c.add(Calendar.DATE, namesMap.get(disp.getDay()));
							disp.startDate(c.getTime());
							disp.endDate(c.getTime());
						}
					} else if (!StringUtils.isBlank(disp.getWeekId()) && StringUtils.isBlank(disp.getDay())) {
						Optional<WeekEntity> weekOptional = weekRepository.findById(disp.getWeekId());
						disp.startDate(weekOptional.get().getStartDate());
						disp.endDate(weekOptional.get().getEndDate());
					} else if (!StringUtils.isBlank(disp.getPeriodId()) && StringUtils.isBlank(disp.getWeekId())
							&& StringUtils.isBlank(disp.getDay())) {
						Optional<PeriodEntity> periodOptional = periodRepository.findById(disp.getWeekId());
						disp.startDate(periodOptional.get().getStartDate());
						disp.endDate(periodOptional.get().getEndDate());
					} else if (!StringUtils.isBlank(disp.getSemesterId()) && StringUtils.isBlank(disp.getPeriodId())
							&& StringUtils.isBlank(disp.getWeekId()) && StringUtils.isBlank(disp.getDay())) {
						Optional<SemesterEntity> semesterOptional = semesterRepository.findById(disp.getWeekId());
						disp.startDate(semesterOptional.get().getStartDate());
						disp.endDate(semesterOptional.get().getEndDate());
					}
					if (!StringUtils.isBlank(disp.getSeanceId())) {
						Optional<SeanceEntity> seanceOptional = seanceRepository.findById(disp.getSeanceId());
						if (seanceOptional.isPresent()) {
							disp.startHour(seanceOptional.get().getStartHour());
							disp.endHour(seanceOptional.get().getEndHour());
						}
					}
				}
			}
		}
		TeacherEntity teacherEntity = mapper.createTeacherRequestToTeacherEntity(createTeacherRequest);
		if (CollectionUtils.isNotEmpty(teacherEntity.getDisponibilities())) {
			teacherEntity.addDisponibilities(teacherEntity.getDisponibilities());
		}
		teacherEntity = teacherRepository.save(teacherEntity);
	}

	@Override
	public TeacherDTO findBydId(String userId) {
		TeacherDTO result = null;
		if (!StringUtils.isBlank(userId)) {
			Optional<TeacherEntity> teacherEntityOptional = teacherRepository.findById(userId);
			if (teacherEntityOptional.isPresent()) {
				result = mapper.teacherEntityToTeacherDTO(teacherEntityOptional.get());
			} else {
				throw new EntityNotFoundException(TeacherDTO.class, "id", userId);
			}
		}
		return result;
	}

	@Override
	public List<TeacherDTO> findTeachers() {
		return mapper.teacherEntitiesToTeacherDTO(teacherRepository.findAllByOrderByFullNameAsc());
	}

	@Override
	public List<TeacherDTO> searcheachers(String teacherId, int cin, String departement, String classs) {
		return mapper.teacherEntitiesToTeacherDTO(teacherRepository.searcheachers(teacherId, cin, departement, classs));
	}

	@Override
	public List<TeacherDTO> findAllByUps(FilterAvailableTeacherDTO filterAvailableTeacherDTO) {
		List<TeacherEntity> result = new ArrayList<>();
		List<String> teachers = disponibilityRepository.findTeachrsInd(filterAvailableTeacherDTO.getEffectDate(), filterAvailableTeacherDTO.getHour());
		if (CollectionUtils.isNotEmpty(teachers)) {
			List<String> data = teachers.stream().filter(Objects::nonNull).collect(Collectors.toList());
			List<TeacherEntity> teacherEntities = teacherRepository.findAllByUps(filterAvailableTeacherDTO.getUps().stream().map(String::toLowerCase).collect(Collectors.toList()));
			result = teacherEntities.stream().filter(item -> !data.contains(item.getTeacherId())).collect(Collectors.toList());
		} else {
			result = teacherRepository.findAllByUps(filterAvailableTeacherDTO.getUps().stream().map(item -> item.toLowerCase()).collect(Collectors.toList()));
		}
		return mapper.teacherEntitiesToTeacherDTO(result.stream().filter(distinctByKey(TeacherEntity::getTeacherId)).collect(Collectors.toList()));
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}

	@Override
	public List<TeacherEntity> findTeachersByIds(List<String> teachers) {
		List<TeacherEntity> teacherEntities = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(teachers)) {
			teachers.forEach(teacherId -> {
				Optional<TeacherEntity> teacherEntityOptional = teacherRepository.findById(teacherId);
				if (teacherEntityOptional.isPresent()) {
					teacherEntities.add(teacherEntityOptional.get());
				}
			});
		}
		return teacherEntities;
	}

	@Override
	public void addDisponibilityToTeacher(String teacherId, Date supervisionDate, int supervisionHour,
			int supervisionDuration) {
		/*
		 * Optional<TeacherEntity> teacherEntityOptional =
		 * teacherRepository.findById(teacherId); if (teacherEntityOptional.isPresent())
		 * { TeacherEntity teacherEntity = teacherEntityOptional.get();
		 * DisponibilityTeacherEntity disponibilityTeacher = new
		 * DisponibilityTeacherEntity()
		 * .supervisionDate(supervisionDate).supervisionHour(supervisionHour)
		 * .supervisionDuration(supervisionDuration);
		 * teacherEntity.addDisponibility(disponibilityTeacher);
		 * teacherRepository.save(teacherEntity); }
		 */
	}

	@Override
	public List<String> findUpByTeachers(List<String> teachers) {
		return teacherRepository.findUpByTeachers(teachers);
	}

	@Override
	public Map<String, Object> findTeachersWithDisponibility(Pageable paging) {
		Page<DisponibilityTeachetDTO> teachers = teacherRepository.findTeachersWithDisponibilityWithPage(paging);
		Map<String, Object> response = new HashMap<>();
		response.put("body", teachers);
		response.put("currentPage", teachers.getNumber());
		response.put("totalItems", teachers.getTotalElements());
		response.put("totalPages", teachers.getTotalPages());
		return response;
	}

	@Override
	public Map<String, Object> findTeachersWithDisponibilityWithFilter(String filter, Pageable paging) {
		Page<DisponibilityTeachetDTO> teachers = teacherRepository.findTeachersWithDisponibilityWithFilter(filter,
				paging);
		Map<String, Object> response = new HashMap<>();
		response.put("body", teachers);
		response.put("currentPage", teachers.getNumber());
		response.put("totalItems", teachers.getTotalElements());
		response.put("totalPages", teachers.getTotalPages());
		return response;
	}

	@Override
	public List<TeacherWithoutDisponibilityDTO> findTeachersWithoutDisponibilities() {
		return teacherRepository.findTeachersWithoutDisponibilities();
	}

	@Override
	public TeacherDTO findByPrincipal(Principal principal) {
		TeacherDTO result = new TeacherDTO();
		if (KeycloakAuthenticationToken.class.isAssignableFrom(principal.getClass())) {
			KeycloakAuthenticationToken authToken = (KeycloakAuthenticationToken) principal;
			AccessToken token = authToken.getAccount().getKeycloakSecurityContext().getToken();
			result = mapper.teacherEntityToTeacherDTO(teacherRepository.findByEmail(token.getEmail()));
		}
		return result;
	}

}