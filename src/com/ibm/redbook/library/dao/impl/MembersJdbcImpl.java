package com.ibm.redbook.library.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.redbook.library.Member;
import com.ibm.redbook.library.dao.DataSourceManager;
import com.ibm.redbook.library.dao.IMember;
import com.ibm.redbook.library.exceptions.DataSourceException;
import com.ibm.redbook.library.exceptions.MemberException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * A concrete implementation of the IMember interface. Uses JDBC to communicate to the required databases.
 */
public class MembersJdbcImpl implements IMember {
	Logger log = Logger.getLogger(getClass().getName());

	@Override
	public Member addMember(Member member) throws MemberException, DataSourceException {
		log.log(Level.INFO, "add member: " + member.getMemberId());
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataSourceManager.MEMBER.getDs().getConnection();
			String sql = "INSERT INTO  member (id, password) VALUES(?,?)";

			ps = con.prepareStatement(sql);
			ps.setString(1, member.getMemberId());
			ps.setString(2, member.getPassword());
			int i = ps.executeUpdate();
			if (i != 1) {
				con.rollback();
				throw new MemberException(
						"Add member FAILED. Expected 1 item to be expected but got: "
								+ i);
			}
		} catch (SQLException e) {
			String msg = "Got an SQLException when creating a member for id:  "
					+ member.getMemberId() + ".  Error: " + e;
			throw new MemberException(msg, e);
		} finally {
			JdbcUtils.closeConnectionAndPS(con, ps);
		}
		return member;
	}

	@Override
	public Member updateMember(Member member) throws MemberException, DataSourceException {
		log.log(Level.INFO, "update member: " + member.getMemberId());
		Connection memberCon = null;
		Connection bookCon = null;
		PreparedStatement ps = null;
		Connection styleCon = null;
		PreparedStatement stylePs = null;
		ResultSet rs = null;
		try {
			memberCon = DataSourceManager.MEMBER.getDs().getConnection();
			String sql = "UPDATE member SET password=? WHERE id=?";
			ps = memberCon.prepareStatement(sql);
			ps.setString(1, member.getPassword());
			ps.setString(2, member.getMemberId());
			int i = ps.executeUpdate();
			if (i != 1) {
				memberCon.rollback();
				throw new MemberException(
						"Update member FAILED. Expected 1 item to get updated but got: "
								+ i);
			}
			ps.close();
		} catch (SQLException e) {
			String msg = "Got an SQLException when updaing a member.  id: "
					+ member.getMemberId() + ".  Error: " + e;
			throw new MemberException(msg, e);
		}

		try {
			String sql = "DELETE from borrowed_list WHERE member_id=?";
			bookCon = DataSourceManager.BOOK.getDs().getConnection();
			ps = bookCon.prepareStatement(sql);
			ps.setString(1, member.getMemberId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			String msg = "Got an SQLException when deleting a members borrowed book list before updating.  id: "
					+ member.getMemberId() + ".  Error: " + e;
			throw new MemberException(msg, e);
		}

		String sql = "INSERT INTO borrowed_list (member_id, book_id) VALUES (?,?)";
		for (String bookId : member.getBorrowedBookIds()) {
			try {
				ps = bookCon.prepareStatement(sql);
				ps.setString(1, member.getMemberId());
				ps.setString(2, bookId);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				String msg = "Got an SQLException when inserting a member's borrowed book list.  id: "
						+ member.getMemberId() + ".  Error: " + e;
				throw new MemberException(msg, e);
			}
		}
		try {
			styleCon = DataSourceManager.STYLE.getDs().getConnection();
			// First we need to see if the user exists
			sql = "SELECT * FROM style WHERE member_id=?";
			stylePs = styleCon.prepareStatement(sql);
			stylePs.setString(1, member.getMemberId());
			rs = stylePs.executeQuery();
			if (rs.next()) {
				sql = "UPDATE style set bgcolor=?, fgcolor=? WHERE member_id=?";
			} else {
				sql = "INSERT INTO style (bgcolor, fgcolor, member_id) VALUES (?,?,?)";
			}

			// Note that the ordering in both queries is the same, to allow a single update
			stylePs = styleCon.prepareStatement(sql);
			stylePs.setString(1, member.getBackgroundColor());
			stylePs.setString(2, member.getForegroundColor());
			stylePs.setString(3, member.getMemberId());
			int i = stylePs.executeUpdate();
			if (i != 1) {
				styleCon.rollback();
				log.log(Level.SEVERE, "Update style FAILED for member "
						+ member.getMemberId()
						+ ". Expected 1 item to get updated but got: " + i);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, "Got an SQLException when updating a members style.  id: "
					+ member.getMemberId() + ", error: " + e.getMessage(), e);
		} finally {
			JdbcUtils.closeConnectionAndPS(styleCon, stylePs);
		}
		JdbcUtils.closeConnectionAndPS(memberCon, ps);
		return member;
	}

	@Override
	public Member getMember(String id) throws MemberException,
			DataSourceException {
		log.log(Level.INFO, "get member: " + id);
		Connection memberCon = null;
		Connection styleCon = null;
		PreparedStatement memberPs = null;
		ResultSet rs = null;
		Member m = null;
		String sql = "";
		PreparedStatement stylePs = null;
		try {
			memberCon = DataSourceManager.MEMBER.getDs().getConnection();
			sql = "SELECT * from member WHERE id=?";
			memberPs = memberCon.prepareStatement(sql);
			memberPs.setString(1, id);
			rs = memberPs.executeQuery();
			if (rs.next()) {
				m = new Member();
				m.setMemberId(id);
				m.setPassword(rs.getString("password"));
				try {
					sql = "SELECT * from borrowed_list WHERE member_id=?";
					Connection bookConn = DataSourceManager.BOOK.getDs().getConnection();
					memberPs = bookConn.prepareStatement(sql);
					memberPs.setString(1, id);
					rs = memberPs.executeQuery();
					while (rs.next()) {
						m.borrowBook(rs.getString("book_id"));
					}
				} catch (SQLException e) {
					String msg = "Got an SQLException when reading a member's borrowed list.  Member ID: " + id + ".  Error: " + e;
					throw new MemberException(msg, e);
				}
				// Get the member's style data. If this doen't succeed the application should be able to continue
				try {
					styleCon = DataSourceManager.STYLE.getDs().getConnection();
					sql = "SELECT * from style WHERE member_id=?";
					stylePs = styleCon.prepareStatement(sql);
					stylePs.setString(1, m.getMemberId());
					rs = stylePs.executeQuery();
					if (rs.next()) {
						m.setBackgroundColor(rs.getString("bgcolor"));
						m.setForegroundColor(rs.getString("fgcolor"));
					}
				} catch (SQLException e) {
					log.log(Level.SEVERE, "Got an SQLException when reading a members style.  id: "
							+ m.getMemberId() + ", error:" + e.getMessage(), e);
				}		
			}
		} catch (SQLException e) {
			String msg = "Got an SQLException when reading a member.  id: "
					+ id + ".  Error: " + e;
			throw new MemberException(msg, e);
		}
		finally {
			JdbcUtils.closeConnectionAndPS(memberCon, memberPs);
			JdbcUtils.closeConnectionAndPS(styleCon, stylePs);
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ignoreMe) {
				}
			}
		}
		return m;
	}

	@Override
	public boolean deleteMember(String id) throws MemberException, DataSourceException {
		log.log(Level.INFO, "delete member: " + id);
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataSourceManager.MEMBER.getDs().getConnection();
			String sql = "DELETE from member WHERE member_id=?";
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			int i = ps.executeUpdate();
			if (i == 1) {
				return true;
			} else {
				con.rollback();
				return false;
			}

		} catch (SQLException e) {
			String msg = "Got an SQLException when creating deleting a member.  id: "
					+ id + ".  Error: " + e;
			throw new MemberException(msg, e);
		} finally {
			JdbcUtils.closeConnectionAndPS(con, ps);
		}
	}
}
