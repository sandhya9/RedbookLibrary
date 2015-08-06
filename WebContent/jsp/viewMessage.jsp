<%@page import="java.util.Date"%>
<%@page import="com.ibm.redbook.library.util.WebAttributes"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Message Returned</title>
</head>
<body>
	<h1>
		Message Returned at
		<%=new Date()%></h1>
	<p><%=(String) request
					.getAttribute(WebAttributes.RETURN_MESSAGE.toString())%></p>
	<p>
		<a href="<%=request.getContextPath()%>">Back to main page</a>
	</p>
</body>
</html>