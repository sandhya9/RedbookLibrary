package com.ibm.redbook.library.util;

/**
 * List of actions that a web page can perform. These are passed back to the calling servlet.
 */
public enum WebActions {
	VERIFY,
	RETURN,
	BORROW,
	SEARCH,
	DISPLAY,
	SET_COLORS
}
