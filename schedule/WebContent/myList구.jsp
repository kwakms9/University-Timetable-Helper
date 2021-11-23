<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import=" java.util.*,schedule.*"
    import = "javax.servlet.http.HttpServletRequest"
    import = "java.text.SimpleDateFormat"
    import = "java.net.Inet4Address"
    isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
  boolean existSession = false;
  boolean everyTimeSession = false;
  UserBean bean = null;

  
  request.setCharacterEncoding("UTF-8");
  SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss 접속");
  Date time = new Date();
  System.out.println(format.format(time));
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>myList</title>
	<link rel="stylesheet" href="style.css">
	<style>
		h1 { text-align: center; }
		#filter-content {
			display: none;
		 }
		#filter-toggle {
		    cursor: pointer;
		}
	</style>
	<script type="text/JavaScript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript">
		function loadList(id, type){
			var _id = id;
			$.ajax({
				type: "post",
				async: "false",
				url: "${pageContext.request.contextPath}/loadSchedule",
				
				data: {userId: _id, loadType: type},
				success: function (data, textStatus){
					
					if(data!="noData"){	<!--데이터 존재 유무 확인-->
						var jsonInfo = JSON.parse(data);
					}else{
						var jsonInfo ="";
					}
					
					var printList ='<table  align="center" width="100%" id="scheduleTable">';
					printList += '<tr align="center"  bgcolor="#99ccff">';
					printList +='<td><b>구분</b></td>';
					printList +='<td><b>학년</b></td>';
					printList +='<td><b>수강학과</b></td>';
					printList +='<td><b>수강번호</b></td>';
					printList +='<td><b>교과목명</b></td>';
					printList +='<td><b>학점</b></td>';
					printList +='<td><b>시간</b></td>';
					printList +='<td><b>담당교수</b></td>';
					printList +='<td><b>강의시간</b></td>';
					printList +='<td><b>강의실</b></td>';
					printList +='<td><b>소영역</b></td>';
					printList +='<td><b>비고</b></td>';
					printList +='<td><b>평점</b></td>';
					if(type == 'selected'){<!--전체 목록 표시구역-->{
						printList +='<td><b>후기(everytime)</b></td>';
					}else{
						printList +='<td><b>중복 수강번호</b></td>';
					}
					printList +='<td><b>삭제하기</b></td>';
					printList +='</tr>';
					
					for(var i in jsonInfo.list){
						
						printList +='<tr align="center">';
						printList +='<td><b>'+jsonInfo.list[i].div+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].grade+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].major+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].lectureNumber+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].lecture+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].credit+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].time+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].prof+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].lectureTime+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].lectureRoom+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].part+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].note+'</b></td>';
						printList +='<td><b>'+jsonInfo.list[i].score+'</b></td>';
						if(type == 'selected'){<!--전체 목록 표시구역-->{
							printList +='<td><b><button type="button" value="보기" onClick="articlePage(\''+jsonInfo.list[i].link+'\')">보기</button></b></td>';
						}else{
							printList +='<td><b>'+jsonInfo.list[i].overlapNumber+'</b></td>';
						}
						
						printList +='<td><b><button type="button" value="삭제" onClick="deleteLecture(\''+jsonInfo.list[i].lectureNumber+'\')">삭제</button></b></td>';
						printList +='</tr>';
					}
					printList +='</table>';
					
					if(type == 'selected'){<!--전체 목록 표시구역-->
						$('.section1').remove;
						$('.section1').html(printList);
						if(data=="noData"){
							alert('데이터 없음!');
						}
					}else{<!--중복된 목록 표시구역-->
						if(data!="noData"){
							$('.section2').remove;
							$('.section2').html(printList);
							<!--var table = document.getElementId("scheduleTable");-->
							<!--var tr = table.getElementsByTagName("tr");-->
							var color = "#" + Math.round(Math.random() * 0xFFFFFF).toString(16);
							var prior = jsonInfo.list[0].overlapNumber;
							
							for(var i in jsonInfo.list){<%-- 중복된 것 끼리 색상 동일하게 --%>
								if(prior != jsonInfo.list[i].overlapNumber){
									color = "#" + Math.round(Math.random() * 0xFFFFFF).toString(16);
								}
								$("#scheduleTable:nth-child("+i+")").next("tr").css("background",color);
								<!--tr[i].style.background = color;-->
							}
							
						}
					}
					
				},
				error: function(request,status,error){
					alert(request.status+"\n"+request.responseText+"asfd"+error)
				}
			})
			
		}
		
		function articlePage(link){	<%--에타 후기 보기--%>
			if(link !='empty'){
				window.open(link, "", "left=225,top=10,width=1200,height=850");<%-- 백그라운드로  https://everytime.kr/lecture/view/2288399 처럼 과목 뒷 번호 가져오고 그 값을 가져와서 웹으로 띄우기(에타 선 로그인)--%>	
			}else{
				alert("수강후기가 없습니다.");
			}
		}
		
		function deleteLecture(LectureNumber){<%-- 담은 과목 삭제--%>
			$.ajax({
				type: "post",
				async: "false",
				url: "${pageContext.request.contextPath}/alterSchedule",
				
				data: {lectureNumber: LectureNumber, command: "delete"},
				success: function (data, textStatus){
					if(data=="success"){
						location.reload();
					}else{
						alert('로그인 되지않습니다.'); 
						location.href='login.html';
					}	
				},
				error: function(request,status,error){
					alert(request.status+"\n"+request.responseText+"asfd"+error)
				}
			})
		}
	</script>
</head>
<body>
	<%
	try{
		existSession = ((Boolean)(session.getAttribute("isLogon")));
	}catch(Exception e){
		existSession = false;
	}
	try{
		bean = (UserBean)session.getAttribute("ub");
	}catch(Exception e){
		//userId = "Not Login";
		System.out.println("Not Login");
	}
%>
<c:set var="logged_in" value="<%=existSession %>"/>

<c:choose>
	<c:when test="${!logged_in}">
		<script>
			alert('로그인 되지않습니다.'); 
			location.href='login.html';
		</script>
	</c:when>
	<c:otherwise>
		<jsp:useBean id="ub" class="schedule.UserBean"/>
		<jsp:setProperty property="userId" name="ub" value='<%=((UserBean)session.getAttribute("ub")).getUserId() %>'/>
		
		<div style='margin:19px;display:flex;'>
		<span style='font-size:1.5em;font-weight: bold;flex-grow: 1;'>담아둔 시간표</span>
		<button type='button' value='뒤로가기' style='margin-left: auto;' onclick='javascript:location.href="${pageContext.request.contextPath}/main.jsp"'>뒤로가기</button>
		</div>
		<section1 class = "section1">
			<script>loadList('${ub.userId}',"selected");</script>
		</section1>
		<hr>
		<h2 style='margin:19px;'>중복된 시간표</h2>
		<section2 class = "section2">
			<table  align="center" width="100%" id="scheduleTable"></table>
			<script>loadList('${ub.userId}', "duplicate");</script>
		</section2>
		<hr>
	</c:otherwise>
</c:choose>
</body>
</html>