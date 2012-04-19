/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   TraceRecord.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-8
 */
package info.ericyue.es.activity;

import info.ericyue.es.util.HttpUtil;
import info.ericyue.es.util.MyOverLay;
import info.ericyue.es.util.MyPositionItemizedOverlay;
import info.ericyue.es.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.location.Location;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View; 
import android.widget.Button; 
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.location.Criteria;
import android.location.LocationListener;
import com.google.android.maps.MapController;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class TraceRecord  extends MapActivity{
	private String locationPrivider="";
	private int zoomLevel=0;
	private GeoPoint gp1;
	private GeoPoint gp2;
	private boolean run=false;
	private double distance=0;
	private MapView mapView;
	private MapController mapController; 
	private LocationManager locationManager;
	private Location location; 
	private Drawable begin,end,me,goods;
	private MyPositionItemizedOverlay itemizedOverlay;
	private List<Overlay> mapOverlays;
	private Bundle bundle;
	private String worker_id;
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, TraceRecord.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	@Override 
	public void onCreate(Bundle savedInstanceState){ 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.tracerecord); 
		bundle=this.getIntent().getExtras();
		mapView = (MapView)findViewById(R.id.mapView); 
		mapController = mapView.getController();

		zoomLevel = 14;//初始放大等级
		mapController.setZoom(zoomLevel); 
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);		
		mapView.setBuiltInZoomControls(true);//可以多点触摸放大  
		mapOverlays=mapView.getOverlays();
	
		/**
		 * 标记路线用
		 */
		begin=this.getResources().getDrawable(R.drawable.begin_markers);
		end=this.getResources().getDrawable(R.drawable.end_markers);
		/**
		 * 标记用户跟物流人员用
		 */
		me=this.getResources().getDrawable(R.drawable.me_markers);
		goods=this.getResources().getDrawable(R.drawable.goods_markers);
		/*取得Provider与Location */
		
		getLocationProvider();
		
		if(location!=null){
			/*取得目前的经纬度 */
			gp1=getGeoByLocation(location); 
			gp2=gp1;
			/*将MapView的中点移至目前位置 */
			refreshMapView();
			/* 设定事件的Listener */
			locationManager.requestLocationUpdates(locationPrivider,2000, 10, mLocationListener);   
		}
		else{
			new AlertDialog.Builder(TraceRecord.this).setTitle("系统信息")
			.setMessage("无法获得当前GPS信息")
			.setNegativeButton("确定",new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which){
				}
			})
			.show();
		}
		addMark(gp1,me);
		addMark(getWorkerGeoPoint(),goods);
		Toast.makeText(this,"您距离您的包裹大约"+(int)GetDistance(gp1,getWorkerGeoPoint())+"米", Toast.LENGTH_LONG).show();
	  }
	
	public void addMark(GeoPoint point,Drawable icon){
		OverlayItem overlayItem = new OverlayItem(point,"","");
		itemizedOverlay = new MyPositionItemizedOverlay(icon);
		itemizedOverlay.addOverlay(overlayItem);
		mapOverlays.add(itemizedOverlay);
		
	}

	public GeoPoint getWorkerGeoPoint(){
		worker_id=HttpUtil.queryTradeInfo("receiver_id", bundle.getString("id"), "deliverer_id");
		String loc=HttpUtil.QueryWorkerCurrentLocation(worker_id);
		String tmp[]=loc.split(",");
		int lat=(int) (Double.parseDouble(tmp[0])*1E6);
		int lng=(int) (Double.parseDouble(tmp[1])*1E6);
		return new GeoPoint(lat,lng);
	}
	
	/* MapView的Listener */
	public final LocationListener mLocationListener = 
		new LocationListener(){ 
		@Override 
		public void onLocationChanged(Location location){} 
		@Override 
		public void onProviderDisabled(String provider){} 
		@Override 
		public void onProviderEnabled(String provider){} 
		@Override 
		public void onStatusChanged(String provider,int status,Bundle extras){} 
	}; 
	/* 取得GeoPoint的method */ 
	private GeoPoint getGeoByLocation(Location location){ 
		GeoPoint gp = null; 
		try{ 
			if(location != null){ 
				double geoLatitude = location.getLatitude()*1E6; 
				double geoLongitude = location.getLongitude()*1E6; 
				gp = new GeoPoint((int) geoLatitude, (int) geoLongitude);
			} 
		} 
		catch(Exception e){ 
			e.printStackTrace(); 
		}
		return gp;
	} 
	/* 取得LocationProvider */
	public void getLocationProvider(){ 
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); 
	    criteria.setAltitudeRequired(false); 
	    criteria.setBearingRequired(false); 
	    criteria.setCostAllowed(true); 
	    criteria.setPowerRequirement(Criteria.POWER_LOW); 
	    locationPrivider = locationManager.getBestProvider(criteria, true); 
	    location = locationManager.getLastKnownLocation(locationPrivider); 
	}
	/* 重设Overlay的method */
	private void resetOverlay(){
		List<Overlay> overlays = mapView.getOverlays(); 
		overlays.clear();
	} 
	/* 更新MapView的method */
	public void refreshMapView(){ 
		mapView.displayZoomControls(true); 
		MapController myMC = mapView.getController(); 
		myMC.animateTo(gp2); 
		myMC.setZoom(zoomLevel); 
		mapView.setSatellite(false); 
	} 
	/* 取得两点间的距离的method */
	public double GetDistance(GeoPoint gp1,GeoPoint gp2){
		double Lat1r = ConvertDegreeToRadians(gp1.getLatitudeE6()/1E6);
		double Lat2r = ConvertDegreeToRadians(gp2.getLatitudeE6()/1E6);
		double Long1r= ConvertDegreeToRadians(gp1.getLongitudeE6()/1E6);
		double Long2r= ConvertDegreeToRadians(gp2.getLongitudeE6()/1E6);
		/* 地球半径(KM) */
		double R = 6371;
		double d = 
				Math.acos(Math.sin(Lat1r)*Math.sin(Lat2r)+
				Math.cos(Lat1r)*Math.cos(Lat2r)*
                Math.cos(Long2r-Long1r))*R;
		return d*1000;
	}
	private double ConvertDegreeToRadians(double degrees){
		return (Math.PI/180)*degrees;
	}
	/* format移动距离的method */
	public String format(double num){
		NumberFormat formatter = new DecimalFormat("###");
		String s=formatter.format(num);
		return s;
	}
	@Override
	protected boolean isRouteDisplayed(){
		return false;
	}
} 


