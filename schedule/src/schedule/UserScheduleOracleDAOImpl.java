package schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class UserScheduleOracleDAOImpl implements UserScheduleDAO{
	private Connection con;
	private PreparedStatement pstmt;
	private DataSource dataFactory;

	public UserScheduleOracleDAOImpl() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addUserSchedule(UserScheduleBean bean) {
		try {
			con = dataFactory.getConnection();
			String id = bean.getUserId();
			String lectureNumber = bean.getLectureNumber();
			
			
			String query = "insert into UserSchedule";
			query += " (USERID,수강번호)";
			query += " values(?,?)";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, id);
			pstmt.setString(2, lectureNumber);
			
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public List getScheduleList(String id) {	//정보 다보기용
		List list = new ArrayList();
		try {
			con = dataFactory.getConnection();
			String query = "select * from UserSchedule where USERID=?";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String userId = rs.getString("USERID");
				String lectureName = rs.getString("수강번호");
				
				list.add(new UserScheduleBean(userId,lectureName));
				
			}
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public void delUserSchedule(UserScheduleBean bean) {
		try {
			con = dataFactory.getConnection();
			Statement stmt = con.createStatement();
			String query = "delete from UserSchedule" + " where USERID=? and 수강번호=?";
			System.out.println("prepareStatememt:" + query);
			System.out.println(bean.getUserId()+" AND "+bean.getLectureNumber());
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, bean.getUserId());
			pstmt.setString(2, bean.getLectureNumber());
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delUserScheduleAll(String userID) {
		try {
			con = dataFactory.getConnection();
			Statement stmt = con.createStatement();
			String query = "delete from UserSchedule" + " where USERID=?";
			System.out.println("prepareStatememt:" + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, userID);
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isExisted(UserScheduleBean bean) {	//user시간표 목록 확인
		boolean result = false;
		String id =bean.getUserId();
		String lectureNumber = bean.getLectureNumber();
		try {
			con = dataFactory.getConnection();
			String query = "select decode(count(*),1,'true','false') as result from UserSchedule";
			query += " where UserID=? and 수강번호=?";
			System.out.println(query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(1, lectureNumber);
			ResultSet rs = pstmt.executeQuery();
			rs.next(); //커서를 첫번째 레코드로 위치시킵니다.
			result = Boolean.parseBoolean(rs.getString("result"));
			System.out.println("result=" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
