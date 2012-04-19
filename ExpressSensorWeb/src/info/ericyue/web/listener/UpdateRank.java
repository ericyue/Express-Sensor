package info.ericyue.web.listener;
import info.ericyue.web.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import com.mysql.jdbc.Statement;

public class UpdateRank extends TimerTask {
	private ServletContext context = null;
	private static boolean isRunning=false; 
	public UpdateRank(ServletContext context){
		  this.context=context;
	}
	@Override
	public void run() {
		if(!isRunning)   { 
			context.log( "开始计算排名 "); 
			DBUtil util=new DBUtil();
			Connection conn=util.openConnection();
			String id = "",rank = "";
			String[] item={"today_sent","week_total","month_total"};
			String[] torank={"today_rank","week_rank","month_rank"};
			String rank1="SET @rank=0";
			for(int i=0;i!=3;++i){	
				String rank2="SELECT @rank:=@rank+1 AS rank,id  FROM statistics  ORDER BY "+item[i]+" DESC";			
				try {
					Statement psrank =(Statement) conn.createStatement();			
					psrank.executeQuery(rank1);
					ResultSet rs=psrank.executeQuery(rank2);		
					while(rs.next()){
						id=rs.getString("id");
						rank=rs.getString("rank");
						String update="UPDATE statistics SET "+torank[i]+" = "+rank+" where id="+id;
						PreparedStatement psupdate = conn.prepareStatement(update);
						psupdate.executeUpdate();
						}
				}			
				 catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			isRunning = false; 
			context.log( "开始计算排名任务执行结束 "); 
		}
		else{ 
			context.log( "开始计算排名的上一次任务执行还未结束 "); 
		} 
	}

}
