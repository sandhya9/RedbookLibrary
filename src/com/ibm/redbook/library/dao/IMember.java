package com.ibm.redbook.library.dao;

import com.ibm.redbook.library.Member;
import com.ibm.redbook.library.exceptions.DataSourceException;
import com.ibm.redbook.library.exceptions.MemberException;

/**
 * represents a member in the database, along with their style information and what books they have on loan
 */
public interface IMember {
	/**
	 * Adds a member to the database
	 * @param member The member to add
	 * @return the member added
	 * @throws MemberException if the member is already in the database, or was not added correctly
	 * @throws DataSourceException if it wasn't possible to get a required data source
	 */
	public Member addMember(Member member) throws MemberException, DataSourceException;
	/**
	 * Updates members details, borrowed books and style in the databases
	 * @param member the member to update
	 * @return the updated member
	 * @throws MemberException if the member couldn't be updated
	 * @throws DataSourceException if it wasn't possible to get a required data source
	 */
	public Member updateMember(Member member) throws MemberException, DataSourceException;
	/**
	 * Retrieves the member from the database, including their style information, list of borrowed books, and their personal details
	 * @param id The Id of the user in the database
	 * @return An object representing the member
	 * @throws MemberException if the member's details couldn't be retrieved
	 * @throws DataSourceException if it wasn't possible to get a required data source
	 */
	public Member getMember(String id) throws MemberException, DataSourceException;
	/**
	 * Deletes a member from the database
	 * @param id the Id of the member to delete
	 * @return the deleted member
	 * @throws MemberException if it wasn't possible to delete the member
	 * @throws DataSourceException if it wasn't possible to get a required data source
	 */
	public boolean deleteMember(String id) throws MemberException, DataSourceException;
}
