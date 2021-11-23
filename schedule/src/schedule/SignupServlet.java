package schedule;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/signUp")
public class SignupServlet extends HttpServlet {
       
   
    public SignupServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


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
		UserBean userInfo = new UserBean();

		BeanFactory factory = new XmlBeanFactory(new FileSystemResource("C:\\myJSP\\workspace\\schedule\\bean.xml"));
		UserDAO dao = (UserDAO) factory.getBean("userDAO");
		
		String command = request.getParameter("command");
		if(command.equals("reduplicateCheck")) {
			String id = request.getParameter("id");
			
			char specialChar [] = "~!@#$%^&*()+|`=\\[]{};:'\".<>/?".toCharArray();
			boolean scUse = false;
			
			userInfo.setUserId(id);
			
			
			for(int i=0; i<specialChar.length;i++) {	//특수문자 검사
				if(id.contains(""+specialChar[i]))
				{
					scUse = true;
					break;
				}
			}
			
			if(!scUse) {
				if(dao.isExisted(userInfo)) {
					out.print("n_usable");
				}else {
					out.print("usable");
				}
			}else {
				out.println("specialChar");
			}
			
		}else if(command.equals("addUser")) {
			String id = request.getParameter("id");
			String pw = request.getParameter("pwd");
			userInfo.setUserId(id);
			userInfo.setUserPw(pw);
			
			dao.addUser(userInfo);
			out.print("alert(\"가입 완료!\");");
			 
			response.sendRedirect("login.html");
		}
	}
}
