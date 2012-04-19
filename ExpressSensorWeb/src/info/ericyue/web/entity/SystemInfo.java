/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   SystemInfo.java 
 * Project Name:ExpressSensorWeb
 * Create Date: 2011-7-22
 */
package info.ericyue.web.entity;

public class SystemInfo {
	private int user_amount;
	private String version;
	private String update;
	
	public void setVersion(String ver){
		this.version=ver;
	}
	public void setUpdate(String date){
		this.update=date;
	}
	public void setUserAmount(int num){
		this.user_amount=num;
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
