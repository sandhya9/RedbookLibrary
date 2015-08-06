package com.ibm.redbook.library.web;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.redbook.library.dao.DataSourceManager;
import com.ibm.redbook.library.testdata.DatabaseTestDataUtil;
import com.ibm.redbook.library.util.WebAttributes;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Populates some simple test data in the database. Should not be made available in production.
 */
public class PopulateTestData extends HttpServlet {	
	private static final long serialVersionUID = -7662287835536102668L;
    Logger log = Logger.getLogger(getClass().getName());	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			log.log(Level.INFO, "init db request");
			try{
				DatabaseTestDataUtil.populateMemberDatabase(DataSourceManager.MEMBER.getDs());
				DatabaseTestDataUtil.populateStyleDatabase(DataSourceManager.STYLE.getDs());
				DatabaseTestDataUtil.populateBookDatabase(DataSourceManager.BOOK.getDs());
				request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "Databases created." );
			}catch (Exception e){
				log.log(Level.SEVERE, e.getMessage(), e);
				request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "Error returned from populating databases");
			}				
			forward(request, response, "/jsp/viewMessage.jsp");
	}

	private void forward(HttpServletRequest request, HttpServletResponse response, String url) 
	 	throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
}
