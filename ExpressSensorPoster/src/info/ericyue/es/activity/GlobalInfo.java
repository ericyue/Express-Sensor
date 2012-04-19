/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   GlobalInfo.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-7-17
 */
package info.ericyue.es.activity;
import info.ericyue.es.util.HttpUtil;
import info.ericyue.es.R;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;

public class GlobalInfo extends Activity {
	/** Called when the activity is first created. */
	private TextView infoWall;
	private LocationManager locationManager;
	private double lat;
	private double lng;
	private int id;
	private int updateSecond=5000;//刷新信息线程的间隔
	private Bundle bundle;
	private final static String M24 = "kk:mm";
    private String timeStr;
	private ListView listView;
	private String[] str=new String[9];
	private Handler objHandler = new Handler();
    private Runnable updateInfo= new Runnable(){
		@Override
		public void run() {
			if(SettingsActivity.getAutoUpdate(GlobalInfo.this)){
				refreshInfo();
			}
			objHandler.postDelayed(updateInfo, updateSecond);
		}
    };
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, GlobalInfo.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState );
        this.setContentView(R.layout.globalinfo);  
        bundle=this.getIntent().getExtras();       
        listView=(ListView) findViewById(R.id.globalList);  
        listView.setCacheColorHint(0);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);  
        //从XML中读取配置信息
        updateSecond=Integer.parseInt(SettingsActivity.getUpdateTime(GlobalInfo.this));
        
        objHandler.postDelayed(updateInfo, updateSecond);
        timeStr ="最近一次更新数据: "+(String) DateFormat.format(M24,System.currentTimeMillis()); 
        infoWall=(TextView) findViewById(R.id.infoWall);
        locate();
        
        getAddressbyGeoPoint();
        str[0]=getIdByUsername(bundle.getString("username"));
        queryDatabase();
        
        fillItemList();
        headInfo(timeStr);
	}
	public void headInfo(String msg){
		infoWall.setText(msg);
	}
	private void fillItemList(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		String[] itemStr={"当前用户","当前坐标","解析地址","今日总委派数","今日完成派送",
						  "今日等待派送","今日拒绝签收","今日投递失败(无人签收)","今日货到付款总额"};
		
  		for(int i=0;i<9;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();  
		    map.put("ItemTitle", itemStr[i]);
		    map.put("ItemText", str[i]);
		    listItem.add(map); 
		}
	    SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
	    	R.layout.globallist,  
 	    	new String[] {"ItemTitle","ItemText"},   
 	    	new int[] {R.id.ItemTitle,R.id.ItemText}  
	    ); 
		listView.setAdapter(listItemAdapter);
		listView.setOnItemClickListener(itemListener);
	}     
	private void queryDatabase() {
        str[3]="共委派邮件 "+HttpUtil.queryStatistics(id,"today_assign")+" 件";
		str[4]="完成配送共 "+HttpUtil.queryStatistics(id,"today_sent")+" 件";
        str[5]="等待配送共 "+HttpUtil.queryStatistics(id,"for_send")+" 件";
        str[6]="拒绝签收共 "+HttpUtil.queryStatistics(id,"today_refuse")+" 件";
        str[7]="投递失败共 "+HttpUtil.queryStatistics(id,"today_fail")+" 件";
        str[8]="货到付款总额 "+HttpUtil.queryStatistics(id,"today_cash")+" 元";
	}
	private String getIdByUsername(String username) {
		String queryString="username="+username;
    	String url=HttpUtil.BASE_URL+"servlet/QueryUser?"+queryString;
    	id=Integer.parseInt(HttpUtil.queryStringForPost(url));
    	bundle.putInt("id",id);
    	return "用户: "+username+" ( ID:"+id+" )";
	}
	public void refreshInfo() {
		timeStr = "最近一次更新数据: "+ (String) DateFormat.format(M24,System.currentTimeMillis()); 
        headInfo(timeStr);
        locate();
        getAddressbyGeoPoint();
        queryDatabase();
        getAddressbyGeoPoint();
        fillItemList();
	}
	
	 private void updateWithNewLocation(Location location) {   
         String latLongString;   
         if (location != null) {   
         double lat = location.getLatitude();   
         double lng = location.getLongitude();   
         latLongString = "纬度:" + lat + "\n经度:" + lng;   
         } else {   
         latLongString = "无法获取地理信息";   
         }   
         str[1]=(latLongString);   
 }   
	private final LocationListener locationListener = new LocationListener() {   
        public void onLocationChanged(Location location) {   
        updateWithNewLocation(location);   
        }   
        public void onProviderDisabled(String provider){   
        updateWithNewLocation(null);   
        }   
        public void onProviderEnabled(String provider){ }   
        public void onStatusChanged(String provider, int status,   
        Bundle extras){ }   
	}; 
	public void locate(){	
		 	Criteria criteria = new Criteria();   
	        criteria.setAccuracy(Criteria.ACCURACY_FINE);   
	        criteria.setAltitudeRequired(false);   
	        criteria.setBearingRequired(false);   
	        criteria.setCostAllowed(true);   
	        criteria.setPowerRequirement(Criteria.POWER_LOW);   
	        String provider = locationManager.getBestProvider(criteria, true);   
	           
	        Location location = locationManager.getLastKnownLocation(provider);   
	        updateWithNewLocation(location);   
	        locationManager.requestLocationUpdates(provider, 2000, 10,   
	                        locationListener);   
	
	}
	public void getAddressbyGeoPoint(){
	    try
	    {
	    	str[2]="暂时无法解析地址\n(可能是天气、建筑物遮挡等原因)";
	        Geocoder gc = new Geocoder(GlobalInfo.this, Locale.getDefault());
	        /* 取出地理坐标经纬度 */
	        double geoLatitude = (int)lat/1E6;
	        double geoLongitude = (int)lng/1E6;
	        /* 自经纬度取得地址（可能有多行地址） */
	        List<Address> lstAddress = gc.getFromLocation(geoLatitude, geoLongitude, 1);
	        /* 判断地址是否为多行 */
	        String result=null;
	        if (lstAddress.size() > 0){
	          StringBuilder sb = new StringBuilder();
	          Address adsLocation = lstAddress.get(0);
	          for (int i = 0; i < adsLocation.getMaxAddressLineIndex(); i++){
	            sb.append(adsLocation.getAddressLine(i)).append("\n");
	          }
	          sb.append(adsLocation.getLocality()).append("\n");
	          sb.append(adsLocation.getPostalCode()).append("\n");
	          sb.append(adsLocation.getCountryName());
	          result=sb.toString();
	        }	        
	        if(result==null||result.length()==0){
	        	str[2]="暂时无法解析地址\n(可能是天气、建筑物遮挡等原因)";
	        }else{
	        	str[2]=result;
	        }
	    }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }
	  }
	private OnItemClickListener itemListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long ids) {
			switch(position){
			case 1:
			case 2:
				TraceRecord.launch(GlobalInfo.this, bundle);
				break;
			case 6:
				break;
			default:
				break;
			}	
		}
	};
}