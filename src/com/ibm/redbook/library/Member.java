package com.ibm.redbook.library;

import java.util.ArrayList;
import java.util.List;

import com.ibm.redbook.library.exceptions.MemberException;

/**
 * Represents a member in the library. Members have a number of books on loan, as well as a style to be applied to their home page
 */
public class Member {

	private List<String> bookIdsOnLoan;
	private String memberId;
	private String password;
	private String backgroundColor;
	private String foregroundColor;
	
	public Member() {
		bookIdsOnLoan = new ArrayList<String>();
		backgroundColor = null;
		foregroundColor = null;;
	}
	
	/**
	 * checks to see if the password provided matches the member's password
	 * @param password the password to check against the member's stored password. This value can be null
	 * @return true if the password passed in is not null, and if it matches the member's password. If a null password was passed in this always returns false
	 */
	public boolean verifyPassword(String password){
		return (password != null && password.equals(this.password));
		
	}
	
	/**
	 * Get the list of books this member has borrowed
	 * @return The list of Id's (not book objects) representing books the member has on loan 
	 */
	public List<String> getBorrowedBookIds() {
		return bookIdsOnLoan;
	}
	
	/**
	 * Will remove a book from the list of books the member is borrowing
	 * @param bookId The Id of the book to return
	 * @throws MemberException if the member is not borrowing the book trying to be returned
	 */
	public void returnBorrowedBook(String bookId) throws MemberException {
		if (bookIdsOnLoan.contains(bookId)) {
			bookIdsOnLoan.remove(bookId);
		} else {
			throw new MemberException("The member does not have book with id " + bookId);
		}
	}
	
	/**
	 * Adds the book to the list of books being borrowed
	 * @param bookId the Id of the book to borrow
	 * @throws MemberException if the member is already borrowing the book
	 */
	public void borrowBook(String bookId) throws MemberException {
		if (bookIdsOnLoan.contains(bookId)) {
			throw new MemberException("The member already has a book with id " + bookId);
		} else {
			
			bookIdsOnLoan.add(bookId);
		}
	}
	
	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getForegroundColor() {
		return foregroundColor;
	}

	public void setForegroundColor(String foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String userId) {
		this.memberId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
