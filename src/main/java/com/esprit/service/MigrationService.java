package com.esprit.service;

import java.util.List;

import com.esprit.dto.DepartmentDTO;
import com.esprit.dto.SemesterDTO;
import com.esprit.dto.SpecialityDTO;
import com.esprit.dto.classes.CreateClassRequest;
import com.esprit.dto.migration.DayMigration;
import com.esprit.dto.migration.PeriodMigration;
import com.esprit.dto.migration.WeekMigration;
import com.esprit.dto.module.CreateModuleRequest;
import com.esprit.dto.room.CreateRoomRequest;
import com.esprit.dto.seance.CreateSeanceRequest;
import com.esprit.dto.teacher.CreateTeacherRequest;
import com.esprit.dto.user.CreateUserRequest;

public interface MigrationService {

	List<CreateClassRequest> selectClasses();

	CreateClassRequest selectClass(String classId);

	void insertClasses();

	void insertClasses(List<CreateClassRequest> classes);

	List<DepartmentDTO> selectDepartments();

	void insertDepartments();

	List<WeekMigration> selectWeek();

	List<DayMigration> selectDays();

	DayMigration selectDayById(String dayId);

	List<CreateModuleRequest> selectModules();

	void insertModules();

	void insertModules(List<CreateModuleRequest> modules);

	List<PeriodMigration> selectPeriod();

	void insertPeriods();

	List<CreateRoomRequest> selectRooms();

	void insertRooms();

	void insertRooms(List<CreateRoomRequest> rooms);

	List<CreateSeanceRequest> selectSeances();

	void insertSeances();

	CreateSeanceRequest selectSeanceById(String seanceId);

	List<SemesterDTO> selectSemester();

	void insertSemester();

	SemesterDTO selectSemesterById(String semesterId);

	List<SpecialityDTO> selectSpecialities();

	void insertSpecialities();

	List<CreateTeacherRequest> selectTeachers();

	void insertTeachers();

	void insertTeachers(List<CreateTeacherRequest> teachers);

	List<CreateUserRequest> selectUsers();

	void insertUsers();

	void insertUsers(List<CreateUserRequest> users);

}