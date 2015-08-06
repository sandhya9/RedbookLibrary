package com.ibm.redbook.library;

import com.ibm.redbook.library.exceptions.BookException;

/**
 * Represents a single book. The book has a number of individual properties, as well as a quantity and a count of the number being borrowed. 
 */
public class Book {
	
	private String id;
	private String name;
	private String description;
	private int quantity;
	private int borrowedCount = 0;
	
	public void setBorrowedCount(int borrowedCount) {
		this.borrowedCount = borrowedCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getNumberAvailable() {
		return quantity - borrowedCount;
	}
	
	/**
	 * Reduces the amount of  books available by one, assuming any are available.
	 * @throws BookException if there are no copies available
	 */
	public void borrowBook() throws BookException {
		if (borrowedCount >= quantity) {
			throw new BookException("All books are already borrowed");
		} else {
			this.borrowedCount++;
		}
	}
	
	/**
	 * Increases the available number of books by one, assuming there are copies on loan
	 * @throws BookException if there are no copies on loan
	 */
	public void returnBook() throws BookException {
		if (borrowedCount < 1) {
			throw new BookException("All books are already returned");
		} else {
			this.borrowedCount--;
		}
	}
	
	
}
