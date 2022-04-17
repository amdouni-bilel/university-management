package com.esprit.error.exception;

public class TeachersExistInSessionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TeachersExistInSessionException(String msg) {
		super(msg);
	}

}