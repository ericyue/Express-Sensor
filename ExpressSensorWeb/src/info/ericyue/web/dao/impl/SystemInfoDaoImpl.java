/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   SystemInfoImpl.java 
 * Project Name:ExpressSensorWeb
 * Create Date: 2011-7-22
 */
package info.ericyue.web.dao.impl;
import java.sql.*;
import info.ericyue.web.dao.SystemInfoDao;
import info.ericyue.web.util.DBUtil;

public class SystemInfoDaoImpl implements SystemInfoDao{
	public String getInfo(int type,int query){
		String sql="select * from system where type=?";
		//type=0 表示送货员的客户端
		//type=1 表示普通用户的客户端
		
		//query=0表示version
		//query=1表示user_amount
		//query=2表示update_date
		DBUtil util=new DBUtil();
		String tmp="";
		Connection conn=util.openConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);			
			switch(query){
			case 0:
				tmp="version";
				break;
			case 1:
				tmp="user_amount";
				break;
			case 2:
				tmp="update_date";
				break;
			}
			pstmt.setInt(1, type);
			ResultSet rs=pstmt.executeQuery();			
		if(rs.next()){
			return rs.getString(tmp);
		}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
