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
import java.util.List;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.location.Location;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;
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
	private double distance=0;
	private MapView mapView;
	private MapController mapController; 
	private LocationManager locationManager;
	private Drawable begin,end,user;
	private MyPositionItemizedOverlay itemizedOverlay;
	private List<Overlay> mapOverlays;
	private Bundle bundle;
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
		zoomLevel = 10;//初始放大等级
		mapController.setZoom(zoomLevel); 
		locationManager = (LocationManager)
        getSystemService(Context.LOCATION_SERVICE);		
		mapView.setBuiltInZoomControls(true);//可以多点触摸放大  
		mapOverlays=mapView.getOverlays();
		begin=this.getResources().getDrawable(R.drawable.begin_markers);
		end=this.getResources().getDrawable(R.drawable.end_markers);
		user=this.getResources().getDrawable(R.drawable.user_markers);
		
		
		GetUserGeoPointAndDraw();
		updateMapView();
	  }
	public void GetUserGeoPointAndDraw(){
		String loc=HttpUtil.queryCurrentLocation();
		Log.i("TRacerecord", loc);
		if(loc==null||loc.length()==0||loc.equals(" "))
			return;
		String[] locations=loc.split("#");
		for(int i=0;i!=locations.length;++i){
			String[] tmp=locations[i].split(",");
			addMark(new GeoPoint((int)(Double.parseDouble(tmp[1])*1E6),(int)(Double.parseDouble(tmp[2])*1E6)),user);
		}
		
	}
	public void addMark(GeoPoint point,Drawable icon){
		OverlayItem overlayItem = new OverlayItem(point,"","");
		itemizedOverlay = new MyPositionItemizedOverlay(icon);
		itemizedOverlay.addOverlay(overlayItem);
		mapOverlays.add(itemizedOverlay);
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
	public void getLocationPrivider(){ 
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); 
	    criteria.setAltitudeRequired(false); 
	    criteria.setBearingRequired(false); 
	    criteria.setCostAllowed(true); 
	    criteria.setPowerRequirement(Criteria.POWER_LOW); 
	    locationPrivider = locationManager.getBestProvider(criteria, true); 
	    locationManager.getLastKnownLocation(locationPrivider); 
	}
	private void setStartPoint(){  
		addMark(gp1,begin);
	}
	private void setRoute(){  
		int mode=2;
		MyOverLay mOverlay = new MyOverLay(gp1,gp2,mode); 
		List<Overlay> overlays = mapView.getOverlays(); 
		overlays.add(mOverlay);
	}
	private void setEndPoint(){  
		addMark(gp2,end);
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
	public void updateMapView(){
		int mode=2;
//		resetOverlay();
		distance=0;
		String un=HttpUtil.GetWorkerTrace(bundle.getString("worker_id"));
		if(un==null){
			return;
		}
		String[] gpsline=un.split("#");
		/**
		 * 画起点
		 */
		String []tmp=gpsline[0].split(",");
		gp2=new GeoPoint((int)(Double.parseDouble(tmp[1])*1E6),(int)(Double.parseDouble(tmp[2])*1E6));
		gp1=gp2;
		addMark(gp2,begin);
		refreshMapView();
		/**
		 * 画路线
		 */
		for(int i=1;i<gpsline.length-1;++i){
			
			tmp=gpsline[i].split(",");
			if(gpsline[i].equals("")||gpsline[i].equals(" ")||gpsline.length==0){
				break;
			}
			gp2=new GeoPoint((int)(Double.parseDouble(tmp[1])*1E6),(int)(Double.parseDouble(tmp[2])*1E6));
			MyOverLay mOverlay = new MyOverLay(gp1,gp2,mode); 
			List<Overlay> overlays = mapView.getOverlays(); 
			overlays.add(mOverlay);
			refreshMapView();
			distance+=GetDistance(gp1,gp2);
			gp1=gp2;
			
		}	
		/**
		 * 画终点
		 */
		tmp=gpsline[gpsline.length-1].split(",");
		gp2=new GeoPoint((int)(Double.parseDouble(tmp[1])*1E6),(int)(Double.parseDouble(tmp[2])*1E6));
		addMark(gp2,end);
		refreshMapView();
		Toast.makeText(this, "您目前移动了大约:"+(int)distance+"米", Toast.LENGTH_LONG).show();
	}
} 


