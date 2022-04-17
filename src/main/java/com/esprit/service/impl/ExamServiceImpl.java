package com.esprit.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.esprit.domain.ClassEntity;
import com.esprit.domain.DisponibilityEntity;
import com.esprit.domain.ExamEntity;
import com.esprit.domain.GroupEntity;
import com.esprit.domain.RoomEntity;
import com.esprit.domain.SessionEntity;
import com.esprit.domain.TeacherEntity;
import com.esprit.dto.SessionDTO;
import com.esprit.dto.exam.CreateExamRequest;
import com.esprit.dto.exam.DeleteExamDTO;
import com.esprit.dto.exam.ExamInfoDTO;
import com.esprit.dto.exam.UpdateExamRequest;
import com.esprit.error.exception.ClassExistInSessionException;
import com.esprit.error.exception.RoomSizeExecption;
import com.esprit.error.exception.TeacherSizeExecption;
import com.esprit.error.exception.TeachersExistInSessionException;
import com.esprit.repository.ClassRepository;
import com.esprit.repository.DisponibilityRepository;
import com.esprit.repository.ExamRepository;
import com.esprit.repository.GroupRepository;
import com.esprit.repository.ModuleRepository;
import com.esprit.repository.RoomRepository;
import com.esprit.repository.SessionRepository;
import com.esprit.repository.TeacherRepository;
import com.esprit.service.ExamService;

@Service
public class ExamServiceImpl implements ExamService {

	private final SessionRepository sessionRepository;

	private final ExamRepository examRepository;

	private final ClassRepository classRepository;

	private final ModuleRepository moduleRepository;

	private final RoomRepository roomRepository;

	private final TeacherRepository teacherRepository;

	private final GroupRepository groupRepository;

	private final DisponibilityRepository disponibilityRepository;

	public ExamServiceImpl(SessionRepository sessionRepository, ExamRepository examRepository,
			ClassRepository classRepository, ModuleRepository moduleRepository, RoomRepository roomRepository,
			TeacherRepository teacherRepository, GroupRepository groupRepository,
			DisponibilityRepository disponibilityRepository) {
		this.sessionRepository = sessionRepository;
		this.examRepository = examRepository;
		this.classRepository = classRepository;
		this.moduleRepository = moduleRepository;
		this.roomRepository = roomRepository;
		this.teacherRepository = teacherRepository;
		this.groupRepository = groupRepository;
		this.disponibilityRepository = disponibilityRepository;
	}

	@Override
	public void addExam(CreateExamRequest createExamRequest) {
		String examId = UUID.randomUUID().toString();

		List<ExamEntity> examEntityBySessions = examRepository
				.findBySession(createExamRequest.getSession().getSessionId());
		List<String> list3 = new ArrayList<>();
		for (ExamEntity examEntityBySession : examEntityBySessions) {
			for (ClassEntity classEntity : examEntityBySession.getClasses()) {
				for (GroupEntity groupEntity : classEntity.getGroups()) {
					for (TeacherEntity teacherEntity : groupEntity.getTeachers()) {
						list3.add(teacherEntity.getTeacherId());
					}
				}
			}
		}
		check(examEntityBySessions, list3, examId, createExamRequest.getTeachers(), createExamRequest.getUp(),
				createExamRequest.getRooms(), createExamRequest.getBloc(), createExamRequest.getExamDate(),
				createExamRequest.getExamHour(), createExamRequest.getEndHour(),
				createExamRequest.getSession().getSessionId(), createExamRequest.getExamDuration(),
				createExamRequest.getModule(), createExamRequest.getClasses());

		ExamEntity examEntityR = mapExam(examId, createExamRequest.getSession().getSessionId(),
				createExamRequest.getExamDate(), createExamRequest.getExamHour(), createExamRequest.getExamDuration(),
				createExamRequest.getModule(), createExamRequest.getClasses());

		ExamEntity examEntity = addExistTeachersAndRoomsToCurrentGroups(examEntityR);

		List<ClassEntity> classEntities = mapTeachers(examId, examEntity, createExamRequest.getTeachers(),
				createExamRequest.getUp(), createExamRequest.getExamDate(), createExamRequest.getExamHour());

		mapRooms(examId, classEntities, createExamRequest.getRooms(), createExamRequest.getBloc(),
				createExamRequest.getExamDate(), createExamRequest.getExamHour());

		examEntity.endHour(createExamRequest.getEndHour());
		examRepository.save(examEntity);

		updateDiponibilties(examId, createExamRequest.getTeachers(), createExamRequest.getUp(),
				createExamRequest.getRooms(), createExamRequest.getBloc(), createExamRequest.getExamDate(),
				createExamRequest.getExamHour(), createExamRequest.getEndHour(), createExamRequest.getSession(),
				createExamRequest.getExamDuration(), createExamRequest.getModule(), createExamRequest.getClasses());
	}

	@Override
	public void updateExam(UpdateExamRequest updateExamRequest) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
		Optional<GroupEntity> groupEntityOptional = groupRepository.findById(updateExamRequest.getGroupId());
		Optional<ExamEntity> examEntityOptional = examRepository.findById(updateExamRequest.getExamId());
		if (groupEntityOptional.isPresent() && examEntityOptional.isPresent()) {
			ExamEntity examEntity = examEntityOptional.get();
			GroupEntity groupEntity = groupEntityOptional.get();

			if (CollectionUtils.isNotEmpty(groupEntity.getTeachers())) {
				List<TeacherEntity> teachers = groupEntity.getTeachers().stream()
						.filter(el -> el.getExamIds().contains(updateExamRequest.getExamId()))
						.collect(Collectors.toList());
				for (TeacherEntity teacherEntity : teachers) {
					List<DisponibilityEntity> result = teacherEntity.getDisponibilities().stream()
							.filter(el -> el.getExamId().equals(updateExamRequest.getExamId()))
							.collect(Collectors.toList());
					teacherEntity.removeDisponibilities(result);
					teacherRepository.save(teacherEntity);
					groupEntity.removeTeacherEntity(teacherEntity);
				}
			}
			if (CollectionUtils.isNotEmpty(groupEntity.getRooms())) {
				List<RoomEntity> rooms = groupEntity.getRooms().stream()
						.filter(el -> el.getExamIds().contains(updateExamRequest.getExamId()))
						.collect(Collectors.toList());
				for (RoomEntity roomEntity : rooms) {
					List<DisponibilityEntity> result = roomEntity.getDisponibilities().stream()
							.filter(el -> el.getExamId().equals(updateExamRequest.getExamId()))
							.collect(Collectors.toList());
					roomEntity.removeDisponibilities(result);
					roomRepository.save(roomEntity);
					groupEntity.removeRoomEntity(roomEntity);
				}
			}
			if (CollectionUtils.isNotEmpty(groupEntity.getTeachers())
					|| CollectionUtils.isNotEmpty(groupEntity.getRooms())) {
				groupRepository.save(groupEntity);
			}

			List<RoomEntity> roomsEntities = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(updateExamRequest.getRooms())) {
				roomsEntities = roomRepository.findByClassRoomIdIn(updateExamRequest.getRooms());
				roomsEntities.forEach(item -> item.getExamIds().add(updateExamRequest.getExamId()));
			} else {
				List<String> rooms = disponibilityRepository.findRommsInd(dateFormat.format(examEntity.getExamDate()),
						examEntity.getExamHour());
				if (CollectionUtils.isNotEmpty(rooms)) {
					List<String> data = rooms.stream().filter(Objects::nonNull).collect(Collectors.toList());
					List<RoomEntity> result = roomRepository.findAllByBlocs(updateExamRequest.getBloc());
					roomsEntities = result.stream().filter(item -> !data.contains(item.getClassRoomId()))
							.collect(Collectors.toList());
				} else {
					roomsEntities = roomRepository.findAllByBlocs(updateExamRequest.getBloc());
				}
			}

			List<TeacherEntity> teacherEntities = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(updateExamRequest.getTeachers())) {
				teacherEntities = teacherRepository.findByTeacherIdIn(updateExamRequest.getTeachers());
				teacherEntities.forEach(item -> item.getExamIds().add(updateExamRequest.getExamId()));
			} else {
				List<String> teachers = disponibilityRepository
						.findTeachrsInd(dateFormat.format(examEntity.getExamDate()), examEntity.getExamHour());
				if (CollectionUtils.isNotEmpty(teachers)) {
					List<String> data = teachers.stream().filter(Objects::nonNull).collect(Collectors.toList());
					teacherEntities = teacherRepository.findAllByUps(updateExamRequest.getUp().stream()
							.map(item -> item.toLowerCase()).collect(Collectors.toList()));
					teacherEntities.stream().filter(item -> !data.contains(item.getTeacherId()))
							.collect(Collectors.toList());
				} else {
					teacherEntities = teacherRepository.findAllByUps(updateExamRequest.getUp().stream()
							.map(item -> item.toLowerCase()).collect(Collectors.toList()));
				}
			}

			groupEntity.addRooms(roomsEntities);
			groupEntity.addTeachers(teacherEntities);
			groupRepository.save(groupEntity);

			for (TeacherEntity teacher : groupEntity.getTeachers()) {

				teacher.addDisponibility(new DisponibilityEntity().startDate(examEntity.getExamDate())
						.endDate(examEntity.getExamDate()).startHour(examEntity.getExamHour())
						.endHour(examEntity.getEndHour()).motif("Surveillance").examId(updateExamRequest.getExamId())
						.session(examEntity.getSession()));
				teacherRepository.save(teacher);
			}
			for (RoomEntity room : groupEntity.getRooms()) {
				String motif = null;
				if (examEntity.getSession().getLabel().equalsIgnoreCase("S1P1")
						|| examEntity.getSession().getLabel().equalsIgnoreCase("S2P1")) {
					motif = "DS";
				} else if (examEntity.getSession().getLabel().equalsIgnoreCase("S1P2")
						|| examEntity.getSession().getLabel().equalsIgnoreCase("S2P2")
						|| examEntity.getSession().getLabel().equalsIgnoreCase("SP")) {
					motif = "Examen";
				} else if (examEntity.getSession().getLabel().equalsIgnoreCase("SR")) {
					motif = "Rattrapage";
				}
				room.addDisponibility(
						new DisponibilityEntity().startDate(examEntity.getExamDate()).endDate(examEntity.getExamDate())
								.startHour(examEntity.getExamHour()).endHour(examEntity.getEndHour()).motif(motif)
								.examId(updateExamRequest.getExamId()).session(examEntity.getSession()));
				roomRepository.save(room);
			}
		}
	}

	@Override
	public void deleteExam(DeleteExamDTO deleteExamDTO) {
		Optional<ExamEntity> examEntityOptional = examRepository.findById(deleteExamDTO.getExamId());
		if (examEntityOptional.isPresent()) {
			ExamEntity exam = examEntityOptional.get();

			List<TeacherEntity> teacherEntities = teacherRepository
					.findByTeacherIdsAndExamId(deleteExamDTO.getTeacherIds(), deleteExamDTO.getExamId());
			if (CollectionUtils.isNotEmpty(teacherEntities)) {
				for (TeacherEntity teacher : teacherEntities) {
					List<DisponibilityEntity> disp = teacher.getDisponibilities().stream()
							.filter(item -> item.getExamId().equals(deleteExamDTO.getExamId()))
							.collect(Collectors.toList());
					if (CollectionUtils.isNotEmpty(disp)) {
						teacher.removeDisponibilities(disp);
						teacherRepository.save(teacher);
						disponibilityRepository.deleteAll(disp);
					}
				}
			}
			List<RoomEntity> roomEntities = roomRepository.findByClassRoomIdsAndExamId(deleteExamDTO.getRoomIds(),
					deleteExamDTO.getExamId());
			if (CollectionUtils.isNotEmpty(roomEntities)) {
				for (RoomEntity room : roomEntities) {
					List<DisponibilityEntity> disp = room.getDisponibilities().stream()
							.filter(item -> item.getExamId().equals(deleteExamDTO.getExamId()))
							.collect(Collectors.toList());
					if (CollectionUtils.isNotEmpty(disp)) {
						room.removeDisponibilities(disp);
						roomRepository.save(room);
						disponibilityRepository.deleteAll(disp);
					}
				}
			}

			Optional<GroupEntity> groupEntityOptional = groupRepository.findById(deleteExamDTO.getGroupId());
			if (groupEntityOptional.isPresent()) {
				GroupEntity groupEntity = groupEntityOptional.get();
				if (CollectionUtils.isNotEmpty(groupEntity.getTeachers())) {
					List<TeacherEntity> tr = groupEntity.getTeachers().stream()
							.filter(teacherEntity -> teacherEntity.getExamIds().size() == 1
									&& teacherEntity.getExamIds().contains(deleteExamDTO.getExamId()))
							.collect(Collectors.toList());
					if (CollectionUtils.isNotEmpty(tr)) {
						tr.forEach(item -> groupEntity.removeTeacherEntity(item));
					} else {
						groupEntity.getTeachers().forEach(item -> item.getExamIds().remove(deleteExamDTO.getExamId()));
					}
				}
				if (CollectionUtils.isNotEmpty(groupEntity.getRooms())) {
					List<RoomEntity> rr = groupEntity.getRooms().stream()
							.filter(roomEntity -> roomEntity.getExamIds().size() == 1
									&& roomEntity.getExamIds().contains(deleteExamDTO.getExamId()))
							.collect(Collectors.toList());
					if (CollectionUtils.isNotEmpty(rr)) {
						rr.forEach(item -> groupEntity.removeRoomEntity(item));
					} else {
						groupEntity.getRooms().forEach(item -> item.getExamIds().remove(deleteExamDTO.getExamId()));
					}
				}
				if (CollectionUtils.isNotEmpty(groupEntity.getTeachers())
						|| CollectionUtils.isNotEmpty(groupEntity.getRooms())) {
					groupRepository.save(groupEntity);
				}
			}

			List<TeacherEntity> teacherEntities1 = teacherRepository
					.findByTeacherIdsAndExamId(deleteExamDTO.getTeacherIds(), deleteExamDTO.getExamId());
			if (CollectionUtils.isNotEmpty(teacherEntities1)) {
				for (TeacherEntity teacher : teacherEntities1) {
					teacher.getExamIds().removeIf(it -> it.equals(deleteExamDTO.getExamId()));
					teacherRepository.save(teacher);
				}
			}
			List<RoomEntity> roomEntities2 = roomRepository.findByClassRoomIdsAndExamId(deleteExamDTO.getRoomIds(),
					deleteExamDTO.getExamId());
			if (CollectionUtils.isNotEmpty(roomEntities2)) {
				for (RoomEntity room : roomEntities2) {
					room.getExamIds().removeIf(it -> it.equals(deleteExamDTO.getExamId()));
					roomRepository.save(room);
				}
			}

			Optional<ClassEntity> classEntityOptional = exam.getClasses().stream()
					.filter(item -> item.getClassId().equals(deleteExamDTO.getClasse())).findFirst();
			ClassEntity classEntity = classEntityOptional.get();
			List<GroupEntity> results = classEntity.getGroups().stream().filter(
					item -> (CollectionUtils.isEmpty(item.getTeachers()) && CollectionUtils.isEmpty(item.getRooms()))
							|| (item.getTeachers().stream()
									.noneMatch(tch -> tch.getExamIds().contains(deleteExamDTO.getExamId()))
									&& item.getRooms().stream()
											.noneMatch((rm -> rm.getExamIds().contains(deleteExamDTO.getExamId())))))
					.collect(Collectors.toList());
			if (classEntity.getGroups().size() == results.size()) {
				exam.removeClassEntity(classEntity);
				examRepository.save(exam);
			}

			if (exam.getClasses().size() == 0) {
				examRepository.delete(exam);
			}

			/*
			 * if (exam.getClasses().size() > 1) { exam.removeClassEntity(classEntity);
			 * examRepository.save(exam); } else { examRepository.delete(exam); }
			 */
		}
	}

	@Override
	public List<ExamEntity> findExams() {
		return examRepository.findAll();
	}

	@Override
	public void deleteExamById(String examId) {
		Optional<ExamEntity> examEntityOptional = examRepository.findById(examId);
		if (examEntityOptional.isPresent()) {
			ExamEntity exam = examEntityOptional.get();

			for (ClassEntity classEntity : exam.getClasses()) {
				for (GroupEntity groupEntity : classEntity.getGroups()) {

					if (CollectionUtils.isNotEmpty(groupEntity.getTeachers())) {
						List<TeacherEntity> teachers = groupEntity.getTeachers().stream()
								.filter(el -> el.getExamIds().contains(examId)).collect(Collectors.toList());
						for (TeacherEntity teacherEntity : teachers) {
							groupEntity.removeTeacherEntity(teacherEntity);
						}
						List<TeacherEntity> teacherEntities = teacherRepository.findByTeacherIdsAndExamId(groupEntity
								.getTeachers().stream().map(item -> item.getTeacherId()).collect(Collectors.toList()),
								examId);
						if (CollectionUtils.isNotEmpty(teacherEntities)) {
							for (TeacherEntity teacher : teacherEntities) {
								List<DisponibilityEntity> disp = teacher.getDisponibilities().stream()
										.filter(item -> item.getExamId().equals(examId)).collect(Collectors.toList());
								if (CollectionUtils.isNotEmpty(disp)) {
									teacher.removeDisponibilities(disp);
									teacher.getExamIds().removeIf(it -> it.equals(examId));
									teacherRepository.save(teacher);
									disponibilityRepository.deleteAll(disp);
								}
							}
						}
					}
					if (CollectionUtils.isNotEmpty(groupEntity.getRooms())) {
						List<RoomEntity> rooms = groupEntity.getRooms().stream()
								.filter(el -> el.getExamIds().contains(examId)).collect(Collectors.toList());
						for (RoomEntity roomEntity : rooms) {
							groupEntity.removeRoomEntity(roomEntity);
						}
						List<RoomEntity> roomEntities = roomRepository.findByClassRoomIdsAndExamId(groupEntity
								.getRooms().stream().map(item -> item.getClassRoomId()).collect(Collectors.toList()),
								examId);
						if (CollectionUtils.isNotEmpty(roomEntities)) {
							for (RoomEntity room : roomEntities) {
								List<DisponibilityEntity> disp = room.getDisponibilities().stream()
										.filter(item -> item.getExamId().equals(examId)).collect(Collectors.toList());
								if (CollectionUtils.isNotEmpty(disp)) {
									room.removeDisponibilities(disp);
									room.getExamIds().removeIf(it -> it.equals(examId));
									roomRepository.save(room);
									disponibilityRepository.deleteAll(disp);
								}
							}
						}
					}
					if (CollectionUtils.isNotEmpty(groupEntity.getTeachers())
							|| CollectionUtils.isNotEmpty(groupEntity.getRooms())) {
						groupRepository.save(groupEntity);
					}
				}
			}
			examRepository.deleteById(examId);
		}
	}

	@Override
	public List<String> findExamsBySession(String session) {
		List<ExamEntity> examEntityBySessions = examRepository.findBySession(session);
		List<String> list3 = new ArrayList<>();
		for (ExamEntity examEntityBySession : examEntityBySessions) {
			for (ClassEntity classEntity : examEntityBySession.getClasses()) {
				for (GroupEntity groupEntity : classEntity.getGroups()) {
					for (TeacherEntity teacherEntity : groupEntity.getTeachers()) {
						list3.add(teacherEntity.getTeacherId());
					}
				}
			}
		}
		return list3;
	}

	@Override
	public List<ExamInfoDTO> getExamInfoByClass(String classId) {
		return examRepository.getExamInfoByClass(classId);
	}

	public void check(List<ExamEntity> examEntityBySessions, List<String> list3, String examId,
			List<String> getTeachers, List<String> getUps, List<String> getRooms, List<String> getBloc,
			Date getExamDate, int getExamHour, int getEndHour, String getSession, double getExamDuration,
			String getModule, List<String> getClasses) {

		ExamEntity examEntity = mapExam(examId, getSession, getExamDate, getExamHour, getExamDuration, getModule,
				getClasses);

		List<ClassEntity> classesEntitiesT = mapTeachers(examId, examEntity, getTeachers, getUps, getExamDate,
				getExamHour);
		List<ClassEntity> classesEntities = mapRooms(examId, classesEntitiesT, getRooms, getBloc, getExamDate,
				getExamHour);

		List<String> teachersEntities = new ArrayList<>();
		List<String> roomsEntities = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(classesEntities)) {
			classesEntities.forEach(classe -> {
				classe.getGroups().forEach(group -> {
					if (CollectionUtils.isNotEmpty(group.getTeachers())) {
						teachersEntities.addAll(group.getTeachers().stream().map(item -> item.getTeacherId())
								.collect(Collectors.toList()));
					}
					if (CollectionUtils.isNotEmpty(group.getRooms())) {
						roomsEntities.addAll(group.getRooms().stream().map(item -> item.getClassRoomId())
								.collect(Collectors.toList()));
					}
				});
			});
		}

		checkCommun(examEntityBySessions, list3, getUps, getBloc, getExamDate, getExamHour, getModule, roomsEntities,
				teachersEntities, classesEntities);

		if (CollectionUtils.isNotEmpty(examEntityBySessions)) {
			List<String> classes = classesEntities.stream().map(item -> item.getClassId()).collect(Collectors.toList());
			List<ExamEntity> array1 = examEntityBySessions.stream()
					.filter(item -> !Objects.isNull(item.getModule())
							&& item.getModule().getModuleId().equals(getModule)
							&& item.getClasses().stream().anyMatch(el -> classes.contains(el.getClassId())))
					.collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(array1)) {
				throw new ClassExistInSessionException("Le(s) classe(s) sélectionné(s) déja existe dans cette session");
			}
			List<ExamEntity> array2 = examEntityBySessions.stream()
					.filter(item -> getExamDate.equals(item.getExamDate()) && getExamHour >= item.getExamHour()
							&& getExamHour < item.getEndHour()
							&& item.getClasses().stream().anyMatch(el -> classes.contains(el.getClassId())))
					.collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(array2)) {
				throw new ClassExistInSessionException("Le(s) classe(s) sélectionné(s) déja existe dans cette période");
			}

		}
	}

	public void checkCommun(List<ExamEntity> examEntityBySessions, List<String> list3, List<String> getUps,
			List<String> getBloc, Date getExamDate, int getExamHour, String getModule, List<String> roomsEntities,
			List<String> teachersEntities, List<ClassEntity> classesEntities) {

		/*
		 * List<TeacherEntity> teacherEntitiess = new ArrayList<>(); if
		 * (CollectionUtils.isNotEmpty(examEntityBySessions)) { for (ExamEntity
		 * examEntity : examEntityBySessions) { for (ClassEntity classEntity :
		 * examEntity.getClasses()) { for (AssignClassModuleEntity
		 * assignClassModuleEntity : classEntity.getAssignClassModules()) { for
		 * (TeacherEntity teacherEntity : assignClassModuleEntity.getTeachers()) {
		 * teacherEntitiess.add(teacherEntity); } } } } } if
		 * (CollectionUtils.isNotEmpty(teacherEntitiess) &&
		 * teacherEntitiess.stream().anyMatch(item ->
		 * teachersEntities.contains(item.getTeacherId()))) { }
		 */

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
		if (CollectionUtils.isEmpty(roomsEntities)) {
			List<RoomEntity> roomEntities = new ArrayList<>();
			List<String> rooms = disponibilityRepository.findRommsInd(dateFormat.format(getExamDate), getExamHour);
			if (CollectionUtils.isNotEmpty(rooms)) {
				List<String> data = rooms.stream().filter(Objects::nonNull).collect(Collectors.toList());
				List<RoomEntity> result = roomRepository.findAllByBlocs(getBloc);
				roomEntities = result.stream().filter(item -> !data.contains(item.getClassRoomId()))
						.collect(Collectors.toList());
			} else {
				roomEntities = roomRepository.findAllByBlocs(getBloc);
			}
			if (roomEntities.size() < classesEntities.size() * 2) {
				throw new RoomSizeExecption("The number of rooms is insufficient");
			}
		} else {
			if (roomsEntities.size() < classesEntities.size() * 2) {
				throw new RoomSizeExecption("The number of rooms is insufficient");
			}
		}

		if (CollectionUtils.isEmpty(teachersEntities)) {
			List<TeacherEntity> result = new ArrayList<>();
			List<String> teachers = disponibilityRepository.findTeachrsInd(dateFormat.format(getExamDate), getExamHour);
			if (CollectionUtils.isNotEmpty(teachers)) {
				List<String> data = teachers.stream().filter(Objects::nonNull).collect(Collectors.toList());
				List<TeacherEntity> teacherEntities = teacherRepository.findAllByUps(getUps);
				result = teacherEntities.stream().filter(item -> !data.contains(item.getTeacherId()))
						.collect(Collectors.toList());
			} else {
				result = teacherRepository.findAllByUps(getUps);
			}
			List<String> items = result.stream().map(item -> item.getTeacherId()).collect(Collectors.toList());
			if (!Collections.disjoint(items, list3)) {
				throw new TeachersExistInSessionException(
						"L'un des enseignants sélectionnés est surveillé dans cette session");
			}
			if (result.size() < classesEntities.size() * 2) {
				throw new TeacherSizeExecption("The number of teachers is insufficient");
			}
		} else {
			/*
			 * if (!Collections.disjoint(teachersEntities, list3)) { throw new
			 * TeachersExistInSessionException("L'un des enseignants sélectionnés est surveillé dans cette session"
			 * ); }
			 */
			if (teachersEntities.size() < classesEntities.size() * 2) {
				throw new TeacherSizeExecption("The number of teachers is insufficient");
			}
		}

	}

	private List<ClassEntity> mapTeachers(String examId, ExamEntity examEntity, List<String> getTeachers,
			List<String> getUps, Date getExamDate, int getExamHour) {
		List<ClassEntity> classEntities = examEntity.getClasses();

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
		if (CollectionUtils.isNotEmpty(getTeachers)) {
			List<TeacherEntity> teacherEntities = teacherRepository.findByTeacherIdIn(getTeachers);
			teacherEntities.forEach(item -> item.getExamIds().add(examId));

			long sizeG = classEntities.stream().map(cl -> cl.getGroups()).flatMap(List::stream).count();
			long number = (int) (teacherEntities.size() / sizeG);
			long rest = teacherEntities.size() % sizeG;

			long k = 0;
			for (ClassEntity cl : classEntities) {
				List<TeacherEntity> teachers = new ArrayList<>();
				for (int i = 0; i < number; i++) {
					teachers.add(teacherEntities.get((int) (k + i)));
				}
				cl.getGroups().get(0).addTeachers(teachers);
				k = k + number;
				if (cl.isGroup()) {
					teachers = new ArrayList<>();
					for (int i = 0; i < number; i++) {
						teachers.add(teacherEntities.get((int) (k + i)));
					}
					cl.getGroups().get(1).addTeachers(teachers);
					k = k + number;
				}
			}
			if (rest > 0) {
				for (ClassEntity cl : classEntities) {
					if (k < teacherEntities.size()) {
						cl.getGroups().get(0).addTeacher(teacherEntities.get((int) k));
						k++;
					}
					if (k < teacherEntities.size()) {
						cl.getGroups().get(1).addTeacher(teacherEntities.get((int) k));
						k++;
					}
				}
			}
		} else {
			List<TeacherEntity> result = new ArrayList<>();
			List<String> teachers = disponibilityRepository.findTeachrsInd(dateFormat.format(examEntity.getExamDate()),
					examEntity.getExamHour());
			if (CollectionUtils.isNotEmpty(teachers)) {
				List<String> data = teachers.stream().filter(Objects::nonNull).collect(Collectors.toList());
				List<TeacherEntity> teacherEntities = teacherRepository
						.findAllByUps(getUps.stream().map(item -> item.toLowerCase()).collect(Collectors.toList()));
				result = teacherEntities.stream().filter(item -> !data.contains(item.getTeacherId()))
						.collect(Collectors.toList());
			} else {
				result = teacherRepository
						.findAllByUps(getUps.stream().map(item -> item.toLowerCase()).collect(Collectors.toList()));
			}
			if (result.size() < classEntities.size() * 2) {
				throw new TeacherSizeExecption("The number of teachers is insufficient");
			}
			int k = 0;
			for (ClassEntity cl : classEntities) {
				cl.getGroups().get(0).addTeacher(result.get(k));
				result.get(k).getExamIds().add(examId);
				k++;
				if (cl.isGroup()) {
					TeacherEntity t = result.get(k);
					cl.getGroups().get(1).addTeacher(t);
					result.get(k).getExamIds().add(examId);
					k++;
				}
			}
		}
		return classEntities;
	}

	private List<ClassEntity> mapRooms(String examId, List<ClassEntity> classEntities, List<String> getRooms,
			List<String> getBloc, Date getExamDate, int getExamHour) {
		List<RoomEntity> roomsEntities = null;
		if (CollectionUtils.isNotEmpty(getRooms)) {
			roomsEntities = roomRepository.findByClassRoomIdIn(getRooms);
		} else {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy");
			List<String> rooms = disponibilityRepository.findRommsInd(dateFormat.format(getExamDate), getExamHour);
			if (CollectionUtils.isNotEmpty(rooms)) {
				List<String> data = rooms.stream().filter(Objects::nonNull).collect(Collectors.toList());
				List<RoomEntity> result = roomRepository.findAllByBlocs(getBloc);
				roomsEntities = result.stream().filter(item -> !data.contains(item.getClassRoomId()))
						.collect(Collectors.toList());
			} else {
				roomsEntities = roomRepository.findAllByBlocs(getBloc);
			}
		}
		if (roomsEntities.size() < classEntities.size() * 2) {
			throw new RoomSizeExecption("The number of rooms is insufficient");
		}
		int k = 0;
		for (ClassEntity cl : classEntities) {
			cl.getGroups().get(0).addRoom(roomsEntities.get(k));
			roomsEntities.get(k).getExamIds().add(examId);
			k++;
			if (cl.isGroup()) {
				cl.getGroups().get(1).addRoom(roomsEntities.get(k));
				roomsEntities.get(k).getExamIds().add(examId);
				k++;
			}
		}
		return classEntities;
	}

	private ExamEntity mapExam(String examId, String getSession, Date getExamDate, int getExamHour,
			double getExamDuration, String getModule, List<String> getClasses) {

		ExamEntity examEntity = new ExamEntity().session(sessionRepository.getOne(getSession)).examDate(getExamDate)
				.examHour(getExamHour).examDuration(getExamDuration).module(moduleRepository.getOne(getModule));

		List<ClassEntity> classEntities = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(getClasses)) {
			getClasses.forEach(classe -> {
				ClassEntity classEntity = classRepository.getOne(classe);

				List<GroupEntity> groupEntities = new ArrayList<>();
				classEntity.getGroups().forEach(
						gr -> groupEntities.add(new GroupEntity().groupId(gr.getGroupId()).label(gr.getLabel())));

				classEntities.add(new ClassEntity().classId(classEntity.getClassId()).label(classEntity.getLabel())
						.category(classEntity.getCategory()).email(classEntity.getEmail()).group(classEntity.isGroup())
						.speciality(classEntity.getSpeciality()).groups(groupEntities));
				// classEntities.forEach(item -> item.addGroups(groupEntities));
			});
		}
		for (ClassEntity classEntity : classEntities) {
			examEntity.addClassEntity(classEntity);
		}
		return examEntity.examId(examId);
	}

	private ExamEntity addExistTeachersAndRoomsToCurrentGroups(ExamEntity examEntity) {
		List<String> classIds = examEntity.getClasses().stream().map(item -> item.getClassId())
				.collect(Collectors.toList());
		for (String classId : classIds) {
			List<GroupEntity> groupEntitiesG1 = groupRepository.findByClassG1(classId);
			if (CollectionUtils.isNotEmpty(groupEntitiesG1)) {
				List<RoomEntity> roomsG1 = new ArrayList<>();
				List<TeacherEntity> teachersG1 = new ArrayList<>();
				for (GroupEntity el : groupEntitiesG1) {
					if (CollectionUtils.isNotEmpty(el.getRooms())) {
						roomsG1.addAll(el.getRooms());
					}
					if (CollectionUtils.isNotEmpty(el.getTeachers())) {
						teachersG1.addAll(el.getTeachers());
					}
				}
				examEntity.getClasses().forEach(item -> {
					if (item.getClassId().equals(classId)) {
						item.getGroups().forEach(el -> {
							if (el.getLabel().equalsIgnoreCase("Groupe 1")) {
								if (CollectionUtils.isNotEmpty(roomsG1)) {
									el.addRooms(roomsG1);
								}
								if (CollectionUtils.isNotEmpty(teachersG1)) {
									el.addTeachers(teachersG1);
								}
							}
						});
					}
				});
			}

			List<GroupEntity> groupEntitiesG2 = groupRepository.findByClassG2(classId);
			if (CollectionUtils.isNotEmpty(groupEntitiesG2)) {
				List<RoomEntity> roomsG1 = new ArrayList<>();
				List<TeacherEntity> teachersG1 = new ArrayList<>();
				for (GroupEntity el : groupEntitiesG2) {
					if (CollectionUtils.isNotEmpty(el.getRooms())) {
						roomsG1.addAll(el.getRooms());
					}
					if (CollectionUtils.isNotEmpty(el.getTeachers())) {
						teachersG1.addAll(el.getTeachers());
					}
				}
				examEntity.getClasses().forEach(item -> {
					if (item.getClassId().equals(classId)) {
						item.getGroups().forEach(el -> {
							if (el.getLabel().equalsIgnoreCase("Groupe 2")) {
								if (CollectionUtils.isNotEmpty(roomsG1)) {
									el.addRooms(roomsG1);
								}
								if (CollectionUtils.isNotEmpty(teachersG1)) {
									el.addTeachers(teachersG1);
								}
							}
						});
					}
				});
			}
		}
		return examEntity;
	}

	private void updateDiponibilties(String examId, List<String> getTeachers, List<String> getUps,
			List<String> getRooms, List<String> getBloc, Date getExamDate, int getExamHour, int getEndHour,
			SessionDTO getSession, double getExamDuration, String getModule, List<String> getClasses) {

		ExamEntity examEntity = mapExam(examId, getSession.getSessionId(), getExamDate, getExamHour, getExamDuration,
				getModule, getClasses);

		List<ClassEntity> classesEntitiesT = mapTeachers(examId, examEntity, getTeachers, getUps, getExamDate,
				getExamHour);
		List<ClassEntity> classesEntities = mapRooms(examId, classesEntitiesT, getRooms, getBloc, getExamDate,
				getExamHour);

		for (ClassEntity classEntity : classesEntities) {
			for (GroupEntity groupEntity : classEntity.getGroups()) {
				SessionEntity session = sessionRepository.getOne(getSession.getSessionId());
				for (TeacherEntity teacher : groupEntity.getTeachers()) {
					teacher.addDisponibility(new DisponibilityEntity().startDate(getExamDate).endDate(getExamDate)
							.startHour(getExamHour).endHour(getEndHour).motif("Surveillance").examId(examId)
							.session(session).examDuration(getExamDuration));
					teacherRepository.save(teacher);
				}
				for (RoomEntity room : groupEntity.getRooms()) {
					String motif = null;
					if (getSession.getLabel().equalsIgnoreCase("S1P1")
							|| getSession.getLabel().equalsIgnoreCase("S2P1")) {
						motif = "DS";
					} else if (getSession.getLabel().equalsIgnoreCase("S1P2")
							|| getSession.getLabel().equalsIgnoreCase("S2P2")
							|| getSession.getLabel().equalsIgnoreCase("SP")) {
						motif = "Examen";
					} else if (getSession.getLabel().equalsIgnoreCase("SR")) {
						motif = "Rattrapage";
					}
					room.addDisponibility(new DisponibilityEntity().startDate(getExamDate).endDate(getExamDate)
							.startHour(getExamHour).endHour(getEndHour).motif(motif).examId(examId).session(session)
							.examDuration(getExamDuration));
					roomRepository.save(room);
				}
			}
		}
	}

}