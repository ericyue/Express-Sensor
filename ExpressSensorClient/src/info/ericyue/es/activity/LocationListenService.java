/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   LocationListenService.java 
 * Project Name:ExpressSensorClient
 * Create Date: 2011-8-20
 */
package info.ericyue.es.activity;

import java.util.Set;

import info.ericyue.es.util.LocationUtil;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class LocationListenService extends Service {
	private LocationManager locationManager;
	private String user_id,user_pwd,username;
	private long seconds;
	private Handler objHandler = new Handler();
	LocationUtil locationUtil; 
	private Runnable mTasks = new Runnable(){
		  public void run(){
			  
			  locationUtil.executeListen();
			  Log.i("用户客户端", "位置服务监听中...");
			  seconds=(long)Integer.parseInt(SettingsActivity.getUpdateTime(LocationListenService.this));
			  objHandler.postDelayed(mTasks, seconds);
		  } 
	 };
	  @Override
	  public void onStart(Intent intent, int startId){
	    super.onStart(intent, startId);
	    user_id=intent.getExtras().getString("id");
	    user_pwd=intent.getExtras().getString("password");
	    username=intent.getExtras().getString("username");
	    
	    locationUtil=new LocationUtil(user_id,username,user_pwd,locationManager,LocationListenService.this); 
	  }
	  @Override
	  public void onCreate(){
	    /* 服务开始，调用每1秒mTasks线程 */
		  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 		  
		  seconds=(long)Integer.parseInt(SettingsActivity.getUpdateTime(this));
		  objHandler.postDelayed(mTasks, seconds);
	      super.onCreate();
	  }
	  @Override
	  public IBinder onBind(Intent intent){
	    /* IBinder方法为Service建构必须重写的方法 */
	    return null;
	  }
	  @Override
	  public void onDestroy(){
	    /* 当服务结束，移除mTasks线程 */
	    objHandler.removeCallbacks(mTasks);
	    super.onDestroy();
	  }  

}
