<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.ibm.redbook.library.Book"%>
<%@page import="com.ibm.redbook.library.util.WebActions"%>
<%@page import="com.ibm.redbook.library.util.WebAttributes"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String fgcolor = (String) request
			.getAttribute(WebAttributes.FOREGROUND_COLOR.toString());
%>
<%
	String bgcolor = (String) request
			.getAttribute(WebAttributes.BACKGROUND_COLOR.toString());
%>
<%
	String returnMessage = (String) request.getAttribute(WebAttributes.RETURN_MESSAGE.toString());
%>
<html>
<body style="color: <%=fgcolor%>; background-color: <%=bgcolor%>">
	<h1>Member Home</h1>
	<p>
		Current Time:
		<%=new Date()%></p>
	<p><%=request.getAttribute(WebAttributes.INFO_STRING.toString())%></p>
	<% if (returnMessage != null) out.println("<p>"+returnMessage+"</p>"); %>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>Name</th>
				<th>Description</th>
				<th>Action</th>
			</tr>
		</thead>
		<tbody>
			<%
				for (int i = 0; i < (Integer) request.getAttribute(WebAttributes.BOOK_COLLECTION_COUNT.toString()); i++) {
					out.println("<tr>");
					Book book = (Book) request
							.getAttribute(WebAttributes.BOOK_COLLECTION.toString()
									+ i);
					out.println("<td>" + book.getId() + "</td>");
					out.println("<td>" + book.getName() + "</td>");
					out.println("<td>" + book.getDescription() + "</td>");
					out.println("<td><form action=\"" + request.getContextPath()
							+ "/UserBookingServlet\" method=\"POST\">");
					out.println("<input type=\"submit\" value=\"Return Book\" />");
					out.println("<input type=\"hidden\" name=\""+WebAttributes.SINGLE_BOOK_ID+"\" value=\""
							+ book.getId() + "\" />");
					out.println("<input type=\"hidden\" name=\""
							+ WebAttributes.REQUEST_TYPE.toString() + "\" value=\""
							+ WebActions.RETURN.toString() + "\" />");
					out.println("</form></td>");
					out.println("</tr>");
				}
			%>
		</tbody>
	</table>

	<h2>Search for a new book</h2>
	<form method="post"
		action="<%=request.getContextPath() + "/UserBookingServlet"%>">
		<input type="hidden" name="<%=WebAttributes.REQUEST_TYPE.toString()%>"
			value="<%=WebActions.SEARCH.toString()%>" />
		<p>
			Book Number <input type="text" name="<%=WebAttributes.SINGLE_BOOK_ID.toString() %>" value="" />
		<p>
			<input type="submit" value="Search" />
		</p>
	</form>

	<h2>Set color scheme</h2>
	<form method="post"
		action="<%=request.getContextPath() + "/UserBookingServlet"%>">
		<input type="hidden" name="<%=WebAttributes.REQUEST_TYPE.toString()%>"
			value="<%=WebActions.SET_COLORS.toString()%>" />
		<p>
			Foreground color <input type="text"
				name="<%=WebAttributes.FOREGROUND_COLOR.toString()%>" value="" />
		</p>
		<p>
			Background color <input type="text"
				name="<%=WebAttributes.BACKGROUND_COLOR.toString()%>" value="" />
		</p>
		<p>
			<input type="submit" value="Set" />
		</p>
	</form>

</body>
</html>


