package com.ibm.redbook.library.testdata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ibm.redbook.library.dao.impl.JdbcUtils;
import com.ibm.redbook.library.exceptions.BookException;
import com.ibm.redbook.library.exceptions.MemberException;
import com.ibm.redbook.library.exceptions.StyleException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Used to create the initial data structure in the various databases. This has been tested on Derby and DB2.
 */
public class DatabaseStructureUtil {

	private static Logger log = Logger.getLogger(DatabaseStructureUtil.class.getName());

	public static void initializeBookDatabase(DataSource ds)
			throws BookException {
		log.log(Level.INFO, "init db request");
		Connection con = null;
		String sql = null;
		PreparedStatement ps = null;
		try {
			try {
				con = ds.getConnection();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to get a connection from the DataSource. Got an SQLException: "
						+ e.getMessage();
				throw new BookException(msg, e);
			}
			try {
				sql = "DROP TABLE borrowed_list";
				ps = con.prepareStatement(sql);
				ps.execute();
			} catch (SQLException ignoreMe) {
				log.log(Level.INFO, "got an sql exception when trying to drop the table.  This will be ignored. But it was:\n"
						+ ignoreMe.getMessage());
			}
			try {
				sql = "DROP TABLE book";
				ps = con.prepareStatement(sql);
				ps.execute();
			} catch (SQLException ignoreMe) {
				log.log(Level.INFO, "got an sql exception when trying to drop the table.  This will be ignored. But it was:\n"
						+ ignoreMe.getMessage());
			}
			
			try {
				sql = "CREATE TABLE book (" + "id varchar(32) not null,"
						+ "name varchar(128)," + "description varchar(512)," + "quantity int,"
						+ "primary key (id))";
				ps = con.prepareStatement(sql);
				ps.execute();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to create tabel 'book'. Got an SQLException: "
						+ e.getMessage();
				throw new BookException(msg, e);
			}
			
			try {
				sql = "CREATE TABLE borrowed_list ("
						+ "member_id varchar(32) not null,"
						+ "book_id varchar(32) not null," + "primary key (member_id, book_id), "
						+ "FOREIGN KEY (book_id)"
						+ "REFERENCES book (id))";
				ps = con.prepareStatement(sql);
				ps.execute();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to creat table 'borrowed_list'. Got an SQLException: "
						+ e.getMessage();
				throw new BookException(msg, e);
			}
		} finally {
			JdbcUtils.closeConnectionAndPS(con, ps);
		}
	}

	public static void initializeMemberDatabase(DataSource memberDataSource)
			throws MemberException {
		log.log(Level.INFO, "init Member db request");
		Connection con = null;
		String sql = null;
		PreparedStatement ps = null;
		try {
			try {
				con = memberDataSource.getConnection();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to get a connection from the members DataSource. Got an SQLException: "
						+ e.getMessage();
				throw new MemberException(msg, e);
			}


			try {
				sql = "DROP TABLE member";
				ps = con.prepareStatement(sql);
				ps.execute();
			} catch (SQLException ignoreMe) {
				log.log(Level.INFO, "got an sql exception when trying to drop the table 'member'.  This will be ignored. But it was:\n"
						+ ignoreMe.getMessage());
			}

			try {
				sql = "CREATE TABLE member (" + "id varchar(32) not null,"
						+ "password varchar(255) not null,"
						+ "primary key (id))";
				ps = con.prepareStatement(sql);
				ps.execute();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to create te 'member' table. Got an SQLException: "
						+ e.getMessage();
				throw new MemberException(msg, e);
			}


		} finally {
			JdbcUtils.closeConnectionAndPS(con, ps);
		}

	}

	public static void initializeStyleDatabase(DataSource styleDataSource)
			throws StyleException {
		log.log(Level.INFO, "init Member db request");
		Connection con = null;
		String sql = null;
		PreparedStatement ps = null;
		try {
			try {
				con = styleDataSource.getConnection();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to get a connection from the members DataSource. Got an SQLException: "
						+ e.getMessage();
				throw new StyleException(msg, e);
			}

			try {
				sql = "DROP TABLE style";
				ps = con.prepareStatement(sql);
				ps.execute();
			} catch (SQLException ignoreMe) {
				log.log(Level.INFO, "got an sql exception when trying to drop the 'style' table.  This will be ignored. But it was:\n"
						+ ignoreMe.getMessage());
			}

			try {
				sql = "CREATE TABLE style (" + "member_id varchar(32) not null,"
						+ "bgcolor varchar(255)," + "fgcolor varchar(255),"
						+ "primary key (member_id))";
				ps = con.prepareStatement(sql);
				ps.execute();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to create the 'style' table. Got an SQLException: "
						+ e.getMessage();
				throw new StyleException(msg, e);
			}
		} finally {
			JdbcUtils.closeConnectionAndPS(con, ps);
		}

	}
}
