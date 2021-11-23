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

	
	public List getSchuduleInfo(String classfication, String name, String score) {	//���ǽð� ���� �˻�
		List list = new ArrayList();
		try {
			con = dataFactory.getConnection();
			String query = "select * from TOTALLIST where (";
			query+=classfication+" and (������ȣ LIKE ? or ������� LIKE ? or ��米�� LIKE ?)"+score;
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, name);
			pstmt.setString(2, name);
			pstmt.setString(3, name);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) { 
				ScheduleBean bean = new ScheduleBean();
				bean.setDiv(rs.getString("����"));
				bean.setGrade(rs.getString("�г�"));
				bean.setMajor(rs.getString("�����а�"));
				bean.setLectureNumber(rs.getString("������ȣ"));
				bean.setLecture(rs.getString("�������"));
				bean.setCredit(rs.getString("����"));
				bean.setTime(rs.getString("�ð�"));
				bean.setProf(rs.getString("��米��"));
				bean.setLectureTime(rs.getString("���ǽð�"));
				bean.setLectureRoom(rs.getString("���ǽ�"));
				bean.setPart(rs.getString("�ҿ���"));
				bean.setNote(rs.getString("���"));
				bean.setScore(rs.getString("����"));
				bean.setLink(rs.getString("�ı�"));
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
	
	public List getSchuduleInfo(String numbers) {	//���ǹ�ȣ�� �˻�
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
				bean.setDiv(rs.getString("����"));
				bean.setGrade(rs.getString("�г�"));
				bean.setMajor(rs.getString("�����а�"));
				bean.setLectureNumber(rs.getString("������ȣ"));
				bean.setLecture(rs.getString("�������"));
				bean.setCredit(rs.getString("����"));
				bean.setTime(rs.getString("�ð�"));
				bean.setProf(rs.getString("��米��"));
				bean.setLectureTime(rs.getString("���ǽð�"));
				bean.setLectureRoom(rs.getString("���ǽ�"));
				bean.setPart(rs.getString("�ҿ���"));
				bean.setNote(rs.getString("���"));
				bean.setScore(rs.getString("����"));
				bean.setLink(rs.getString("�ı�"));
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
