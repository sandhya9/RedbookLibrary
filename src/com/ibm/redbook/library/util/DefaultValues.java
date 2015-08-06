package com.ibm.redbook.library.util;

/**
 * A collection of default values for anything that requires a default
 */
public enum DefaultValues {
	BACKGROUND_COLOR("white"), FOREGROUND_COLOR("black");
	private String defaultValue = null;

	@Override
	/** 
	 * Returns the actual default value, rather than the name o the enum. for example, BACKGROUND_COLOR.toString() returns the string "black" without quotes.
	 */
	public String toString() {
		return defaultValue;
	}

	private DefaultValues(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
