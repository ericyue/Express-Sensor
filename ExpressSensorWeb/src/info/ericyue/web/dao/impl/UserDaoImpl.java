/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   UserDaoImpl.java 
 * Project Name:Express Sensor-Web
 * Create Date: 2011-7-18
 */
package info.ericyue.web.dao.impl;
import java.sql.*;


import info.ericyue.web.dao.UserDao;
import info.ericyue.web.entity.*;
import info.ericyue.web.util.DBUtil;
public class UserDaoImpl implements UserDao {
	public User login(String username, String password) {
		String sql = "select id,username,password from user where username=? and password=? ";
		DBUtil util = new DBUtil();
		Connection conn = util.openConnection();
		try {
			if(conn!=null){
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt(1);
				User u = new User();
				u.setId(id);
				u.setPassword(password);
				u.setUsername(username);
				return u;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			util.closeConn(conn);
		}	
			return null;
		}
	
}
