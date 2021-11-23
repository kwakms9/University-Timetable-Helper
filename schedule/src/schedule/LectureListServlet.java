package schedule;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

@WebServlet("/search")
public class LectureListServlet extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		JSONObject data = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject info;
		
		String classification [] = request.getParameter("classification").split(",");
		String queryContent = request.getParameter("queryContent");
		String score = request.getParameter("score");
		String filterQuery = request.getParameter("filterQuery");
		boolean onlyOnline = Boolean.parseBoolean(request.getParameter("onlyOnline"));
		
		HttpSession session = request.getSession();
		boolean isLogin = false;
		
		try{
			isLogin = ((Boolean)(session.getAttribute("isLogon")));
		}catch(Exception e){
			isLogin = false;
		}
		if(!isLogin) {
			out.print("fail");
			return;
		}
		
		if(!score.equals("0.0")) {	//평점선택이 0이면 모든 평점 검색
			 score = " and 평점>="+score;
		}else {
			score ="";
		}
		
		String connectQueryStr="구분='"+classification[1]+"'";
		if(!classification[1].equals("")) {
			for(int i=2; i<classification.length; i++) {//구분의 조건식을 새로 완성
				if(!classification[i].equals("")) {
					connectQueryStr+=" or 구분='"+classification[i]+"'";
				}
			}
		}System.out.println("--------------"+connectQueryStr);
		
		connectQueryStr+=")";
		if(onlyOnline) {
			connectQueryStr+=" and (강의시간='본교가상' or 강의시간='DU∼MOOC' or 강의시간='K∼MOOC')";
		}
		
		filterQuery = filterQuery.replace(" ", "");
		if(!filterQuery.equals("")){
			String keyword[] = filterQuery.split(",");

			for(int i=0; keyword.length>i; i++) {
				if(i==0) {
					connectQueryStr+=" and (";
				}
				connectQueryStr+="교과목명 NOT LIKE '%"+keyword[i]+"%'";
				if(!(keyword.length==i+1)) {
					connectQueryStr+=" and ";
				}
			}
			connectQueryStr+=")";
		}
		
		BeanFactory factory = new XmlBeanFactory(new FileSystemResource("C:\\myJSP\\workspace\\schedule\\bean.xml"));
		ScheduleDAO sdao = (ScheduleDAO) factory.getBean("scheduleDAO");
		List <ScheduleBean> scheduleList;
		scheduleList =sdao.getSchuduleInfo(connectQueryStr, queryContent, score);
		
		for(ScheduleBean schedule : scheduleList) {
			info = new JSONObject();
			info.put("div", schedule.getDiv());
			info.put("grade", schedule.getGrade());
			info.put("major", schedule.getMajor());
			info.put("lectureNumber", schedule.getLectureNumber());
			info.put("lecture", schedule.getLecture());
			info.put("credit", schedule.getCredit());
			info.put("time", schedule.getTime());
			info.put("prof", schedule.getProf());
			info.put("lectureTime", schedule.getLectureTime());
			info.put("lectureRoom", schedule.getLectureRoom());
			info.put("part", schedule.getPart());
			info.put("note", schedule.getNote());
			info.put("score", schedule.getScore());
			info.put("link", schedule.getLink());
			array.add(info);
		}
		
		data.put("list",array);
		
		String jsonInfo = data.toJSONString();
		System.out.println(jsonInfo);
		out.print(jsonInfo);
		
	}
	
}
