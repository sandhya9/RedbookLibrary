package com.ibm.redbook.library.dao;

import com.ibm.redbook.library.Book;
import com.ibm.redbook.library.exceptions.BookException;
import com.ibm.redbook.library.exceptions.DataSourceException;

/**
 * Representation of a book in the data base.
 */
public interface IBook {
	/**
	 * Adds a book to a database
	 * @param book The book to add
	 * @return A copy of the book
	 * @throws BookException if the book already exists
	 * @throws DataSourceException if it wasn't possible to get a required data source
	 */
	public Book addBook(Book book) throws BookException, DataSourceException;
	/**
	 * Updates a book in the database
	 * @param book The book to update
	 * @return the book that was updated
	 * @throws BookException if the book does not already exist
	 * @throws DataSourceException if it wasn't possible to get a required data source 
	 */
	public Book updateBook(Book book) throws BookException, DataSourceException;
	/**
	 * retrieves a book from the database
	 * @param id The Id of the book to retrieve
	 * @return an object representing the book
	 * @throws BookException If the book wasn't in the database
	 * @throws DataSourceException if it wasn't possible to get a required data source
	 */
	public Book getBook(String id) throws BookException, DataSourceException;
	/**
	 * deletes the book from the database
	 * @param id the Id of the book to delete
	 * @return an object representing the book
	 * @throws BookException if the book does not exist
	 * @throws DataSourceException if it wasn't possible to get a required data source
	 */
	public boolean deleteBook(String id) throws BookException, DataSourceException;
}
