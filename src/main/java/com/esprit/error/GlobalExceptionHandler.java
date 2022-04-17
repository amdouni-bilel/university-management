package com.esprit.error;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.esprit.error.exception.ClassExistInSessionException;
import com.esprit.error.exception.DeleteDisponibilityException;
import com.esprit.error.exception.EntityAlreadyExistsExeption;
import com.esprit.error.exception.EntityNotFoundException;
import com.esprit.error.exception.ModuleExistInSessionException;
import com.esprit.error.exception.RoomSizeExecption;
import com.esprit.error.exception.TeacherSizeExecption;
import com.esprit.error.exception.TeachersExistInSessionException;
import com.esprit.error.exception.UnicityException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice()
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Malformed JSON request";
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("code", status);
		body.put("status", status.value());
		body.put("error", error);
		return new ResponseEntity<>(body, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> errors = new ArrayList<>(0);
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		ApiError apiError = new ApiError(CustomHttpStatus.BAD_REQUEST, new Date(), errors);
		return new ResponseEntity<>(apiError, headers, status);
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> entityNotFoundException(EntityNotFoundException ex) {
		return buildResponseEntity(new ApiError(CustomHttpStatus.ENTITY_NOT_FOUND, new Date(), ex.getMessage()),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EntityAlreadyExistsExeption.class)
	public ResponseEntity<Object> entityAlreadyExistsExeption(EntityAlreadyExistsExeption ex) {
		return buildResponseEntity(new ApiError(CustomHttpStatus.ENTITY_NOT_FOUND, new Date(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RoomSizeExecption.class)
	public ResponseEntity<Object> roomSizeExecption(RoomSizeExecption ex) {
		return buildResponseEntity(new ApiError(CustomHttpStatus.ROOM_SIZE_REQUEST, new Date(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TeacherSizeExecption.class)
	public ResponseEntity<Object> teacherSizeExecption(TeacherSizeExecption ex) {
		return buildResponseEntity(new ApiError(CustomHttpStatus.TEACHER_SIZE_REQUEST, new Date(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ModuleExistInSessionException.class)
	public ResponseEntity<Object> moduleExistInSessionException(ModuleExistInSessionException ex) {
		return buildResponseEntity(new ApiError(CustomHttpStatus.MODULE_EXIST_REQUEST, new Date(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UnicityException.class)
	public ResponseEntity<Object> unicityException(UnicityException ex) {
		return buildResponseEntity(new ApiError(CustomHttpStatus.UNICITY_REQUEST, new Date(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ClassExistInSessionException.class)
	public ResponseEntity<Object> classExistInSessionException(ClassExistInSessionException ex) {
		return buildResponseEntity(new ApiError(CustomHttpStatus.CLASS_EXIST_REQUEST, new Date(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TeachersExistInSessionException.class)
	public ResponseEntity<Object> teachersExistInSessionException(TeachersExistInSessionException ex) {
		return buildResponseEntity(new ApiError(CustomHttpStatus.TEACHERS_EXIST_REQUEST, new Date(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DeleteDisponibilityException.class)
	public ResponseEntity<Object> deleteDisponibilityException(DeleteDisponibilityException ex) {
		return buildResponseEntity(new ApiError(CustomHttpStatus.DELETE_DISP_REQUEST, new Date(), ex.getMessage()),
				HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus status) {
		return new ResponseEntity<>(apiError, status);
	}

}