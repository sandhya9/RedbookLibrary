package com.ibm.redbook.library.exceptions;

/**
 * Base exception for any application specific exceptions
 */
public class ProcessingException extends Exception {
	private static final long serialVersionUID = 4392484963379782474L;

	public ProcessingException() {
		super();
	}

	public ProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessingException(String message) {
		super(message);
	}

	public ProcessingException(Throwable cause) {
		super(cause);
	}


}
