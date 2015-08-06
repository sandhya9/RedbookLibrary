package com.ibm.redbook.library.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.ibm.redbook.library.Book;
import com.ibm.redbook.library.dao.DataSourceManager;
import com.ibm.redbook.library.dao.IBook;
import com.ibm.redbook.library.exceptions.BookException;
import com.ibm.redbook.library.exceptions.DataSourceException;

/**
 * A concrete implementation of the IBook interface. Uses JDBC to acces database resources.
 */
public class BookJdbcImpl implements IBook {
	private static Logger log = Logger.getLogger(BookJdbcImpl.class.getName());
	
	@Override
	public Book addBook(Book book) throws BookException, DataSourceException {
		log.info("add book: " + book.getId());
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataSourceManager.BOOK.getDs().getConnection();			
			String sql = "INSERT INTO  book (id, name, description, quantity) VALUES(?,?,?,?)";
			
			ps = con.prepareStatement(sql);
			ps.setString(1, book.getId());
			ps.setString(2, book.getName());
			ps.setString(3, book.getDescription());
			ps.setInt(4, book.getQuantity());
			int i = ps.executeUpdate();
			if (i!=1){
				con.rollback();
				throw new BookException("Add book FAILED. Expected 1 item to be expected but got: " + i);
			}
		} catch (SQLException e) {			
			String msg = "Got an SQLException when creating inserting a book for id:  " + book.getId() 
					+ ".  Error: " + e;			
			throw new BookException(msg,e);
		}finally{
			JdbcUtils.closeConnectionAndPS(con, ps);
		}
		return book;
	}

	@Override
	public Book updateBook(Book book) throws BookException, DataSourceException {
		log.info("update book: " + book.getId());
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DataSourceManager.BOOK.getDs().getConnection();			
			String sql = "UPDATE book SET name=?, description=?, quantity=? WHERE id=?";			
			ps = con.prepareStatement(sql);			
			ps.setString(1, book.getName());
			ps.setString(2, book.getDescription());
			ps.setInt(3, book.getQuantity());
			ps.setString(4, book.getId());
			int i = ps.executeUpdate();
			if (i!=1){
				con.rollback();
				throw new BookException("Update book FAILED. Expected 1 item to get updated but got: " + i);
			}
		} catch (SQLException e) {			
			String msg = "Got an SQLException when creating updaing a book.  id: " + book.getId() 
					+ ".  Error: " + e;					
			throw new BookException(msg,e);
		}finally{
			JdbcUtils.closeConnectionAndPS(con, ps);
		}
		return book;
	}

	@Override
	public Book getBook(String id) throws BookException, DataSourceException {
		log.info("get book: " + id);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Book b = null;
		try {
			con = DataSourceManager.BOOK.getDs().getConnection();			
			String sql = "SELECT * from book WHERE id=?";			
			ps = con.prepareStatement(sql);			
			ps.setString(1, id);			
			rs = ps.executeQuery();
			if (rs.next()){
				b = new Book();
				b.setId(id);
				b.setName(rs.getString("name"));
				b.setQuantity(rs.getInt("quantity"));
				b.setDescription(rs.getString("description"));
				rs.close();
				sql = "SELECT COUNT(*) as borrowed_count FROM borrowed_list WHERE book_id=?";
				ps = con.prepareStatement(sql);
				ps.setString(1, id);
				rs = ps.executeQuery();
				if(rs.next()) {
					b.setBorrowedCount(rs.getInt("borrowed_count"));
				} else {
					throw new BookException("Was not able to determine the number of borrowers of book with ID " + id);
				}				
			}
			

		} catch (SQLException e) {			
			String msg = "Got an SQLException when creating reading a book.  id: " + id 
					+ ".  Error: " + e;					
			throw new BookException(msg,e);
		}finally{
			JdbcUtils.closeConnectionAndPS(con, ps);
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ignoreMe) {					
				}
			}
		}		
		return b;
	}

	@Override
	public boolean deleteBook(String id) throws BookException, DataSourceException {
		log.info("get book: " + id);
		Connection con = null;
		PreparedStatement ps = null;		
		try {
			con = DataSourceManager.BOOK.getDs().getConnection();			
			String sql = "DELETE from book WHERE id=?";			
			ps = con.prepareStatement(sql);			
			ps.setString(1, id);			
			int i = ps.executeUpdate();
			if (i==1){
				return true;
			}else{
				con.rollback();
				return false;
			}
			
		} catch (SQLException e) {			
			String msg = "Got an SQLException when creating deleting a book.  id: " + id 
					+ ".  Error: " + e;			
			throw new BookException(msg,e);
		}finally{
			JdbcUtils.closeConnectionAndPS(con, ps);		
		}		
	}


}
