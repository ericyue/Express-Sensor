/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   DBUtil.java 
 * Project Name:Express Sensor-Web
 * Create Date: 2011-7-18
 */
package info.ericyue.web.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class DBUtil {	
	/*
	 * 关闭数据库连接
	 */
	public void closeConn(Connection conn){
	
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 打开数据库连接
	 */
	public Connection openConnection() {
		Properties prop = new Properties();
		String driver = null;
		String url = null;
		String username = null;
		String password = null;

		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream(
					"DBConfig.properties"));
			driver = prop.getProperty("driver");
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");			
			Class.forName(driver);			
			return DriverManager.getConnection(url,username,password);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
