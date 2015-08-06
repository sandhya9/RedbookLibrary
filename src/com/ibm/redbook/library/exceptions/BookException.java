package com.ibm.redbook.library.exceptions;

/**
 * Represents an exception when performing operations with Book resources.
 */
public class BookException extends ProcessingException {
	private static final long serialVersionUID = 4392484963379782474L;

	public BookException() {
		super();
	}

	public BookException(String message, Throwable cause) {
		super(message, cause);
	}

	public BookException(String message) {
		super(message);
	}

	public BookException(Throwable cause) {
		super(cause);
	}


}
