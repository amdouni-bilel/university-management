package com.esprit.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esprit.dto.room.CreateRoomRequest;
import com.esprit.dto.room.FilterAvailableRoomDTO;
import com.esprit.dto.room.RoomDTO;
import com.esprit.dto.room.RoomWithoutDisponibilityDTO;
import com.esprit.service.RoomService;

@RestController
@RequestMapping("room")
public class RoomController {

	private final RoomService service;

	public RoomController(RoomService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Void> createClassRoom(@RequestBody @Valid CreateRoomRequest createRoomRequest) {
		service.addClassRoom(createRoomRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Void> updateClassRoom(@RequestBody @Valid CreateRoomRequest createRoomRequest) {
		service.updateClassRoom(createRoomRequest);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("{classRoomId}")
	public ResponseEntity<Void> deleteClassRoom(@PathVariable String classRoomId) {
		service.deleteClassRoom(classRoomId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("without-disponibilities")
	public ResponseEntity<List<RoomWithoutDisponibilityDTO>> findRoomsWithoutDisponibilities() {
		return new ResponseEntity<>(service.findRoomsWithoutDisponibilities(), HttpStatus.OK);
	}

	@GetMapping("disponibilities")
	public ResponseEntity<Map<String, Object>> findRoomsWithDisponibility(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int size, @RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortType, @RequestParam(required = false) String filter) {
		Map<String, Object> result;
		if (!StringUtils.isBlank(sortField) && !StringUtils.isBlank(sortField)) {
			Sort sort = null;
			if (sortType.equals("ASC")) {
				sort = Sort.by(sortField).ascending();
			} else if (sortType.equals("DESC")) {
				sort = Sort.by(sortField).descending();
			}
			result = service.findRoomsWithDisponibility(PageRequest.of(page, size, sort));
		} else if (!StringUtils.isBlank(filter)) {
			result = service.findRoomsWithoutDisponibilitiesWithFilter(filter, PageRequest.of(page, size));
		} else if (!StringUtils.isBlank(filter) && !StringUtils.isBlank(sortField) && !StringUtils.isBlank(sortField)) {
			result = service.findRoomsWithDisponibility(PageRequest.of(page, size));
		} else {
			result = service.findRoomsWithDisponibility(PageRequest.of(page, size));
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("bloc/{blocId}")
	public ResponseEntity<List<RoomDTO>> findByBloc(@PathVariable String blocId) {
		return new ResponseEntity<>(service.findByBloc(blocId), HttpStatus.OK);
	}

	@GetMapping("{classRoomId}")
	public ResponseEntity<RoomDTO> findClassRoom(@PathVariable String classRoomId) {
		return new ResponseEntity<>(service.findClassRoom(classRoomId), HttpStatus.OK);
	}

	@PostMapping("bloc")
	public ResponseEntity<List<String>> findBlocByRooms(@RequestBody List<String> rooms) {
		return new ResponseEntity<>(service.findBlocByRooms(rooms), HttpStatus.OK);
	}

	@PostMapping("blocs")
	public ResponseEntity<List<RoomDTO>> findAllByBlocs(@RequestBody FilterAvailableRoomDTO filterAvailableRoomDTO) {
		return new ResponseEntity<>(service.findAllByBlocs(filterAvailableRoomDTO), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<RoomDTO>> findRooms() {
		return new ResponseEntity<>(service.findRooms(), HttpStatus.OK);
	}

}