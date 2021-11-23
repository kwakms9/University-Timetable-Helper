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



public interface UserDAO {
	
	
	public UserBean getUserInfo(String userid);
	
	public void addUser(UserBean bean);
	
	public boolean updatePwd(UserBean bean);
	
	public void delUser(String userid);

	public boolean isExisted(UserBean bean);
	
	public boolean checkInfo(UserBean bean);
	
	
}
