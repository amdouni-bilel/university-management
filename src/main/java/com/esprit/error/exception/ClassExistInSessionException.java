package com.esprit.error.exception;

public class ClassExistInSessionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClassExistInSessionException(String msg) {
		super(msg);
	}

}