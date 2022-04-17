package com.esprit.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.esprit.domain.DisponibilityEntity;

public interface DisponibilityRepository extends JpaRepository<DisponibilityEntity, String> {

	@Query(nativeQuery = true, value = "select * from DISPONIBILITY ds inner join SALLE s on s.CLASSROOMID = ds.FK_ROOM and s.CLASSROOMID = ?1")
	List<DisponibilityEntity> findAllByRoom(String roomId);

	@Query(nativeQuery = true, value = "select * from DISPONIBILITY ds inner join ESP_ENSEIGNANT s on s.TEACHERID = ds.fk_teacher and s.teacherId = ?1")
	List<DisponibilityEntity> findAllByTeacher(String teacherId);

	@Query(nativeQuery = true, value = "select count(*) from disponibility d WHERE d.fk_room = ?1 "
			+ "AND ((d.starthour < ?2 AND ?2 < d.endhour) OR (d.starthour < ?3 AND ?3 < d.endhour)) "
			+ "AND ((d.startdate <= TO_DATE(?4, 'dd/MM/yyyy') AND TO_DATE(?4, 'dd/MM/yyyy') <= d.enddate) OR (d.startdate <= TO_DATE(?5, 'dd/MM/yyyy') AND TO_DATE(?5, 'dd/MM/yyyy') <= d.enddate))")
	int checkUnicityRoom(String roomId, int startHour, int endHour, String startDate, String endDate);

	@Query(nativeQuery = true, value = "select count(*) from disponibility d WHERE d.fk_teacher = ?1 "
			+ "AND ((d.starthour < ?2 AND ?2 < d.endhour) OR (d.starthour < ?3 AND ?3 < d.endhour)) "
			+ "AND ((d.startdate <= TO_DATE(?4, 'dd/MM/yyyy') AND TO_DATE(?4, 'dd/MM/yyyy') <= d.enddate) OR (d.startdate <= TO_DATE(?5, 'dd/MM/yyyy') AND TO_DATE(?5, 'dd/MM/yyyy') <= d.enddate))")
	int checkUnicityTeacher(String teacherId, int startHour, int endHour, String startDate, String endDate);

	public static String query = "SELECT disp.fk_room FROM DISPONIBILITY disp where (TO_DATE(?1, 'dd/MM/yyyy') >= disp.STARTDATE or TO_DATE(?1, 'dd/MM/yyyy') <= disp.ENDDATE) and (?2 >= disp.startHour or ?2 < disp.endHour)";
	@Query(nativeQuery = true, value = query)
	List<String> findRommsInd(String effectDate, int hour);

	public static String query2 = "SELECT disp.fk_teacher FROM DISPONIBILITY disp where (TO_DATE(?1, 'dd/MM/yyyy') >= disp.STARTDATE or TO_DATE(?1, 'dd/MM/yyyy') <= disp.ENDDATE) and (?2 >= disp.startHour or ?2 < disp.endHour)";
	@Query(nativeQuery = true, value = query2)
	List<String> findTeachrsInd(String effectDate, int hour);
	
	public static String query3 = "SELECT disp.fk_room FROM DISPONIBILITY disp where ?1 BETWEEN disp.STARTDATE and disp.ENDDATE and ?2 >= disp.startHour and ?2 < disp.endHour";
	@Query(nativeQuery = true, value = query3)
	List<String> findRommsInd(Date effectDate, double hour);

	public static String query4 = "SELECT disp.fk_room FROM DISPONIBILITY disp where ?1 BETWEEN disp.STARTDATE and disp.ENDDATE and ?2 >= disp.startHour and ?2 < disp.endHour";
	@Query(nativeQuery = true, value = query4)
	List<String> findTeachrsInd(Date effectDate, double hour);
}