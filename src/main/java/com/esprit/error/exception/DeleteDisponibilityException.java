package com.esprit.error.exception;

public class DeleteDisponibilityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DeleteDisponibilityException() {
		super("On ne peut pas supprimer cette indisponiblé");
	}

}