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
 * Used to populate test data in the databases.
 */
public class DatabaseTestDataUtil {

	private static Logger log = Logger.getLogger(DatabaseTestDataUtil.class.getName());

	public static void populateMemberDatabase(DataSource memberDataSource)
			throws MemberException {
		log.log(Level.INFO, "TEST data member creation request");
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
				sql = "INSERT INTO member (" + "id, password) VALUES (?,?)";
				ps = con.prepareStatement(sql);

				ps.setString(1, "user1");
				ps.setString(2, "password1");
				ps.execute();

				ps.setString(1, "user2");
				ps.setString(2, "password2");
				ps.execute();

			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to insert member into database. Got an SQLException: "
						+ e.getMessage();
				throw new MemberException(msg, e);
			}

		} finally {
			JdbcUtils.closeConnectionAndPS(con, ps);
		}
	}

	public static void populateStyleDatabase(DataSource styleDataSource)
			throws StyleException {
		log.log(Level.INFO, "TEST data style creation request");
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
				sql = "INSERT INTO style ("
						+ "member_id, bgcolor, fgcolor) VALUES (?,?,?)";
				ps = con.prepareStatement(sql);
				ps.setString(1, "user1");
				ps.setString(2, "black");
				ps.setString(3, "white");
				ps.execute();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to insert style data. Got an SQLException: "
						+ e.getMessage();
				throw new StyleException(msg, e);
			}
		} finally {
			JdbcUtils.closeConnectionAndPS(con, ps);
		}

	}

	public static void populateBookDatabase(DataSource bookDataSource) throws BookException {
		log.log(Level.INFO, "TEST data style creation request");
		Connection con = null;
		String sql = null;
		PreparedStatement ps = null;
		try {
			try {
				con = bookDataSource.getConnection();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to get a connection from the members DataSource. Got an SQLException: "
						+ e.getMessage();
				throw new BookException(msg, e);
			}
			try {
				sql = "INSERT INTO book ("
						+ "id, name, description, quantity) VALUES (?,?,?,?)";
				ps = con.prepareStatement(sql);

				ps.setString(1, "1");
				ps.setString(2, "WebSphere Application Server V7 Flexible Management: Security Configuration Requirements");
				ps.setString(3, "IBM® WebSphere\u00ae Application Server V7 introduces an administration function called that is designed to facilitate administration of many WebSphere Application Server stand-alone profiles and Federated Network Deployment topologies.");
				ps.setInt(4, 10);
				ps.execute();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to insert data into the book table. Got an SQLException: "
						+ e.getMessage();
				throw new BookException(msg, e);
			}
			try {
				sql = "INSERT INTO book ("
						+ "id, name, description, quantity) VALUES (?,?,?,?)";
				ps = con.prepareStatement(sql);

				ps.setString(1, "2");
				ps.setString(2, "WebSphere Application Server V8: Administration and Configuration Guide");
				ps.setString(3, "This IBM\u00ae Redbooks\u00ae publication provides system administrators and developers with the knowledge to configure an IBM WebSphere\u00ae Application Server Version 8 runtime environment, to package and deploy applications, and to perform ongoing management of the WebSphere environment.");
				ps.setInt(4, 0);
				ps.execute();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to insert data into the book table. Got an SQLException: "
						+ e.getMessage();
				throw new BookException(msg, e);
			}
			try {
				sql = "INSERT INTO book ("
						+ "id, name, description, quantity) VALUES (?,?,?,?)";
				ps = con.prepareStatement(sql);
				
				ps.setString(1, "3");
				ps.setString(2, "WebSphere Application Server: New Features in V8.5");
				ps.setString(3, "WebSphere Application Server can help businesses offer richer user experiences through the rapid delivery of innovative applications. Developers can jumpstart development efforts and leverage existing skills by selecting from the comprehensive set of open standards-based programming models supported.");
				ps.setInt(4, 1);
				ps.execute();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to insert data into the book table. Got an SQLException: "
						+ e.getMessage();
				throw new BookException(msg, e);
			}
			
			try {
				sql = "INSERT INTO borrowed_list ("
						+ "member_id, book_id) VALUES (?,?)";
				ps = con.prepareStatement(sql);
				ps.setString(1, "user1");
				ps.setString(2, "1");

				ps.execute();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to insert data into the borrowed_list table. Got an SQLException: "
						+ e.getMessage();
				throw new BookException(msg, e);
			}
			try {
				sql = "INSERT INTO borrowed_list ("
						+ "member_id, book_id) VALUES (?,?)";
				ps = con.prepareStatement(sql);
				ps.setString(1, "user1");
				ps.setString(2, "3");

				ps.execute();
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
				String msg = "Failed to insert data into the borrowed_list table. Got an SQLException: "
						+ e.getMessage();
				throw new BookException(msg, e);
			}
			
			
			
		} finally {
			JdbcUtils.closeConnectionAndPS(con, ps);
		}

	}

}
