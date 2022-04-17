package com.esprit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.esprit.domain.RoomEntity;
import com.esprit.dto.room.DisponibilityRoomDTO;
import com.esprit.dto.room.RoomWithoutDisponibilityDTO;

public interface RoomRepository extends JpaRepository<RoomEntity, String> {

	List<RoomEntity> findAllByOrderByClassRoomIdAsc();

	public static String query = "SELECT * FROM salle sl where sl.classRoomId NOT IN\n" +
			"(SELECT disp.fk_room FROM DISPONIBILITY disp where ?1 BETWEEN disp.STARTDATE and disp.ENDDATE and ?2 >= disp.startHour and ?2 < disp.endHour )\n"
			+ "AND sl.bloc IN ?3";

	@Query("SELECT new com.esprit.dto.room.RoomWithoutDisponibilityDTO(r.classRoomId, r.label, r.capacity, r.pole, r.bloc) FROM RoomEntity as r")
	List<RoomWithoutDisponibilityDTO> findRoomsWithoutDisponibilities();

	@Query("FROM RoomEntity as r WHERE r.bloc = ?1 and r.disponibilities IS NULL ORDER BY classRoomId ASC")
	List<RoomEntity> findByBloc(String bloc);

	@Query(nativeQuery = true, value = "SELECT * FROM salle sl where sl.bloc IN ?1 ORDER BY classRoomId ASC")
	List<RoomEntity> findAllByBlocs(List<String> blocs);

	List<RoomEntity> findByClassRoomIdIn(List<String> roomIds);


	@Query(nativeQuery = true, value = "SELECT * FROM salle es JOIN ROOMENTITY_EXAMIDS tex ON tex.ROOMENTITY_CLASSROOMID = es.CLASSROOMID where es.classRoomId IN ?1 AND ?2 = tex.EXAMIDS")
	List<RoomEntity> findByClassRoomIdsAndExamId(List<String> roomIds, String examId);

	@Query(nativeQuery = true, value = "select DISTINCT bloc from salle where classRoomId IN ?1")
	List<String> findBlocByRooms(List<String> rooms);

	@Query("select "
			+ "new com.esprit.dto.room.DisponibilityRoomDTO(es.classRoomId, d.disponibilityId, d.startDate, d.endDate, d.startHour, d.endHour, d.motif, d.commentd) "
			+ "from RoomEntity es LEFT JOIN es.disponibilities d WHERE d.disponibilityId IS NOT NULL")
	Page<DisponibilityRoomDTO> findRoomsWithDisponibility(Pageable pageable);

	@Query("select "
			+ "new com.esprit.dto.room.DisponibilityRoomDTO(es.classRoomId, d.disponibilityId, d.startDate, d.endDate, d.startHour, d.endHour, d.motif, d.commentd) "
			+ "from RoomEntity es LEFT JOIN es.disponibilities d "
			+ "WHERE d.disponibilityId IS NOT NULL AND "
			+ "  ( UPPER(es.classRoomId) like '%'||UPPER(?1)||'%' OR UPPER(d.motif) like '%'||UPPER(?1)||'%' OR UPPER(d.commentd) like '%'||UPPER(?1)||'%' OR UPPER(d.startHour) like '%'||UPPER(?1)||'%' OR UPPER(d.endHour) like '%'||UPPER(?1)||'%' OR UPPER(d.startDate) like '%'||UPPER(?1)||'%' OR UPPER(d.endDate) like '%'||UPPER(?1)||'%' ) ")
	Page<DisponibilityRoomDTO> findRoomsWithDisponibilityWithFilter(String filter, Pageable pageable);

}