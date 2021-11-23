<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"
import ="schedule.*"
isELIgnored="false"  %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 가입창</title>
<link rel="stylesheet" href="style.css">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script type="text/javascript">
	function transVerify(){
		$('#reduplicateCheck').html("중복확인")
		$('#reduplicateCheck').attr("disabled",false);
	}
	
	function fn_sendInfo(){
		var _id = $("#id").val();
		var _pw = $("#pwd").val();
		var _pwCheck = $("#pwdCheck").val();

		if(_id.length==0 ||_id==""){
		    alert("아이디는 필수입니다.");
		}else if(_pw.length==0 ||_pw==""){
			alert("비밀번호는 필수입니다.");
		}else if(_pwCheck.length==0 ||_pwCheck==""&&_pw!=_pwCheck){
			alert("비밀번호가 일치하지 않습니다.");
		}else if($("#reduplicateCheck").html() == "사용가능"){
		    return true;
		}else{
			alert("중복검사를 해주십시오.");
		}
		return false;
	}
	
	function fn_checkReduplication(){
		var _id = $("#id").val();
		
		if(_id.length==0 ||_id==""){
		    alert("아이디는 필수입니다.");
		}else{
			$.ajax({
				type: "post",
				async: "false",
				url: "${pageContext.request.contextPath}/signUp",
				dataType: "text",
				data: {id: $("#id").val(), command: "reduplicateCheck"},
				success: function (data, textStatus){
					if(data == 'usable'){
						alert("사용 가능한 ID입니다.");
						$('#reduplicateCheck').html("사용가능")
						$('#reduplicateCheck').attr("disabled",true);
					}else if(data == 'n_usable'){
						alert("이미 있는 ID입니다.");
					}else{
						alert("아이디에 특수문자는 불가합니다!");
					}
				},
				error: function(request,status,error){
					alert(request.status+"afds"+request.responseText+"asfd"+error)
				}
			})
		}
	}
</script>
</head>
<body>
	<header style="height:10rem;">
		
	</header>
	<form name="frmUser" action="signUp" onsubmit="return fn_sendInfo()">
	<table align="center" style="border:1px solid #dcdcdc;">
		<th>회원 가입창</th>
		<tr>
			<td>아이디</td>
			<td><input type="text" id="id" name="id" onclick="transVerify()"/></td>
			<td><button type="button" value = "중복확인" id = "reduplicateCheck" onclick ="fn_checkReduplication()">중복확인</button></td>
		</tr>
		<tr>
			<td>비밀번호</td>
			<td><input type="password" id="pwd" name="pwd"/></td>
		</tr>
		<tr>
			<td>비밀번호 확인</td>
			<td><input type="password" id="pwdCheck" /></td>
	 	</tr>
		<!--  <tr>
		 <td>이름</td>
			<td><input type="text" name="name"></td>
		  </tr>
		    <tr>
			<td>이메일</td>
			<td><input type="text" name="email"></td>
		  </tr>-->
		  <tr>
		  	<td colspan="2"><button type="button" value="뒤로가기" style="margin-top:0.2rem;" onclick="history.back();">뒤로가기</button>
		  	<button type="reset" value="다시입력">다시입력</button></td>
		  	
		  	<td><button type="submit" value="가입하기" style="margin-left:auto;">가입하기</button></td>
		  </tr>  
	  </table>

   <input  type="hidden" name = "command" id="command" value="addUser" />	
  </form>
 </body>
</html>
