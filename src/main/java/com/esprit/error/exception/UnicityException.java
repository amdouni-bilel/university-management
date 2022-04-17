package com.esprit.error.exception;

public class UnicityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnicityException() {
		super("exist");
	}

}