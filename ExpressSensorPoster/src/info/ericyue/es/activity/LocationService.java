/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   LocationService.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-18
 */
package info.ericyue.es.activity;

import java.io.IOException;

import info.ericyue.es.util.*;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {
	private LocationManager locationManager;
	private Handler objHandler = new Handler();
	private String worker_id;
	private Bundle bundle;
	private long seconds;
	private WiFiAP wifiap=new WiFiAP(this);
	private LocationUtil locationUtil;
	private Runnable mTasks = new Runnable(){
		public void run(){			
			  try {
				locationUtil.executeListen();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  Log.i("快递端", "位置服务监听中...");
			  seconds=(long)Integer.parseInt(SettingsActivity.getUpdateTime(LocationService.this));
			  objHandler.postDelayed(mTasks, 10000);
		  } 
	  };

	  @Override
	  public void onStart(Intent intent, int startId){
	    super.onStart(intent, startId);
	    bundle=intent.getExtras();
	    worker_id=intent.getExtras().getString("worker_id");
	    locationUtil=new LocationUtil(bundle,locationManager,LocationService.this,worker_id,wifiap);
	  }
	  @Override
	  public void onCreate(){
	    /* 服务开始，调用每1秒mTasks线程 */
		  locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		  seconds=(long)Integer.parseInt(SettingsActivity.getTraceTime(this));
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
