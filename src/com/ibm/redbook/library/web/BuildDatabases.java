package com.ibm.redbook.library.web;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.redbook.library.dao.DataSourceManager;
import com.ibm.redbook.library.testdata.DatabaseStructureUtil;
import com.ibm.redbook.library.util.WebAttributes;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Called form the main page and used construct the database, making it easier to test the application. Note that this will return a forbidden message if test mode is not enabled
 */
public class BuildDatabases extends HttpServlet {
	private static final long serialVersionUID = -7662287835536102668L;
	Logger log = Logger.getLogger(getClass().getName());

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// We should not be building the database from a production system
		String inTesting = null;
		try {
			//$ANALYSIS-IGNORE
			inTesting = (String) new InitialContext()
								.lookup("java:comp/env/inTesting");
		} catch (NamingException e) {
			log.log(
					Level.SEVERE, "Cannot lookup if we are running test mode or not"
							+ e.getMessage(), e);
		}
		if ("true".equalsIgnoreCase(inTesting)) {
			//log.log(Level.INFO, "init db request from server " + org.apache.catalina.util.ServerInfo.getServerInfo());
			try {
				DatabaseStructureUtil
						.initializeMemberDatabase(DataSourceManager.MEMBER
								.getDs());
				DatabaseStructureUtil
						.initializeStyleDatabase(DataSourceManager.STYLE
								.getDs());
				DatabaseStructureUtil
						.initializeBookDatabase(DataSourceManager.BOOK.getDs());
				request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(),
						"Databases created.");
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute(WebAttributes.RETURN_MESSAGE.toString(), "Data base creation failed");
			}
			forward(request, response, "/jsp/viewMessage.jsp");
		} else {
			response.sendError(
					HttpsURLConnection.HTTP_FORBIDDEN,
					"These materials are forbidden");
		}
	}

	private void forward(HttpServletRequest request,
			HttpServletResponse response, String url) throws ServletException,
			IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}
}
