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



public class ScheduleOracleDAOImpl implements ScheduleDAO{
	private Connection con;
	private PreparedStatement pstmt;
	private DataSource dataFactory;

	public ScheduleOracleDAOImpl() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public List getSchuduleInfo(String classfication, String name, String score) {	//강의시간 조건 검색
		List list = new ArrayList();
		try {
			con = dataFactory.getConnection();
			String query = "select * from TOTALLIST where (";
			query+=classfication+" and (수강번호 LIKE ? or 교과목명 LIKE ? or 담당교수 LIKE ?)"+score;
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, name);
			pstmt.setString(3, name);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) { 
				ScheduleBean bean = new ScheduleBean();
				bean.setDiv(rs.getString("구분"));
				bean.setGrade(rs.getString("학년"));
				bean.setMajor(rs.getString("수강학과"));
				bean.setLectureNumber(rs.getString("수강번호"));
				bean.setLecture(rs.getString("교과목명"));
				bean.setCredit(rs.getString("학점"));
				bean.setTime(rs.getString("시간"));
				bean.setProf(rs.getString("담당교수"));
				bean.setLectureTime(rs.getString("강의시간"));
				bean.setLectureRoom(rs.getString("강의실"));
				bean.setPart(rs.getString("소영역"));
				bean.setNote(rs.getString("비고"));
				bean.setScore(rs.getString("평점"));
				bean.setLink(rs.getString("후기"));
				System.out.println(bean.getLectureNumber());
				list.add(bean);
			}	
		   
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List getSchuduleInfo(String numbers) {	//강의번호로 검색
		List list = new ArrayList();
		try {
			con = dataFactory.getConnection();
			String query = "select * from TOTALLIST where ";
			query+=numbers;
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) { 
				ScheduleBean bean = new ScheduleBean();
				bean.setDiv(rs.getString("구분"));
				bean.setGrade(rs.getString("학년"));
				bean.setMajor(rs.getString("수강학과"));
				bean.setLectureNumber(rs.getString("수강번호"));
				bean.setLecture(rs.getString("교과목명"));
				bean.setCredit(rs.getString("학점"));
				bean.setTime(rs.getString("시간"));
				bean.setProf(rs.getString("담당교수"));
				bean.setLectureTime(rs.getString("강의시간"));
				bean.setLectureRoom(rs.getString("강의실"));
				bean.setPart(rs.getString("소영역"));
				bean.setNote(rs.getString("비고"));
				bean.setScore(rs.getString("평점"));
				bean.setLink(rs.getString("후기"));
				System.out.println(bean.getLectureNumber());
				list.add(bean);
			}	
		   
			rs.close();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
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
	
}
