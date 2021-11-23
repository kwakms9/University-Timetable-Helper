package schedule;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Servlet implementation class UserDataServlet
 */
@WebServlet("/UserDataServlet")
public class UserDataServlet extends HttpServlet {
       
    public UserDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doHandle(request, response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		boolean isLogin = false;
		
		try{
			isLogin = ((Boolean)(session.getAttribute("isLogon")));
		}catch(Exception e){
			isLogin = false;
		}
		if(!isLogin) {
			out.print("not_login");
			return;
		}
		
		BeanFactory factory = new XmlBeanFactory(new FileSystemResource("C:\\myJSP\\workspace\\schedule\\bean.xml"));
		UserDAO dao = (UserDAO) factory.getBean("userDAO");
		UserBean bean = new UserBean();
		String command = request.getParameter("command");
		String userID = request.getParameter("userId");
		String pw = request.getParameter("pw");
		
		bean.setUserId(userID);
		bean.setUserPw(pw);
		
		if(command.equals("changePW")) {
			String npw = request.getParameter("npw");
			if(dao.checkInfo(bean)) {	//check previous PW
				bean.setUserPw(npw);
				dao.updatePwd(bean);	//update new PW
				out.print("success");
			}else {
				out.print("not_match");
			}
			
		}else if(command.equals("deleteUser")) {
			if(dao.checkInfo(bean)) {
				UserScheduleDAO usdao = (UserScheduleDAO) new XmlBeanFactory(
						new FileSystemResource("C:\\myJSP\\workspace\\schedule\\bean.xml")).getBean("userScheduleDAO");
				usdao.delUserScheduleAll(userID);
				dao.delUser(userID);
				out.print("success");
			}else {
				out.print("not_match");
			}
			
		}

	}
}
