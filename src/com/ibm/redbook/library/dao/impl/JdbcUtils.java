package com.ibm.redbook.library.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A collection of methods that can be used statically across JDBC calls to perform common tasks
 */
public class JdbcUtils {
	
	/**
	 * Closes the connection to the database and the prepared statement. This should be used at the end of all database work. Note this will not throw on any SQLExceptions that occur
	 * @param con The connection to the database to close, if not null
	 * @param ps The prepared statement to close, if not null
	 */
	public static void closeConnectionAndPS(Connection con, PreparedStatement ps){
		if (con != null){
			try {
				con.close();
			} catch (SQLException ignoreMe) {
			}
		}
		if (ps != null){
			try {
				ps.close();
			} catch (SQLException ignoreMe) {
			}
		}
	}

}
