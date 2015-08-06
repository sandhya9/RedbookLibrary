package com.ibm.redbook.library.exceptions;

/**
 * Represents an exception while working with Style information
 */
public class StyleException extends ProcessingException {

	private static final long serialVersionUID = -2055253828229087802L;

	public StyleException() {
		super();
	}

	public StyleException(String message, Throwable cause) {
		super(message, cause);
	}

	public StyleException(String message) {
		super(message);
	}

	public StyleException(Throwable cause) {
		super(cause);
	}
}
