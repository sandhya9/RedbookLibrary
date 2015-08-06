<%@page import="java.util.Date"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="com.ibm.redbook.library.util.WebActions"%>
<%@page import="com.ibm.redbook.library.util.WebAttributes"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Library Demo App</title>
</head>
<body>
	<h1>Welcome to the IBM Redbooks Publications Application Demo</h1>
	<p>
		Current Time:
		<%=new Date()%></p>
	<ul>
		<li>Login and manage your books
			<form method="post" id="0"
				action="<%=request.getContextPath()%>/UserBookingServlet">
				<p>
					User ID: <input type="text" name="<%=WebAttributes.MEMBER_ID.toString() %>" value="" />
				</p>
				<p>
					Password:<input type="password" name="<%=WebAttributes.MEMBER_PASSWORD.toString() %>" value="" />
				</p>
				<p>
					<input type="submit" value="Login" />
				</p>
				<input type="hidden" name="<%=WebAttributes.REQUEST_TYPE.toString()%>"
					value="<%=WebActions.VERIFY.toString()%>" />
			</form>
		</li>
		<!-- The values below will not be sent to the browser unless test mode is enabled -->
		<%
			if (Boolean.parseBoolean((String) new InitialContext()
					.lookup("java:comp/env/inTesting"))) {
				out.println("<li><a href='test/BuildDatabases'>Build The Databases</a></li>");
				out.println("<li><a href='test/PopulateTestData'>Populate the Database with test data</a></li>");
			}
		%>
	</ul>

</body>
</html>