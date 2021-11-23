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

public interface UserScheduleDAO {
	
	public void addUserSchedule(UserScheduleBean bean);
	
	
	public List getScheduleList(String id);
	
	public void delUserSchedule(UserScheduleBean bean);

	public void delUserScheduleAll(String userID);
	
	public boolean isExisted(UserScheduleBean bean);
	
}
