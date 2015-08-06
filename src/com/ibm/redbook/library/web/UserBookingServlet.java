package com.ibm.redbook.library.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.redbook.library.Book;
import com.ibm.redbook.library.Member;
import com.ibm.redbook.library.dao.IBook;
import com.ibm.redbook.library.dao.IMember;
import com.ibm.redbook.library.dao.impl.BookJdbcImpl;
import com.ibm.redbook.library.dao.impl.MembersJdbcImpl;
import com.ibm.redbook.library.exceptions.ProcessingException;
import com.ibm.redbook.library.util.DefaultValues;
import com.ibm.redbook.library.util.WebActions;
import com.ibm.redbook.library.util.WebAttributes;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserBookingServlet extends HttpServlet {
	private static final long serialVersionUID = -7662287835536102668L;
	private ServletConfig config;
	Logger log = Logger.getLogger(getClass().getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserBookingServlet() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String notifyOnGet = config.getServletContext().getInitParameter(
				"notifyOnGet");
		if (notifyOnGet == null) {
			log.log(Level.WARNING, "Failed to read the context param notifyOnGet. got a null back");
		} else {
			log.log(Level.INFO, "found the value for the context param: notifyOnGet.  Value is: "
					+ notifyOnGet);
		}
		if ("true".equals(notifyOnGet)) {
			log.log(Level.INFO, "This Servlet was acceessed via a GET request");
		}
		// Don't allow a GET request, instead redirect
		redirect(request, response, "");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.log(Level.INFO, "Processing a post");
		// Variables that store data relating to a member
		IMember mdao = new MembersJdbcImpl();
		Member m = null;
		String memberID = null;
		// Hold a variable to decide if we need to populate general display data
		boolean populateDisplayData = false;
		String forwardingPage = "jsp/viewMessage.jsp";
		request.setAttribute(WebAttributes.INFO_STRING.toString(), config.getInitParameter("INFO_FIELD"));

		// Get the request type. If this is a verify it will need to be handled immediately
		String requestType = (String) request.getParameter(WebAttributes.REQUEST_TYPE.toString());
		log.log(Level.INFO, "Request type is ["+ requestType + "]");
		// First check if the user is verified
		if (WebActions.VERIFY.toString().equalsIgnoreCase(requestType)) {
			log.log(Level.INFO, "Verifying member");
			memberID = request.getParameter(WebAttributes.MEMBER_ID.toString());
			String password = request.getParameter(WebAttributes.MEMBER_PASSWORD.toString());
			if (memberID == null || memberID.trim().equalsIgnoreCase("")) {
				log.log(Level.INFO, "Failed login attempt, no details given, member id was [" + memberID + "]");
				request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "Login details were not correct");
			} else {
				try {
					m = mdao.getMember(memberID);
					if (m == null) {
						log.log(Level.INFO, "Member ID returned null, indicating it is not a member in the database, member id was [" + memberID + "]");
						request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "Login details were not correct");
					} else {
						if (m.verifyPassword(password)) {
							// The member id is set across the session
							request.getSession().setAttribute(WebAttributes.MEMBER_ID.toString(), memberID);
						} else {
							log.log(Level.INFO, "Failed login attempt, password was not verified, member id was [" + memberID + "]");
							request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "Login details were not correct");
						}
					}
				} catch (ProcessingException e) {
					log.log(Level.SEVERE, "Error processing a verification request: " + e.getMessage(), e);
					request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "Unable to process request. An error has been logged");
				}
			}
		}
		
		// If we do not have a member (which is likely unless this is the first request), attempt to get the id from the session
		if (m == null) {
			memberID = (String) request.getSession().getAttribute(WebAttributes.MEMBER_ID.toString());
			try {
				m = mdao.getMember(memberID);
			} catch (ProcessingException e) {
				log.log(Level.SEVERE, "Error retrieving member using member ID " + memberID + ", " + e.getMessage(), e);
				request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "Unable to process request. An error has been logged");
			}
		}
		
		// If we are happy the member ID is good, we can continue
		if (m != null) {
			// At this stage the user is verified, so even if it is not required it is acceptable to provide details to the user request
			populateDisplayData = true;
			
			// All operations at this stage will require some kind of access to the Book data (even the style updates require it to populate further data)
			IBook bdao = new BookJdbcImpl();
			try {
			// Determine the request type
			if (WebActions.SEARCH.toString().equalsIgnoreCase(requestType)) {
				// Handle a Search. This requires a redirect to the viewing page
				// The user does not need their full data populated for this
				populateDisplayData = false;
				log.log(Level.INFO, "Processing search on a book");
				String bookId = request.getParameter(WebAttributes.SINGLE_BOOK_ID.toString());
				Book book = bdao.getBook(bookId);
				if (book == null) {
					request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "No book with ID: " + bookId);
				} else {
					request.setAttribute(WebAttributes.SINGLE_BOOK.toString(), book);
					request.setAttribute(WebAttributes.SINGLE_BOOK_IS_BORROWED.toString(), m.getBorrowedBookIds().contains(book.getId()));
					forwardingPage = "jsp/viewBook.jsp";
				}
			} else if (WebActions.BORROW.toString().equalsIgnoreCase(requestType)) {
				// If the member has decided to borrow, add the record
				log.log(Level.INFO, "Processing borrow to a book");
				String bookId = request.getParameter(WebAttributes.SINGLE_BOOK_ID.toString());
				Book b = bdao.getBook(bookId);
				if (m.getBorrowedBookIds().contains(bookId)) {
					request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "Member already has " + b.getName() + " (ID: "+ b.getId() + ") on loan");
				} else if (b.getNumberAvailable() > 0) {
					m.borrowBook(bookId);
					b.borrowBook();
					// Update all databases that may have been affected
					bdao.updateBook(b);
					// Update the database for the member
					mdao.updateMember(m);
				} else {
					request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "No copies of " + b.getName() + " (ID: "+ b.getId() + ") are available");
				}
			} else if (WebActions.RETURN.toString().equalsIgnoreCase(requestType)) {
				// The member may have removed a book, so remove it
				log.log(Level.INFO, "Processing return of a book");
				String bookId = request.getParameter(WebAttributes.SINGLE_BOOK_ID.toString());
				Book b = bdao.getBook(bookId);
				if (m.getBorrowedBookIds().contains(bookId)) {
					m.returnBorrowedBook(bookId);
					b.returnBook();
					// Update all databases that may have been affected
					bdao.updateBook(b);
					// Update the database for the member
					mdao.updateMember(m);
				} else {
					request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "You tried to return " + b.getName() + " (ID: "+ b.getId() + "), but you are not registered as borrowing it");
				}
			} else if (WebActions.SET_COLORS.toString().equalsIgnoreCase(requestType)) {
				// If a user attempts to set their colors but cannot, register this but allow the application to continue
				String backgroundColor = request.getParameter(WebAttributes.BACKGROUND_COLOR.toString());
				String foregroundColor = request.getParameter(WebAttributes.FOREGROUND_COLOR.toString());
				if (backgroundColor != null && !backgroundColor.trim().equalsIgnoreCase("")) {
					m.setBackgroundColor(backgroundColor);
				}
				if (foregroundColor != null && !foregroundColor.trim().equalsIgnoreCase("")) {
					m.setForegroundColor(foregroundColor);
				}
				mdao.updateMember(m);
			} else if (!WebActions.VERIFY.toString().equalsIgnoreCase(requestType)){
				// If we get a request type that is not recognised, it will be logged, but the member should be redirected to the main page
				log.log(Level.SEVERE, "Unrecognised Web action: ["+ requestType+"]");
			}
			
			// if this has gone successfully, or there was no recognised operation, populate the home page info (assuming there are no errors)
			if (populateDisplayData) {
				// Get the member's color scheme if they have one
				if (m.getBackgroundColor() != null) {
					request.setAttribute(WebAttributes.BACKGROUND_COLOR.toString(), m.getBackgroundColor());
				} else {
					request.setAttribute(WebAttributes.BACKGROUND_COLOR.toString(), DefaultValues.BACKGROUND_COLOR.toString());
				}
				if (m.getForegroundColor() != null) {
					request.setAttribute(WebAttributes.FOREGROUND_COLOR.toString(), m.getForegroundColor());
				} else {
					request.setAttribute(WebAttributes.FOREGROUND_COLOR.toString(), DefaultValues.FOREGROUND_COLOR.toString());
				}
				
				// Get the list of books for the member, and set the number of books to expect
				request.setAttribute(WebAttributes.BOOK_COLLECTION_COUNT.toString(), m.getBorrowedBookIds().size());
				int arrayCounter = 0;
				for (String bookId : m.getBorrowedBookIds()) {
					request.setAttribute(WebAttributes.BOOK_COLLECTION.toString() + arrayCounter, bdao.getBook(bookId));
					arrayCounter++;
				}
				forwardingPage = "jsp/viewUserHome.jsp";
			}
			} catch (ProcessingException e) {
				log.log(Level.SEVERE, "Error during main process using member ID " + memberID + ", " + e.getMessage(), e);
				request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "Unable to process request. An error has been logged");
			}			
			
		} else {
			log.log(Level.INFO, "Member returned null using data from the session, indicating it is not a member in the database, member id was [" + memberID + "]");			
		}

		forward(request, response, forwardingPage);
	}

	private void forward(HttpServletRequest request,
			HttpServletResponse response, String url) throws ServletException,
			IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
	
	private void redirect(HttpServletRequest request, HttpServletResponse response, String url) 
		 	throws ServletException, IOException {
			String encodedURL = response.encodeRedirectURL(url);
			response.sendRedirect( request.getContextPath() + "/" + encodedURL );
		}

}
