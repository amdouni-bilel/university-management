package com.esprit.service.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.esprit.dto.CreateDisponibilityRequest;
import com.esprit.dto.DepartmentDTO;
import com.esprit.dto.PeriodDTO;
import com.esprit.dto.SemesterDTO;
import com.esprit.dto.SpecialityDTO;
import com.esprit.dto.WeekDTO;
import com.esprit.dto.classes.CreateClassRequest;
import com.esprit.dto.migration.DayMigration;
import com.esprit.dto.migration.PeriodMigration;
import com.esprit.dto.migration.WeekMigration;
import com.esprit.dto.module.AssignClassToModuleRequest;
import com.esprit.dto.module.CreateModuleRequest;
import com.esprit.dto.room.CreateRoomRequest;
import com.esprit.dto.seance.CreateSeanceRequest;
import com.esprit.dto.teacher.CreateTeacherRequest;
import com.esprit.dto.user.CreateUserRequest;
import com.esprit.service.ClassService;
import com.esprit.service.DepartmentService;
import com.esprit.service.IAMService;
import com.esprit.service.MigrationService;
import com.esprit.service.ModuleService;
import com.esprit.service.PeriodService;
import com.esprit.service.RoomService;
import com.esprit.service.SeanceService;
import com.esprit.service.SemesterService;
import com.esprit.service.SpecialityService;
import com.esprit.service.TeacherService;
import com.esprit.service.mapper.migration.ClassRowMapper;
import com.esprit.service.mapper.migration.DayRowMapper;
import com.esprit.service.mapper.migration.SeanceRowMapper;
import com.esprit.service.mapper.migration.SemesterRowMapper;

@Service
public class MigrationServiceImpl implements MigrationService {

	private static final String queryClass = "select * from CLASSE";

	private static final String querylassById = "select * from CLASSE where CODE_CL = ?";

	private static final String queryDepartment = "select distinct d.CODE_DEPT, d.LIBELLE_DEPT, EE.UP\n"
			+ "from ESP_DEPT d\n" + "         left join ESP_ENSEIGNANT EE\n"
			+ "                   on d.CODE_DEPT = EE.CODE_DEPT";

	private static final String queryWeek = "select * from semaine";

	private static final String queryDay = "select * from JOURNEE";

	private static final String queryDayById = "select * from JOURNEE where CODEJOUR = ?";

	private static final String queryModule = "select \n" + 
			"m.CODE_MODULE,\n" + 
			"m.DESCRIPTION,\n" + 
			"m.UP,\n" + 
			"mp.CODE_CL,\n" + 
			"mp.ANNEE_DEB,\n" + 
			"mp.ANNEE_FIN,\n" + 
			"mp.NB_HEURES,\n" + 
			"mp.COEF,\n" + 
			"mp.ID_ENS,\n" + 
			"mp.ID_ENS2,\n" + 
			"mp.PERIODE,\n" + 
			"mp.TYPE_EPREUVE,\n" + 
			"sm.ID as \"IDSM\",\n" + 
			"P.ID as \"IDPER\"\n" + 
			"from ESP_MODULE m\n" + 
			"LEFT JOIN ESP_MODULE_PANIER_CLASSE_SAISO mp ON m.CODE_MODULE = mp.CODE_MODULE\n" + 
			"LEFT JOIN SEMESTRE sm ON sm.IDSEM = mp.NUM_SEMESTRE AND sm.ANNEUNIVERSITAIRE LIKE '%' || mp.ANNEE_DEB || '%'\n" + 
			"LEFT JOIN PERIODE P on mp.PERIODE = P.IDPER AND sm.ID = P.IDSEM WHERE ANNEE_DEB > '2016' AND sm.ANNEUNIVERSITAIRE = '2017/2018'";

	private static final String queryPeriod = "select \n" + 
			"p.ID as \"ID_PER\",\n" + 
			"p.IDPER as \"COD_PER\",  \n" + 
			"p.DATEDEB as \"DATE_DEB_PER\",\n" + 
			"p.DATFIN as \"DAT_FIN_PER\",\n" + 
			"w.ID as \"ID_WEEK\",\n" + 
			"w.DATEDEB as \"DATE_DEB_WEEK\",\n" + 
			"w.DATFIN  as \"DAT_FIN_WEEK\"\n" + 
			"from PERIODE p inner join SEMAINE w on p.ID = w.IDPER order by p.ID";
	
	private static final String queryRoom = "SELECT\n" + 
			"    s.code_salle as \"roomCode\",\n" + 
			"    s.description as \"roomLabel\",\n" + 
			"    s.pole as \"pole\",\n" + 
			"    disp.MOTIFDISP as \"reason\",\n" + 
			"    jr.NOMJOUR as \"dayCode\",\n" + 
			"    disp.CODSEANCE as \"seanceCode\",\n" + 
			"    smn.ID as \"weekId\",\n" + 
			"    smt.ID as \"numSemester\",\n" + 
			"    smt.ANNEUNIVERSITAIRE as \"year\",\n" + 
			"    prd.ID as \"numPeriod\"\n" + 
			"FROM SALLE s\n" + 
			"         LEFT JOIN DISPONIBILITESEMAINESALLE disp ON s.CODE_SALLE = disp.CODSAL\n" + 
			"         LEFT JOIN SEMAINE smn ON smn.ID = disp.IDSEMAINE\n" + 
			"         LEFT JOIN SEMESTRE smt ON smt.ID = disp.IDSEM\n" + 
			"         LEFT JOIN PERIODE prd ON prd.ID = smn.IDPER\n" + 
			"         LEFT JOIN JOURNEE jr ON jr.CODEJOUR = disp.CODJOUR\n" + 
			"WHERE smn.ID IS NOT NULL AND jr.NOMJOUR IS NOT NULL AND s.POLE <> 'a' AND smt.ANNEUNIVERSITAIRE = '2017/2018'";

	private static final String querySeance = "select * from seance";

	private static final String querySeanceById = "select * from seance where CODESEANCE = ?";
	
	private static final String querySemester = "select\n" + 
			"    s.ID as \"ID_SEM\",\n" + 
			"    s.IDSEM as \"COD_SEM\",\n" + 
			"    s.DATEDEB as \"DATE_DEB_SEM\",\n" + 
			"    s.DATFIN as \"DAT_FIN_SEM\",\n" + 
			"    p.ID as \"ID_PER\",\n" + 
			"    p.IDPER as \"COD_PER\",\n" + 
			"    p.DATEDEB as \"DATE_DEB_PER\",\n" + 
			"    p.DATFIN  as \"DAT_FIN_PER\",\n" + 
			"    sm.ID as \"ID_SEMT\",\n" + 
			"    sm.DATEDEB as \"DATE_DEB_SEMT\",\n" + 
			"    sm.DATFIN as \"DATE_FIN_SEMT\"\n" + 
			"    from SEMESTRE s\n" + 
			"    left join PERIODE p on s.ID = p.IDSEM\n" + 
			"    left join SEMAINE sm on sm.IDPER = p.ID order by s.ID";

	private static final String querySemesterById = "select\n" + 
			"s.ID as \"ID_SEM\",\n" + 
			"s.IDSEM as \"COD_SEM\",\n" + 
			"s.DATEDEB as \"DATE_DEB_SEM\",\n" + 
			"s.DATFIN as \"DAT_FIN_SEM\",\n" + 
			"p.ID as \"ID_PER\",\n" + 
			"p.IDPER as \"COD_PER\",\n" + 
			"p.DATEDEB as \"DATE_DEB_PER\",\n" + 
			"p.DATFIN  as \"DAT_FIN_PER\"\n" + 
			"from SEMESTRE s inner join PERIODE p on s.ID = p.IDSEM where s.ID = ?";

	private static final String querySpeciality = "SELECT\n" + 
			"    c.CODE_SPECIALITE, c.LIB_SPECIALITE\n" + 
			"FROM\n" + 
			"    CLASSE c\n" + 
			"GROUP BY\n" + 
			"    c.CODE_SPECIALITE, c.LIB_SPECIALITE\n" + 
			"HAVING\n" + 
			"        COUNT(*) > 1 and c.CODE_SPECIALITE is not null and c.LIB_SPECIALITE is not null";
	
	private static final String queryTeacher = "SELECT\n" + 
			"			    ens.ID_ENS,\n" + 
			"			    ens.CIN,\n" + 
			"			    ens.SEXE_ENS,\n" + 
			"			    ens.NOM_ENS,\n" + 
			"			    ens.MAIL_ENS,\n" + 
			"			    ens.TEL1,\n" + 
			"			    ens.UP,\n" + 
			"			    ens.TYPE_ENS,\n" + 
			"			    ens.CODE_DEPT,\n" + 
			"			    jr.NOMJOUR as \"dayCode\",\n" + 
			"			    disp.CODSEANCE as \"seanceCode\",\n" + 
			"			    smn.ID as \"weekId\",\n" + 
			"			    smt.ID as \"numSemester\",\n" + 
			"			    smt.ANNEUNIVERSITAIRE as \"year\"\n" + 
			"			FROM ESP_ENSEIGNANT ens\n" + 
			"			         LEFT JOIN DISPONIBILITESEMAINE disp ON ens.ID_ENS = disp.IDENS\n" + 
			"			         LEFT JOIN SEMAINE smn ON smn.ID = disp.IDSEMAINE\n" + 
			"			         LEFT JOIN SEMESTRE smt ON smt.ID = disp.IDSEM\n" + 
			"			         LEFT JOIN JOURNEE jr ON jr.CODEJOUR = disp.CODJOUR\n" + 
	        "            WHERE smn.ID IS NOT NULL AND jr.NOMJOUR IS NOT NULL AND smt.ANNEUNIVERSITAIRE = '2017/2018'";

	private static final String queryUser = "SELECT * FROM utilisateur WHERE ROWID IN ( SELECT MAX(ROWID) FROM utilisateur GROUP BY LOGIN ) "
			+ "AND TYPEUSER <> 'classe' AND TYPEUSER <> 'sco'  AND TYPEUSER <> 'pfe' AND EMAIL IS NOT NULL";

	private boolean departmentDoesNotExist;

	private boolean moduleDoesNotExist;

	private boolean periodDoesNotExist;

	private boolean roomDoesNotExist;

	private boolean semesterDoesNotExist;

	private boolean teacherDoesNotExist;

	@Qualifier("scoemploiJdbc")
	private final JdbcTemplate jdbcTemplate;

	private final ClassService classService;

	private final DepartmentService departmentService;

	private final ModuleService moduleService;

	private final PeriodService periodService;

	private final RoomService roomService;

	private final SeanceService seanceService;

	private final SemesterService semesterService;

	private final SpecialityService specialityService;

	private final TeacherService teacherService;

	private final IAMService iAMService;

	public MigrationServiceImpl(JdbcTemplate jdbcTemplate, ClassService classService,
			DepartmentService departmentService, ModuleService moduleService, PeriodService periodService,
			RoomService roomService, SeanceService seanceService, SemesterService semesterService,
			SpecialityService specialityService, TeacherService teacherService, IAMService iAMService) {
		this.jdbcTemplate = jdbcTemplate;
		this.classService = classService;
		this.departmentService = departmentService;
		this.moduleService = moduleService;
		this.periodService = periodService;
		this.roomService = roomService;
		this.seanceService = seanceService;
		this.semesterService = semesterService;
		this.specialityService = specialityService;
		this.teacherService = teacherService;
		this.iAMService = iAMService;
	}

	@Override
	public List<CreateClassRequest> selectClasses() {
		List<CreateClassRequest> classes = new ArrayList<>();
		return jdbcTemplate.query(queryClass, (ResultSet rs) -> {
			while (rs.next()) {
				classes.add(new CreateClassRequest().classId(rs.getString("CODE_CL")).label(rs.getString("LIBELLE_CL"))
						.category(rs.getString("CATEGORIE")).email(rs.getString("MAIL"))
						.speciality(rs.getString("CODE_SPECIALITE")));
			}
			return classes;
		});
	}

	@Override
	public CreateClassRequest selectClass(String classId) {
		return jdbcTemplate.queryForObject(querylassById, new ClassRowMapper(), new Object[] { classId });
	}

	@Override
	public void insertClasses() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(selectClasses())) {
			selectClasses().forEach(item -> classService.addClass(item));
		}
	}

	@Override
	public void insertClasses(List<CreateClassRequest> classes) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(classes)) {
			classes.forEach(item -> classService.addClass(item));
		}
	}

	@Override
	public List<DepartmentDTO> selectDepartments() {
		List<DepartmentDTO> departments = new ArrayList<>();
		return jdbcTemplate.query(queryDepartment, (ResultSet rs) -> {
			while (rs.next()) {
				departmentDoesNotExist = true;
				String departmentCode = rs.getString("CODE_DEPT");

				List<String> ups = new ArrayList<>();
				ups.add(rs.getString("UP"));

				departments.forEach(department -> {
					if (department.getDepartmenetId().equals(departmentCode)) {
						department.getUp().addAll(ups);
						departmentDoesNotExist = false;
					}
				});

				if (departmentDoesNotExist) {
					departments.add(new DepartmentDTO().departmenetId(rs.getString("CODE_DEPT"))
							.label(rs.getString("LIBELLE_DEPT")).up(ups));
				}

			}
			return departments;
		});
	}

	@Override
	public void insertDepartments() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(selectDepartments())) {
			selectDepartments().forEach(item -> departmentService.addDepartment(item));
		}
	}

	@Override
	public List<WeekMigration> selectWeek() {
		List<WeekMigration> weeks = new ArrayList<>();
		return jdbcTemplate.query(queryWeek, (ResultSet rs) -> {
			while (rs.next()) {
				weeks.add(new WeekMigration().weekId(rs.getString("ID")).startDate(rs.getDate("DATEDEB"))
						.endDate(rs.getDate("DATFIN")).periodCode(rs.getString("IDPER"))
						.semesterCode(rs.getString("IDSEM")));
			}
			return weeks;
		});
	}

	@Override
	public List<DayMigration> selectDays() {
		List<DayMigration> days = new ArrayList<>();
		return jdbcTemplate.query(queryDay, (ResultSet rs) -> {
			while (rs.next()) {
				days.add(new DayMigration().dayId(rs.getString("CODEJOUR")).label(rs.getString("NOMJOUR")));
			}
			return days;
		});
	}

	@Override
	public DayMigration selectDayById(String dayId) {
		return jdbcTemplate.queryForObject(queryDayById, new DayRowMapper(), new Object[] { dayId });
	}

	@Override
	public List<CreateModuleRequest> selectModules() {
		List<CreateModuleRequest> modules = new ArrayList<>();

		return jdbcTemplate.query(queryModule, (ResultSet rs) -> {
			while (rs.next()) {
				moduleDoesNotExist = true;
				String moduleCode = rs.getString("CODE_MODULE");

				List<AssignClassToModuleRequest> assignClasses = new ArrayList<>();
				AssignClassToModuleRequest assignClass = new AssignClassToModuleRequest()
						.classId(rs.getString("CODE_CL")).coefficient(rs.getDouble("COEF"))
						.teacherIds(Arrays.asList(rs.getString("ID_ENS"), rs.getString("ID_ENS2")))
						.nbrHour(rs.getDouble("NB_HEURES")).periodId(Arrays.asList(rs.getString("IDPER")))
						.semesterId(rs.getString("IDSM"));

				if (!StringUtils.isBlank(rs.getString("TYPE_EPREUVE"))) {
					String examType = null;
					if (rs.getString("TYPE_EPREUVE").equals("01")) {
						examType = "Écrit";
					}
					if (rs.getString("TYPE_EPREUVE").equals("02")) {
						examType = "Pratique";
					} else if (rs.getString("TYPE_EPREUVE").equals("03")) {
						examType = "Soutenance";
					}
					assignClass.typeExam(examType);
				}

				assignClasses.add(assignClass);

				modules.forEach(module -> {
					if (module.getModuleId().equals(moduleCode)) {
						module.getAssignClasses().addAll(assignClasses);
						moduleDoesNotExist = false;
					}
				});

				if (moduleDoesNotExist) {
					modules.add(new CreateModuleRequest().moduleId(moduleCode).designation(rs.getString("DESCRIPTION"))
							.up(rs.getString("UP")).assignClasses(assignClasses));
				}
			}
			return modules;
		});
	}

	@Override
	public void insertModules() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(selectModules())) {
			selectModules().forEach(item -> moduleService.addModule(item));
		}
	}

	@Override
	public void insertModules(List<CreateModuleRequest> modules) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(modules)) {
			modules.forEach(item -> moduleService.addModule(item));
		}
	}

	@Override
	public List<PeriodMigration> selectPeriod() {
		List<PeriodMigration> periods = new ArrayList<>();
		return jdbcTemplate.query(queryPeriod, (ResultSet rs) -> {
			while (rs.next()) {
				periodDoesNotExist = true;
				String periodId = rs.getString("ID_PER");

				List<WeekMigration> weeks = new ArrayList<>();
				weeks.add(new WeekMigration().weekId(rs.getString("ID_WEEK")).startDate(rs.getDate("DATE_DEB_WEEK"))
						.endDate(rs.getDate("DAT_FIN_WEEK")));

				for (PeriodMigration period : periods) {
					if (period.getPeriodId().equals(periodId) && CollectionUtils.isNotEmpty(weeks)) {
						period.getWeeks().addAll(weeks);
						periodDoesNotExist = false;
					}
				}
				;

				if (periodDoesNotExist) {
					periods.add(new PeriodMigration().periodId(periodId).code(rs.getString("COD_PER"))
							.startDate(rs.getDate("DATE_DEB_PER")).endDate(rs.getDate("DAT_FIN_PER")));
				}

			}
			return periods;
		});
	}

	@Override
	public void insertPeriods() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
	}

	@Override
	public List<CreateRoomRequest> selectRooms() {
		List<CreateRoomRequest> rooms = new ArrayList<>();
		return jdbcTemplate.query(queryRoom, (ResultSet rs) -> {
			while (rs.next()) {
				roomDoesNotExist = true;
				String roomCode = rs.getString("roomCode");

				List<CreateDisponibilityRequest> disponibilities = new ArrayList<>();

				CreateDisponibilityRequest disponibility = new CreateDisponibilityRequest();
				if (!StringUtils.isBlank(rs.getString("dayCode"))) {
					disponibility.day(rs.getString("dayCode"));
				}
				if (!StringUtils.isBlank(rs.getString("seanceCode"))) {
					disponibility.seanceId(rs.getString("seanceCode"));
				}
				if (!StringUtils.isBlank(rs.getString("weekId"))) {
					disponibility.weekId(rs.getString("weekId"));
				}
				if (!StringUtils.isBlank(rs.getString("year"))) {
					disponibility.year(rs.getString("year"));
				}
				if (!StringUtils.isBlank(rs.getString("reason"))) {
					disponibility.motif(rs.getString("reason"));
				}
				if (!StringUtils.isBlank(rs.getString("numSemester"))) {
					disponibility.semesterId(rs.getString("numSemester"));
				}
				if (!StringUtils.isBlank(rs.getString("numPeriod"))) {
					disponibility.periodId(rs.getString("numPeriod"));
				}
				if (disponibility != null) {
					disponibilities.add(disponibility);
				}

				rooms.forEach(room -> {
					if (room.getClassRoomId().equals(roomCode)) {
						room.getDisponibilities().addAll(disponibilities);
						roomDoesNotExist = false;
					}
				});

				if (roomDoesNotExist) {
					CreateRoomRequest room = new CreateRoomRequest().classRoomId(roomCode)
							.label(rs.getString("roomLabel")).pole(rs.getString("pole"))
							.disponibilities(disponibilities);
					rooms.add(room);
				}

			}
			return rooms;
		});
	}

	@Override
	public void insertRooms() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(selectRooms())) {
			selectRooms().forEach(item -> roomService.addClassRoom(item));
		}
	}

	@Override
	public void insertRooms(List<CreateRoomRequest> rooms) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(rooms)) {
			rooms.forEach(item -> roomService.addClassRoom(item));
		}
	}

	@Override
	public List<CreateSeanceRequest> selectSeances() {
		List<CreateSeanceRequest> meetings = new ArrayList<>();
		return jdbcTemplate.query(querySeance, (ResultSet rs) -> {
			while (rs.next()) {
				meetings.add(new CreateSeanceRequest().seanceId(rs.getString("CODESEANCE"))
						.startHour(Integer.parseInt(rs.getString("HEUREDEB").replace("H:", "")))
						.endHour(Integer.parseInt(rs.getString("HEUREFIN").replace("H:", ""))));
			}
			return meetings;
		});
	}

	@Override
	public void insertSeances() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(selectSeances())) {
			selectSeances().forEach(item -> seanceService.addSeance(item));
		}
	}

	@Override
	public CreateSeanceRequest selectSeanceById(String semesterId) {
		return jdbcTemplate.queryForObject(querySeanceById, new SeanceRowMapper(), new Object[] { semesterId });
	}

	@Override
	public List<SemesterDTO> selectSemester() {
		List<SemesterDTO> semesters = new ArrayList<>();
		return jdbcTemplate.query(querySemester, (ResultSet rs) -> {
			while (rs.next()) {
				semesterDoesNotExist = true;
				String semesterId = rs.getString("ID_SEM");

				List<WeekDTO> weeks = new ArrayList<>();
				weeks.add(new WeekDTO().weekId(rs.getString("ID_SEMT")).startDate(rs.getDate("DATE_DEB_SEMT"))
						.endDate(rs.getDate("DATE_FIN_SEMT")));

				List<PeriodDTO> periods = new ArrayList<>();
				periods.add(new PeriodDTO().periodId(rs.getString("ID_PER")).code(rs.getString("COD_PER"))
						.startDate(rs.getDate("DATE_DEB_PER")).endDate(rs.getDate("DAT_FIN_PER")).weeks(weeks));

				semesters.forEach(semester -> {
					if (semester.getSemestreId().equals(semesterId)) {
						semester.getPeriods().addAll(periods);
						semesterDoesNotExist = false;
					}
				});

				if (semesterDoesNotExist) {
					semesters.add(new SemesterDTO().semestreId(semesterId).code(rs.getString("COD_SEM"))
							.startDate(rs.getDate("DATE_DEB_SEM")).endDate(rs.getDate("DAT_FIN_SEM")).periods(periods));
				}

			}

			semesters.forEach(semester -> {
				PeriodDTO firstPeriod = semester.getPeriods().get(0);
				List<PeriodDTO> p = new ArrayList<>(Arrays.asList(firstPeriod));
				semester.getPeriods().forEach(periode -> {
					Optional<PeriodDTO> optinalP = p.stream()
							.filter(period -> period.getCode().equals(periode.getCode())).findFirst();
					if (optinalP.isPresent()) {
						p.remove(optinalP.get());
						PeriodDTO copyP = optinalP.get();
						copyP.getWeeks().addAll(periode.getWeeks());
						p.add(copyP);
					} else {
						p.add(periode);
					}
				});
				p.forEach(el -> el.weeks(el.getWeeks().stream().distinct().collect(Collectors.toList())));
				semester.periods(p);
			});

			return semesters;
		});
	}

	@Override
	public SemesterDTO selectSemesterById(String seanceId) {
		return jdbcTemplate.query(querySemesterById, new SemesterRowMapper(), seanceId).get(0);
	}

	@Override
	public void insertSemester() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(selectSemester())) {
			selectSemester().forEach(item -> semesterService.addSemester(item));
		}
	}

	@Override
	public List<SpecialityDTO> selectSpecialities() {
		List<SpecialityDTO> specialities = new ArrayList<>();
		return jdbcTemplate.query(querySpeciality, (ResultSet rs) -> {
			while (rs.next()) {
				specialities.add(new SpecialityDTO().specialityId(rs.getString("CODE_SPECIALITE"))
						.label(rs.getString("LIB_SPECIALITE")));
			}
			return specialities.stream()
					.collect(Collectors.collectingAndThen(
							Collectors.toCollection(
									() -> new TreeSet<>(Comparator.comparing(SpecialityDTO::getSpecialityId))),
							ArrayList::new));
		});
	}

	@Override
	public void insertSpecialities() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(selectSpecialities())) {
			selectSpecialities().forEach(item -> specialityService.addSpeciality(item));
		}
	}

	@Override
	public List<CreateTeacherRequest> selectTeachers() {
		List<CreateTeacherRequest> teachers = new ArrayList<>();
		return jdbcTemplate.query(queryTeacher, (ResultSet rs) -> {
			while (rs.next()) {
				teacherDoesNotExist = true;
				String teacherId = rs.getString("ID_ENS");

				List<CreateDisponibilityRequest> disponibilities = new ArrayList<>();

				CreateDisponibilityRequest disponibility = new CreateDisponibilityRequest();
				if (!StringUtils.isBlank(rs.getString("dayCode"))) {
					disponibility.day(rs.getString("dayCode"));
				}
				if (!StringUtils.isBlank(rs.getString("seanceCode"))) {
					disponibility.seanceId(rs.getString("seanceCode"));
				}
				if (!StringUtils.isBlank(rs.getString("weekId"))) {
					disponibility.weekId(rs.getString("weekId"));
				}
				if (!StringUtils.isBlank(rs.getString("year"))) {
					disponibility.year(rs.getString("year"));
				}
				if (!StringUtils.isBlank(rs.getString("numSemester"))) {
					disponibility.semesterId(rs.getString("numSemester"));
				}
				if (disponibility != null) {
					disponibilities.add(disponibility);
				}

				teachers.forEach(teacher -> {
					if (teacher.getTeacherId().equals(teacherId)) {
						teacher.getDisponibilities().addAll(disponibilities);
						teacherDoesNotExist = false;
					}
				});

				if (teacherDoesNotExist) {
					CreateTeacherRequest teacher = new CreateTeacherRequest().teacherId(rs.getString("ID_ENS"))
							.phone(rs.getString("TEL1")).disponibilities(disponibilities).type(rs.getString("TYPE_ENS"))
							.up(rs.getString("UP")).email(rs.getString("MAIL_ENS")).fullName(rs.getString("NOM_ENS"));
					String codeDept = rs.getString("CODE_DEPT");
					if (!StringUtils.isBlank(codeDept)) {
						teacher.departement(codeDept);
					}
					teachers.add(teacher);
				}

			}
			return teachers;
		});
	}

	@Override
	public void insertTeachers() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(selectTeachers())) {
			selectTeachers().forEach(item -> teacherService.addTeacher(item));
		}
	}

	@Override
	public void insertTeachers(List<CreateTeacherRequest> teachers) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(teachers)) {
			teachers.forEach(item -> teacherService.addTeacher(item));
		}
	}

	@Override
	public List<CreateUserRequest> selectUsers() {
		List<CreateUserRequest> users = new ArrayList<>();
		return jdbcTemplate.query(queryUser, (ResultSet rs) -> {
			while (rs.next()) {
				String roleResult;
				String role = rs.getString("TYPEUSER");
				if (role.equalsIgnoreCase("admin")) {
					roleResult = "ADMIN";
				} else if (role.equalsIgnoreCase("ens")) {
					roleResult = "TEACHER";
				} else if (role.equalsIgnoreCase("user")) {
					roleResult = "AGENT";
				} else {
					roleResult = role;
				}
				users.add(new CreateUserRequest().userId(rs.getString("IDUSER")).firstName(rs.getString("NOMUSER"))
						.lastName(rs.getString("PRENOMUSER")).role(roleResult).username(rs.getString("LOGIN"))
						.email(rs.getString("EMAIL")).password(rs.getString("PASSWORD")).pole(rs.getString("POLE")));
			}
			return users;
		});
	}

	@Override
	public void insertUsers() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(selectUsers())) {
			selectUsers().forEach(item -> iAMService.addUser(item));
		}
	}

	@Override
	public void insertUsers(List<CreateUserRequest> users) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		if (CollectionUtils.isNotEmpty(users)) {
			users.forEach(item -> iAMService.addUser(item));
		}
	}

}