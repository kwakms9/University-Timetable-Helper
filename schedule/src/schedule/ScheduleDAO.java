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



public interface ScheduleDAO {
	
	public List getSchuduleInfo(String classfication, String name, String score);
	
	public List getSchuduleInfo(String numbers);
	
	public boolean updatePwd(UserBean bean);
	
	public void delUser(String userid);
	
}
