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
  request.setCharacterEncoding("UTF-8");
  SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss 접속");
  Date time = new Date();
  System.out.println(format.format(time));
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>tmp</title>
	<link rel="stylesheet" href="style.css">
	<style>
		h1 { text-align: center; }
		#filter-content {
			display: none;
		 }
		#filter-toggle {
		    cursor: pointer;
		}
		 input, select{ 
            border:1px solid #dcdcdc;
            border-radius:6px;
        }
	</style>
	<script type="text/JavaScript" src="http://code.jquery.com/jquery-latest.min.js"></script>
	<script type="text/javascript">
		function fn_search(){
			var _queryContent = $("#queryContent").val();
			var classificationStr = "";
           	var online = false;
           		
           	if($("input:checkbox[name=onlyOnline]").is(":checked") == true){<%-- 가상강좌만 보기 --%>
           		online = true;
           	}
			$("input[name=classification]:checked").each(function() {
                var test = $(this).val();
                console.log(test);
               
                classificationStr+=","+test;
            });
			
			
			$.ajax({
				type: "post",
				async: "false",
				url: "${pageContext.request.contextPath}/search",
				
				data: {classification: classificationStr , queryContent: "%"+$("#queryContent").val()+"%", 
					score: $("#score").val(), filterQuery: $("#filterQuery").val(), onlyOnline: online},
				success: function (data, textStatus){
					if(data!="fail"){
						var jsonInfo = JSON.parse(data);
						var printList ='<table  align="center" width="100%">';
						printList += '<tr align="center"  bgcolor="#99ccff">';
						printList +='<th><b>구분</b></th>';
						printList +='<th><b>학년</b></th>';
						printList +='<th><b>수강학과</b></th>';
						printList +='<th><b>수강번호</b></th>';
						printList +='<th><b>교과목명</b></th>';
						printList +='<th><b>학점</b></th>';
						printList +='<th><b>시간</b></th>';
						printList +='<th><b>담당교수</b></th>';
						printList +='<th><b>강의시간</b></th>';
						printList +='<th><b>강의실</b></th>';
						printList +='<th><b>소영역</b></th>';
						printList +='<th><b>비고</b></th>';
						printList +='<th><b>평점</b></th>';
						printList +='<th><b>후기(everytime)</b></th>';
						printList +='<th><b>시간표 저장</b></th>';
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
							printList +='<td><b><button type="button" value="보기" onClick="articlePage(\''+jsonInfo.list[i].link+'\')">보기</button></b></td>';
							printList +='<td><b><button type="button" value="담기" onClick="addLecture(\''+jsonInfo.list[i].lectureNumber+'\')">담기</button></b></td>';
							printList +='</tr>';
						}
						printList +='</table><hr>';
						$('#showList').remove;
						$('#showList').html(printList);
					}else{
						alert('로그인 되지않습니다.'); 
						location.href='login.html'
					}
				},
				error: function(request,status,error){
					alert(request.status+"afds"+request.responseText+"asfd"+error)
				}
			})
			
		}<%--and fn_search()--%>
		
		function articlePage(link){	<%--에타 후기 보기--%>
			if(link !='empty'){
				window.open(link, "", "left=225,top=10,width=1200,height=850");<%-- 백그라운드로  https://everytime.kr/lecture/view/2288399 처럼 과목 뒷 번호 가져오고 그 값을 가져와서 웹으로 띄우기(에타 선 로그인)--%>	
			}else{
				alert("수강후기가 없습니다.");
			}
		}
		
		function everytimeLogin(){
			var _url = "https://daegu.everytime.kr/lecture";
			window.open(_url, "", "left=225,top=10,width=1200,height=850");
		}
		
		function openCloseFilter() { <!--필터 fold -->
		    if(document.getElementById('filter-content').style.display === 'block'){
		      	document.getElementById('filter-content').style.display = 'none';
		      	document.getElementById('filter-toggle').textContent = '▶';
		    } else {
		      	document.getElementById('filter-content').style.display = 'block';
		      	document.getElementById('filter-toggle').textContent = '◀';
		    }
		  }
		
		function mySchedule(){
			location.href="myList.jsp";
		}
		
		function addLecture(number){
			$.ajax({
				type: "post",
				async: "false",
				url: "${pageContext.request.contextPath}/alterSchedule",
				
				data: {lectureNumber: number, command: "add"},
				success: function (data, textStatus){
					if(data!="fail"){
						alert(data);
					}else{
						alert('로그인 되지않습니다.'); 
						location.href='login.html'
					}
				},
				error: function(request,status,error){
					alert(request.status+"afds"+request.responseText+"asfd"+error)
				}
			});
		}
		
		function logout(){ 
			location.href='logout.jsp';
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
		<%
			String id;
			ub=(UserBean)session.getAttribute("ub");
			id = ub.getUserId();
		%>
		
		<%--jsp:include page="title.jsp" flush="true">
			<jsp:param value="<%=name %>" name="name"/>
			<jsp:param value="<%=account %>" name="account"/>
		</jsp:include--%>
		
		<c:set var = "userId" value="<%=id %>"/>		
			<%--쿼리 조건(체크밖스드롭박스)
				이수구분, 검색명(강의이름또는 교수명으로 검색되도록), 별점 이상 출력
			--%>
	<table  align="center" >
		<tr align="center" >
			<td>구분</td>
			<td>
				<input type="checkbox" name="classification" value="공통" checked>공통
				<input type="checkbox" name="classification" value="균형">균형
				<input type="checkbox" name="classification" value="교직">교직
				<input type="checkbox" name="classification" value="자유">자유
				<input type="checkbox" name="classification" value="일선">일선
				<input type="checkbox" name="classification" value="전선">전선
				<input type="checkbox" name="classification" value="전필">전필
			</td>
			<td><div id ="userInfo">
					<button type="button" onclick="mySchedule()" style="width:6rem;" value="시간표보기">시간표보기</button>
					<button type="button" onclick="logout()" value="로그아웃">로그아웃</button>
						
				</div>
			</td>
		</tr>
		<tr align="center" >
			<td>검색(교수, 강의이름, 수강번호):</td>
			<td>
				<input type="text" id = "queryContent" size="40" placeholder="  Search..." onKeypress="javascript:if(event.keyCode==13) {fn_search()}"/>
				<select id="score" id="score">
					<option value="0.0">미선택</option>
					<option value="5.0">5.0</option>
					<option value="4.5">4.5이상</option>
					<option value="4.0">4.0이상</option>
					<option value="3.5">3.5이상</option>
					<option value="3.0">3.0이상</option>
					<option value="2.5">2.5이상</option>
					<option value="2.0">2.0이상</option>
					<option value="1.5">1.5이상</option>
					<option value="1.0">1.0이상</option>
					<option value="0.5">0.5이상</option>
				</select>
			</td>
			<td style="display:flex;">
				<button type="button" value="검색" style="margin-right:auto;width:6rem;" onclick="fn_search()">검색</button>
			</td>
		</tr>
		<tr align="center">
			<td style="display:flex;">
				<label style="margin-top:2px;">필터(제외시킬 키워드)</label> <span id="filter-toggle" style="w"onclick="openCloseFilter()">▶</span>
			</td>
			<td>
				<div id="filter-content">
					<input type="text" id = "filterQuery" size="38"  style="margin-top:2px;" placeholder="  ','단위로 키워드 입력" onKeypress="javascript:if(event.keyCode==13) {fn_search()}"/>
					<input type="checkbox" name="onlyOnline" value="가상">가상강좌만
				</div>
			</td>
		</tr>
	</table>
	<div id="showList" style="margin-top:10px;">
	</div>
	</c:otherwise>
</c:choose>
</body>
</html>