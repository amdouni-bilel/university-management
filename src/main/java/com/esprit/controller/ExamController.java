package com.esprit.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esprit.domain.ExamEntity;
import com.esprit.dto.exam.CreateExamRequest;
import com.esprit.dto.exam.DeleteExamDTO;
import com.esprit.dto.exam.ExamInfoDTO;
import com.esprit.dto.exam.MailExamDTO;
import com.esprit.dto.exam.UpdateExamRequest;
import com.esprit.service.ExamService;
import com.esprit.service.MailService;

@RestController
@RequestMapping("exam")
public class ExamController {

	private final ExamService service;

	private final MailService mailService;

	public ExamController(ExamService service, MailService mailService) {
		this.service = service;
		this.mailService = mailService;
	}

	@PostMapping
	public ResponseEntity<Void> createExam(@RequestBody @Valid CreateExamRequest createExamRequest) {
		service.addExam(createExamRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("mail")
	public ResponseEntity<Void> sendMail(@RequestBody @Valid List<MailExamDTO> mailExamDTO) throws Exception {
		mailService.sendEmail(mailExamDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<Void> upateExam(@RequestBody @Valid UpdateExamRequest updateExamRequest) {
		service.updateExam(updateExamRequest);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PatchMapping
	public ResponseEntity<Void> deleteExam(@RequestBody DeleteExamDTO deleteExamDTO) {
		service.deleteExam(deleteExamDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("{examId}")
	public ResponseEntity<Void> deleteExam(@PathVariable String examId) {
		service.deleteExamById(examId);
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@GetMapping("session/{session}")
	public ResponseEntity<List<String>> findExamsBySession(@PathVariable String session) {
		return new ResponseEntity<>(service.findExamsBySession(session), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<ExamEntity>> findExams() {
		return new ResponseEntity<>(service.findExams(), HttpStatus.OK);
	}

	@GetMapping("class/{classId}")
	public ResponseEntity<List<ExamInfoDTO>> getExamInfoByClass(@PathVariable String classId) {
		return new ResponseEntity<>(service.getExamInfoByClass(classId), HttpStatus.OK);
	}

}