/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   SystemInfo.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-7-22
 */
package info.ericyue.es.entity;

public class SystemInfo {
	private String version;
	private String update;
	private int user_amount;
	public void setVersion(String ver){
		version=ver;
	}
	public void setUpdate(String date){
		update=date;
	}
	public void setUserAmount(int num){
		user_amount=num;
	}
	
	public String getVersion(){
		return version;
	}
	public String getUpdate(){
		return update;
	}
	public int getUserAmount(){
		return user_amount;
	}
}
