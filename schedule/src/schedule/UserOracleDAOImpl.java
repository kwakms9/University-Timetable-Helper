package schedule;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public class UserOracleDAOImpl implements UserDAO{
	private Connection con;
	private PreparedStatement pstmt;
	private DataSource dataFactory;

	public UserOracleDAOImpl() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public UserBean getUserInfo(String userid) {	//user정보 다보기용
		UserBean bean = new UserBean();
		try {
			con = dataFactory.getConnection();
			String query = "select * from UserInfo where ID=?";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, userid);
			ResultSet rs = pstmt.executeQuery();
			rs.next(); 
			
			String id = rs.getString("ID");
			String pw = rs.getString("pw");
			
			bean.setUserId(id);
			bean.setUserPw(pw);
			
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public void addUser(UserBean bean) {
		try {
			con = dataFactory.getConnection();
			String id = bean.getUserId();
			String pw = bean.getUserPw();
			
			
			String query = "insert into UserInfo";
			query += " (ID,PW)";
			query += " values(?,?)";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean updatePwd(UserBean bean) {
		try {
			con = dataFactory.getConnection();
			Statement stmt = con.createStatement();
			String query = "update UserInfo set pw =? where ID=?";
			System.out.println("prepareStatememt:" + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bean.getUserPw());
			pstmt.setString(2, bean.getUserId());
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {	
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void delUser(String userid) {
		try {
			con = dataFactory.getConnection();
			Statement stmt = con.createStatement();
			String query = "delete from UserInfo" + " where ID=?";
			System.out.println("prepareStatememt:" + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, userid);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isExisted(UserBean bean) {	//user id 확인
		boolean result = false;
		String id =bean.getUserId();
		try {
			con = dataFactory.getConnection();
			String query = "select decode(count(*),1,'true','false') as result from UserInfo";
			query += " where ID=?";
			System.out.println(query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next(); //커서를 첫번째 레코드로 위치시킵니다.
			result = Boolean.parseBoolean(rs.getString("result"));
			System.out.println("result=" + result);
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean checkInfo(UserBean bean) {	//id pw 검사
		boolean result = false;
		String id =bean.getUserId();
		String pw = bean.getUserPw();
		try {
			con = dataFactory.getConnection();
			String query = "select decode(count(*),1,'true','false') as result from UserInfo";
			query += " where ID=? and PW=?";
			System.out.println(query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			ResultSet rs = pstmt.executeQuery();
			rs.next(); //커서를 첫번째 레코드로 위치시킵니다.
			result = Boolean.parseBoolean(rs.getString("result"));
			System.out.println("result=" + result);
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
