<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import=" java.util.*,schedule.*"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Logout...</title>
</head>
<body>
	<%
		session.setAttribute("isLogon", false);
		session.setAttribute("ub", null);
	%>
	<script>
		location.href='login.html';
	</script>
</body>
</html>