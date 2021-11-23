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

@WebServlet("/alterSchedule")
public class UserScheduleController extends HttpServlet{

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
		HttpSession session =  request.getSession(false);
		
		String data=""; //결과 표시
		boolean isLogin =false;
		
		try{
			isLogin = ((Boolean)(session.getAttribute("isLogon")));
		}catch(Exception e){
			isLogin = false;
		}
		
		if(!isLogin) {
			data="fail";
			out.print(data);
			return;
		}
		
		String lectureNumber = request.getParameter("lectureNumber");
		String command = request.getParameter("command");
		UserBean bean = (UserBean)session.getAttribute("ub");
		String userId = bean.getUserId();
		BeanFactory factory = new XmlBeanFactory(new FileSystemResource("C:\\myJSP\\workspace\\schedule\\bean.xml"));
		UserScheduleDAO dao = (UserScheduleDAO) factory.getBean("userScheduleDAO");
		
		if(!userId.equals(null)) {
			UserScheduleBean sBean = new UserScheduleBean(userId,lectureNumber);
			if(command.equals("add")) {
				dao.addUserSchedule(sBean);
				data="저장되었습니다.";
			}else if(command.equals("delete")){
				dao.delUserSchedule(sBean);
				data="success";
			}
		}else {
			data="잘못된 접근!";
		}
		
		System.out.println(data);
		out.print(data);

	}
}
