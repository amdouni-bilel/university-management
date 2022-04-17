package com.esprit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.esprit.service.MigrationService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("migration")
public class MigrationController {

	private final MigrationService service;

	public MigrationController(MigrationService service) {
		this.service = service;
	}

	@ApiOperation(value = "Récupérer la liste des classes")
	@GetMapping("classes")
	public ResponseEntity<List<CreateClassRequest>> selectClasses() {
		return new ResponseEntity<>(service.selectClasses(), HttpStatus.OK);
	}

	@ApiOperation(value = "Récupérer classe par id")
	@GetMapping("{classId}")
	public ResponseEntity<CreateClassRequest> selectClass(@PathVariable String classId) {
		return new ResponseEntity<>(service.selectClass(classId), HttpStatus.OK);
	}

	@ApiOperation(value = "Inserer la liste des classes automatiquement")
	@PostMapping("classes/automatically")
	public ResponseEntity<Void> insertClasses() {
		service.insertClasses();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Inserer la liste des classes manuellement")
	@PostMapping("classes")
	public ResponseEntity<Void> insertClasses(@RequestBody List<CreateClassRequest> classes) {
		service.insertClasses(classes);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Récupérer la liste des departements")
	@GetMapping("departments")
	public ResponseEntity<List<DepartmentDTO>> selectDepartments() {
		return new ResponseEntity<>(service.selectDepartments(), HttpStatus.OK);
	}

	@ApiOperation(value = "Inserer la liste des departements automatiquement")
	@PostMapping("departments")
	public ResponseEntity<Void> insertDepartments() {
		service.insertDepartments();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Récupérer la liste des semaines")
	@GetMapping("weeks")
	public ResponseEntity<List<WeekMigration>> selectWeeks() {
		return new ResponseEntity<>(service.selectWeek(), HttpStatus.OK);
	}

	@ApiOperation(value = "Récupérer la liste des jours")
	@GetMapping("days")
	public ResponseEntity<List<DayMigration>> selectDays() {
		return new ResponseEntity<>(service.selectDays(), HttpStatus.OK);
	}

	@ApiOperation(value = "Récupérer un jour by id")
	@GetMapping("day/{dayId}")
	public ResponseEntity<DayMigration> selectDayById(@PathVariable String dayId) {
		return new ResponseEntity<>(service.selectDayById(dayId), HttpStatus.OK);
	}

	@ApiOperation(value = "Récupérer la liste des modules")
	@GetMapping("modules")
	public ResponseEntity<List<CreateModuleRequest>> selectModules() {
		return new ResponseEntity<>(service.selectModules(), HttpStatus.OK);
	}

	@ApiOperation(value = "Inserer la liste des modules automatiquement")
	@PostMapping("modules/automatically")
	public ResponseEntity<Void> insertModules() {
		service.insertModules();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Inserer la liste des modules manuellement")
	@PostMapping("modules")
	public ResponseEntity<Void> insertModules(@RequestBody List<CreateModuleRequest> modules) {
		service.insertModules(modules);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Récupérer la liste des périodes")
	@GetMapping("periods")
	public ResponseEntity<List<PeriodMigration>> selectPeriods() {
		return new ResponseEntity<>(service.selectPeriod(), HttpStatus.OK);
	}

	@ApiOperation(value = "Inserer la liste des périodes automatiquement")
	@PostMapping("periods/automatically")
	public ResponseEntity<Void> insertPeriods() {
		service.insertPeriods();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Récupérer la liste des salles")
	@GetMapping("rooms")
	public ResponseEntity<List<CreateRoomRequest>> selectRooms() {
		return new ResponseEntity<>(service.selectRooms(), HttpStatus.OK);
	}

	@ApiOperation(value = "Inserer la liste des salles automatiquement")
	@PostMapping("rooms/automatically")
	public ResponseEntity<Void> insertRooms() {
		service.insertRooms();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Inserer la liste des salles manuellement")
	@PostMapping("rooms")
	public ResponseEntity<Void> insertRooms(@RequestBody List<CreateRoomRequest> rooms) {
		service.insertRooms(rooms);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Récupérer la liste des séances")
	@GetMapping("seances")
	public ResponseEntity<List<CreateSeanceRequest>> selectSeances() {
		return new ResponseEntity<>(service.selectSeances(), HttpStatus.OK);
	}

	@ApiOperation(value = "Inserer la liste des séances automatiquement")
	@PostMapping("seances/automatically")
	public ResponseEntity<Void> insertSeances() {
		service.insertSeances();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@ApiOperation(value = "Récupérer un seance by id")
	@GetMapping("{seanceId}")
	public ResponseEntity<CreateSeanceRequest> selectSeanceById(@PathVariable String seanceId) {
		return new ResponseEntity<>(service.selectSeanceById(seanceId), HttpStatus.OK);
	}

	@ApiOperation(value = "Récupérer la liste des semestres")
	@GetMapping("semesters")
	public ResponseEntity<List<SemesterDTO>> selectSemesters() {
		return new ResponseEntity<>(service.selectSemester(), HttpStatus.OK);
	}

	@ApiOperation(value = "Inserer la liste des semestres automatiquement")
	@PostMapping("semesters/automatically")
	public ResponseEntity<Void> insertSemester() {
		service.insertSemester();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Récupérer un semestre by id")
	@GetMapping("{semesterId}")
	public ResponseEntity<SemesterDTO> selectSemesterById(@PathVariable String semesterId) {
		return new ResponseEntity<>(service.selectSemesterById(semesterId), HttpStatus.OK);
	}

	@ApiOperation(value = "Récupérer la liste des specialités")
	@GetMapping("specialities")
	public ResponseEntity<List<SpecialityDTO>> selectSpecialities() {
		return new ResponseEntity<>(service.selectSpecialities(), HttpStatus.OK);
	}

	@ApiOperation(value = "Inserer la liste des specialités automatiquement")
	@PostMapping("specialities/automatically")
	public ResponseEntity<Void> insertSpecialities() {
		service.insertSpecialities();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Récupérer la liste des enseignants")
	@GetMapping("teachers")
	public ResponseEntity<List<CreateTeacherRequest>> selectTeachers() {
		return new ResponseEntity<>(service.selectTeachers(), HttpStatus.OK);
	}

	@ApiOperation(value = "Inserer la liste des enseignants automatiquement")
	@PostMapping("teachers/automatically")
	public ResponseEntity<Void> insertTeachers() {
		service.insertTeachers();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Inserer la liste des enseignants manuellement")
	@PostMapping("teachers")
	public ResponseEntity<Void> insertTeachers(@RequestBody List<CreateTeacherRequest> teachers) {
		service.insertTeachers(teachers);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Récupérer la liste des utilisateurs")
	@GetMapping("users")
	public ResponseEntity<List<CreateUserRequest>> selectUsers() {
		return new ResponseEntity<>(service.selectUsers(), HttpStatus.OK);
	}

	@ApiOperation(value = "Inserer la liste des utilisateurs automatiquement")
	@PostMapping("users/automatically")
	public ResponseEntity<Void> insertUsersAut() {
		service.insertUsers();
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@ApiOperation(value = "Inserer la liste des utilisateurs manuellement")
	@PostMapping("users")
	public ResponseEntity<Void> insertUsers(List<CreateUserRequest> users) {
		service.insertUsers(users);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

}