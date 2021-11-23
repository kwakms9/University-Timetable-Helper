<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import=" java.util.*,schedule.*,org.springframework.beans.factory.BeanFactory,
    org.springframework.beans.factory.xml.XmlBeanFactory,org.springframework.core.io.FileSystemResource"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  request.setCharacterEncoding("UTF-8");
%> 
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>login...</title>
</head>
<body>
<jsp:useBean id="ub" class="schedule.UserBean" />
<jsp:setProperty property="*" name="ub"/>


<%
	ub.setUserId(request.getParameter("id"));
	ub.setUserPw(request.getParameter("pw"));
	boolean result = false;
	BeanFactory factory = new XmlBeanFactory(new FileSystemResource("C:\\myJSP\\workspace\\schedule\\bean.xml"));
	UserDAO dao = (UserDAO) factory.getBean("userDAO");
	result = dao.checkInfo(ub);
%>
<c:choose>
	<c:when test="<%=result %>">
		<%
			session.setAttribute("isLogon", true);
			session.setAttribute("ub", ub);
		%>
		<c:redirect url="main.jsp"/>
	</c:when>
	<c:otherwise>
		<script>
			alert('입력정보가 일치하지 않습니다.'); 
			location.href='login.html';
		</script>
	</c:otherwise>
</c:choose>

</body>
</html>