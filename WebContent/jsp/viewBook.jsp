<%@page import="java.util.Date"%>
<%@page import="com.ibm.redbook.library.Book"%>
<%@page import="com.ibm.redbook.library.util.WebActions"%>
<%@page import="com.ibm.redbook.library.util.WebAttributes"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<body>
	Current Time:
	<%=new Date()%><br />
	<%
		Book b = (Book) request.getAttribute(WebAttributes.SINGLE_BOOK.toString());
		if (b == null) {
			out.println("Did not find any book on the request to display.");
			return;
		}
	%>
	<%=request.getAttribute(WebAttributes.INFO_STRING.toString()) %><br />
	Matching books
	<br />
	<ul>
	<li>ID: <%=b.getId() %></li>
	<li>Name: <%=b.getName() %></li>
	<li>Description: <%=b.getDescription() %></li>
	<li>Availability: <%=b.getNumberAvailable() %>/<%=b.getQuantity() %></li>
	</ul>
	
	<form action="<%=request.getContextPath() + "/UserBookingServlet"%>" method="post">
		Book Number <input type="text" name="<%=WebAttributes.SINGLE_BOOK_ID.toString() %>" value="<%=b.getId()%>" />
		<input type="hidden" name="<%=WebAttributes.REQUEST_TYPE.toString()%>"
			value="<%=WebActions.BORROW.toString()%>" /> <br />
		<input type="submit" value="Borrow" />
	</form>
	<a href="<%=request.getContextPath()%>">Back to main page</a>
	
</body>
</html>


