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
	<title>내 시간표 목록</title>
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
					
					var printListTB =$('<table  align="center" width="100%" />');
					
					var head = $('<tr align="center"  bgcolor="#99ccff" />').append(
							$("<th />").append($("<b />").text("구분")),
							$("<th />").append($("<b />").text("학년")),
							$("<th />").append($("<b />").text("수강학과")),
							$("<th />").append($("<b />").text("수강번호")),
							$("<th />").append($("<b />").text("교과목명")),
							$("<th />").append($("<b />").text("학점")),
							$("<th />").append($("<b />").text("시간")),
							$("<th />").append($("<b />").text("담당교수")),
							$("<th />").append($("<b />").text("강의시간")),
							$("<th />").append($("<b />").text("강의실")),
							$("<th />").append($("<b />").text("소영역")),
							$("<th />").append($("<b />").text("비고")),
							$("<th />").append($("<b />").text("평점"))
						);
					if(type == 'selected'){<!--전체 목록 표시구역-->
						head.append($("<th />").append($("<b />").text("후기(everytime)").append($("<lable class='help_icon' onClick='fn_reviewHelp()'/>").text(" ?"))));
					}else{
						head.append($("<th />").append($("<b />").text("중복 수강번호").append($("<label class='help_icon' onClick='fn_duplicateHelp()' />").text(" ?"))));
					}
					head.append($("<th />").append($("<b />").text("삭제하기")));
					printListTB.append(head);
					
					for(var i in jsonInfo.list){
						
						var row = $('<tr align="center">').append(
								$("<td />").append($("<b />").text(jsonInfo.list[i].div)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].grade)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].major)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].lectureNumber)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].lecture)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].credit)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].time)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].prof)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].lectureTime)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].lectureRoom)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].part)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].note)),
								$("<td />").append($("<b />").text(jsonInfo.list[i].score))
							);
						if(type == 'selected'){<!--전체 목록 표시구역-->
							row.append($("<td />").append($("<b />").append($("<button type = 'button' value='보기' onClick='articlePage(\""+jsonInfo.list[i].link+"\")'>").text("보기"))));
						}else{<!--중복 표시구역-->
							row.append($("<td />").append($("<b />").text(jsonInfo.list[i].overlapNumber)));
						}
						row.append($("<td />").append($("<b />").append($("<button type = 'button' value='삭제' onClick='deleteLecture(\""+jsonInfo.list[i].lectureNumber+"\")'>").text("삭제"))));
						printListTB.append(row);
					}//for
					
					
					if(type == 'selected'){<!--전체 목록 표시구역-->
						$('.section1').remove;
						$('.section1').html(printListTB);
						if(data=="noData"){
							alert('데이터 없음!');
						}
					}else{<!--중복된 목록 표시구역-->
						if(data!="noData"){
							$('.section2').remove;
							$('.section2').html(printListTB);
							<!--var table = document.getElementId("scheduleTable");-->
							<!--var tr = table.getElementsByTagName("tr");-->
							var color = "#" + Math.round(Math.random() * 0xFFFFFF).toString(16);
							var prior = jsonInfo.list[0].overlapNumber;
							
							for(var i =0 ; i<jsonInfo.list.length ; i++ ){<%-- 중복된 것 끼리 색상 동일하게 --%>
								console.log(i+"--"+jsonInfo.list[i].overlapNumber)
								if(prior != jsonInfo.list[i].overlapNumber){
									color = "#" + Math.round(Math.random() * 0xFFFFFF).toString(16);
								}
								prior = jsonInfo.list[i].overlapNumber;
								$(".section2 > table > tbody > tr:nth-child("+(i+2)+")").css("background",color);
								<!--background color-->
							}
							
						}
					}
					
				},
				error: function(request,status,error){
					alert(request.status+"\n"+request.responseText+"asfd"+error)
				}
			})
			
		}//loadList
		
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
		}<!--deleteLecture-->
		
		function fn_duplicateHelp(){<!--modal-->
			$("#modal_duplicate").fadeIn(); 
		} 
		function fn_reviewHelp(){<!--modal-->
			$("#modal_review").fadeIn(); 
		}
		$(function(){$(".modal_content").click(function(){ 
			$(".modal").fadeOut(); })
		}); 
		
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
		
		<jsp:include page="title.jsp" flush="true">
			<jsp:param value="${ub.userId }" name="userID"/>
		</jsp:include>
		
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
		
		<div class="modal" id ="modal_review"> 
			<div class="modal_content" title="클릭하면 창이 닫힙니다."> 
				아래의 버튼을 클릭하시면<br> 
				에브리타임의 과목 후기 사이트로 이동합니다.
			</div>
		</div>
		
		<div class="modal" id ="modal_duplicate"> 
			<div class="modal_content" title="클릭하면 창이 닫힙니다."> 
				아래의 색상 및 번호는<br> 
				동시간에 겹치는 과목을 나타냅니다.
			</div>
		</div>
	</c:otherwise>
</c:choose>
</body>
</html>