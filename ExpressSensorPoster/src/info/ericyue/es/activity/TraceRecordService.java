/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   TraceRecordService.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-20
 */
package info.ericyue.es.activity;

import info.ericyue.es.util.LocationUtil;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class TraceRecordService extends Service {
	private LocationManager locationManager;
	private Handler objHandler = new Handler();
	private String worker_id;
	private LocationUtil locationUtil; 
	private Runnable mTasks = new Runnable(){
		public void run(){	
			  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
			  locationUtil=new LocationUtil(locationManager,TraceRecordService.this,worker_id);
			  Log.i("轨迹记录", "轨迹记录服务运行中...");
			  objHandler.postDelayed(mTasks, 10000);
			  locationUtil.submitGPSToServer();
		  } 
	  };
	  @Override
	  public void onStart(Intent intent, int startId){
	    super.onStart(intent, startId);
	    worker_id=intent.getExtras().getString("worker_id");
	  }
	  @Override
	  public void onCreate(){
	    /* 服务开始，调用每1秒mTasks线程 */
	    objHandler.postDelayed(mTasks, 10000);
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
