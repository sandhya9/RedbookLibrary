package com.ibm.redbook.library.exceptions;

/**
 * Represents an exception working with a member's data
 */
public class MemberException extends ProcessingException {
	private static final long serialVersionUID = 4392484963379782474L;

	public MemberException() {
		super();
	}

	public MemberException(String message, Throwable cause) {
		super(message, cause);
	}

	public MemberException(String message) {
		super(message);
	}

	public MemberException(Throwable cause) {
		super(cause);
	}


}
