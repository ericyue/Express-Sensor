/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   UserDao.java 
 * Project Name:Express Sensor-Web
 * Create Date: 2011-7-18
 */
package info.ericyue.web.dao;
import info.ericyue.web.entity.User;
public interface UserDao {
	public abstract User login(String username,String password);
}
