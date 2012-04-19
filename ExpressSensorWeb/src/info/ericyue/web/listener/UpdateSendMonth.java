package info.ericyue.web.listener;

import info.ericyue.web.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.mysql.jdbc.Statement;

public class UpdateSendMonth extends TimerTask {
	private ServletContext context = null;
	private static boolean isRunning=false; 
	public UpdateSendMonth(ServletContext context){
		  this.context=context;
	}
	@Override
	public void run() {
		if(!isRunning)   { 
			context.log( "开始更新每月统计"); 
			DBUtil util=new DBUtil();
			String sql="select id from statistics";
			Connection conn=util.openConnection();
			try {
				Statement pstmt = (Statement) conn.createStatement();
				ResultSet rs=pstmt.executeQuery(sql);
				while(rs.next()){
					String id=rs.getString("id");
					String update="UPDATE statistics SET month_total = month_total+week_total where id="+id;
					Statement total = (Statement) conn.createStatement();
					total.executeUpdate(update);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isRunning = false; 
			context.log( "每月统计任务执行结束 "); 
		}
		else{ 
			context.log( "每月统计的上一次任务执行还未结束 "); 
		} 
	}

}
