package com.ibm.redbook.library.exceptions;

/**
 * Represents an exception when working with a DataSource
 */
public class DataSourceException extends ProcessingException {
	private static final long serialVersionUID = 4392484963379782474L;

	public DataSourceException() {
		super();
	}

	public DataSourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataSourceException(String message) {
		super(message);
	}

	public DataSourceException(Throwable cause) {
		super(cause);
	}


}
