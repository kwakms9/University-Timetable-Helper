<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import=" java.util.*,schedule.*"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  boolean existSession = false;
  request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>대학 시간표 도우미</title>
	<link rel="stylesheet" href="style.css">
	<script>
	$(function(){ <!--modal-->
		$("#info").click(function(){
			$("#modal_myInfo").fadeIn(); }); 
		$(".close_modal").click(function(){ 
			$(".modal").fadeOut(); }); 
	});
	
	function changePW(){
		var _id = "${param.userID }";
		var _pw = $("#pw").val();
		var _npw = $("#npw").val();
		var _rnpw = $("#rnpw").val(); 
		
		if(_pw.length==0 ||_pw==""||_npw.length==0 ||_npw==""){
			alert("비밀번호를 입력해주십시오.");
		}else if(_npw!=_rnpw){
			alert("새 비밀번호가 일치하지 않습니다!");
		}else{
			$.ajax({
				type: "post",
				async: "false",
				url: "${pageContext.request.contextPath}/UserDataServlet",
				
				data: {userId: _id, pw: _pw, npw: _npw, command: "changePW"},
				success: function (data, textStatus){
					if(data=="success"){
						alert("수정완료!");
						$(".modal").fadeOut();
					}else if(data=="not_match"){
						alert("기존비밀번호가 일치하지 않습니다!");
					}else{
						alert("로그인 되지않습니다.");
						location.href='login.html';
					}
				
				},error: function(request,status,error){
					alert(request.status+"\n"+request.responseText+"\n"+error)
				}
			})//ajax
		}
	}//changePW
	
	function withdrawUser(){
		var _id = "${param.userID }";
		var result = confirm("정말로 탈퇴하시겠습니까?"); 
		
		if(result){
			$("#modal_delete").fadeIn();
		}
	}//withdrawUser
	
	function deleteUser(){
		var _id = "${param.userID }";
		var _pw = $("#dpw").val();
		
		$.ajax({
			type: "post",
			async: "false",
			url: "${pageContext.request.contextPath}/UserDataServlet",
			
			data: {userId: _id, pw: _pw, command: "deleteUser"},
			success: function (data, textStatus){
				if(data=="success"){
					alert("탈퇴되었습니다!");
					location.href='login.html';
				}if(data=="not_match"){
					alert("비밀번호가 일치하지 않습니다!");
				}else{
					alert("로그인 되지않습니다.");
					location.href='login.html';
				}
			
			
			},error: function(request,status,error){
				alert(request.status+"\n"+request.responseText+"\n"+error)
			}
		});//ajax
	}//deleteUser
	</script>
	<style>
		.modal_information{
			 width:500px; height:400px;
			 background:#fff; 
			 border-radius:10px;
			 position:relative; 
			 top:30%; 
			 left:50%;
			 margin-top:-100px; 
			 margin-left:-200px;
			 text-align:center;
			 box-sizing:border-box; 
			 padding:20px 0;
			 line-height:23px; 
		}
		.spacing{
			border-collapse: separate;
			border-spacing:0 10px;
		}
	</style>
</head>
<body>

	<div align="center" style="height:100px;">
		<div style="padding:2.5rem;display:flex;">
			<span align='center' style='margin-left:15.5rem;font-size:1.5em;font-weight: bold;flex-grow: 1;'>대학 시간표 도우미</span>
			<label align="right" style="margin-right:1rem;">${param.userID }님 접속</label>
			<button style="margin-right:0.5rem;" type="button" value="내 정보" id="info" onClick="">내 정보</button>
			<button align="right" style="margin-right:2rem;" type="button" value="로그아웃" onClick="location.href='logout.jsp'">로그아웃</button>
		
		</div>
	</div>
	
	<div class="modal" id="modal_myInfo"> <!-- 정보수정 -->
		<div class="modal_information" title="정보수정"> 
		<div style="display:flex;margin-bottom:64px;">
			<label align="center" style="margin:0 0 0 auto;"><b>정보 수정</b></label>
			<label class="close_modal" style="cursor:pointer;margin-left:auto;margin-right:20px;color:#666666;">X</label>
		</div>
		
		<table align="center" class="spacing">
			<tr>
				<td style="text-align:right;">아이디: </td> <td><input type="text" value="${param.userID }" disabled/></td>
			</tr>
			<tr>
				<td style="text-align:right;">기존 비밀번호: </td> <td><input type="password" id = "pw"/></td>
			</tr>
			<tr>
				<td style="text-align:right;">새 비밀번호: </td> <td><input type="password" id = "npw"/></td>
			</tr>
			<tr>
				<td style="text-align:right;">새 비밀번호 확인: </td> <td><input type="password" id = "rnpw"/></td>
			</tr>
			<tr>
				<td colspan="2"><button align="center" type="button" id="changePW" onClick="changePW()" onKeypress="javascript:if(event.keyCode==13) {changePW()}">변경하기</button></td>
			</tr>
			<tr>
				<td colspan="2"><button align="center" type="button" id="withdrawUser" onClick="withdrawUser()">탈퇴하기</button></td>
			</tr>
		</table>
		</div>
	</div>
	<div class="modal" id="modal_delete"> <!-- 탈퇴 -->
		<div class="modal_information"> 
		<div style="display:flex;margin-bottom:64px;">
			<label align="center" style="margin:0 0 0 auto;"><b>탈퇴하기</b></label>
			<label class="close_modal" style="cursor:pointer;margin-left:auto;margin-right:20px;color:#666666;">X</label>
		</div>
		<table align="center" class="spacing">
			<tr>
				<td style="text-align:right;">비밀번호: </td> <td><input type="password" id = "dpw"/></td>
			</tr>
			<tr>
				<td colspan="2"><button align="center" type="button" id="withdrawUser" onClick="deleteUser()">탈퇴하기</button></td>
			</tr>
		</table>
		</div>
	</div>
</head>
<body>

</body>
</html>