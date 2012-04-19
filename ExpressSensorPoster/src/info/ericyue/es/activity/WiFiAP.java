/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   WifiHotSpot.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-13
 */
package info.ericyue.es.activity;

import java.lang.reflect.Method;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

public class WiFiAP{
	private WifiManager wifi;
	private Context context;
	/**
	 * WIFI-AP状态码
	 */
    private static final int WIFI_AP_STATE_UNKNOWN = -1;
    private static final int WIFI_AP_STATE_DISABLING = 0;
    private static final int WIFI_AP_STATE_DISABLED = 1;
    private static final int WIFI_AP_STATE_ENABLING = 2;
    private static final int WIFI_AP_STATE_ENABLED = 3;
    private static final int WIFI_AP_STATE_FAILED = 4;
    private final String[] WIFI_STATE_TEXTSTATE = new String[] {
            "DISABLING","DISABLED","ENABLING","ENABLED","FAILED"
        };
    public WiFiAP(Context context){
    	this.context=context;
    }
    public void StartWiFiAP(){
    	wifi=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	boolean wifiApIsOn =( getWifiAPState()==WIFI_AP_STATE_ENABLED || getWifiAPState()==WIFI_AP_STATE_ENABLING);
        new SetWifiAPTask(!wifiApIsOn,false).execute();
    }

    /**
     * 从其他Activity启动
     * @param c
     * @param bundle 传入的数据
     */
    public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, WiFiAP.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}

    public void closeWiFiAP() {
        boolean wifiApIsOn = getWifiAPState()==WIFI_AP_STATE_ENABLED || getWifiAPState()==WIFI_AP_STATE_ENABLING;
        if (wifiApIsOn) {
            new SetWifiAPTask(false,true).execute();
        } else {
        	/**
        	 * 停止服务
        	 */
        }
    }
    /**
     * Endable/disable wifi
     * @param enabled
     * @return WifiAP state
     */
    public int setWifiApEnabled(boolean enabled) {
        if (enabled && null!=wifi.getConnectionInfo()) {
            wifi.setWifiEnabled(false);
            try {Thread.sleep(1500);} catch (Exception e) {}
        }
        int state = WIFI_AP_STATE_UNKNOWN;
        try {
            wifi.setWifiEnabled(false);
            Method method1 = wifi.getClass().getMethod("setWifiApEnabled",WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();
            netConfig.SSID = "Express Sensor";
            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);    
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            netConfig.preSharedKey = "11111111";
            method1.invoke(wifi, netConfig, enabled);
            Method method2 = wifi.getClass().getMethod("getWifiApState");
            state = (Integer) method2.invoke(wifi);
        } catch (Exception e) {
           Log.e("WiFiService", e.getMessage());
        }

        if (!enabled) {
            int loopMax = 10;
            while (loopMax>0 && (getWifiAPState()==WIFI_AP_STATE_DISABLING
                    || getWifiAPState()==WIFI_AP_STATE_ENABLED
                    || getWifiAPState()==WIFI_AP_STATE_FAILED)) {
                try {Thread.sleep(500);loopMax--;} catch (Exception e) {}
            }
            wifi.setWifiEnabled(true);
        } else if (enabled) {
            int loopMax = 10;
            while (loopMax>0 && (getWifiAPState()==WIFI_AP_STATE_ENABLING
                    || getWifiAPState()==WIFI_AP_STATE_DISABLED
                    || getWifiAPState()==WIFI_AP_STATE_FAILED)) {
                try {Thread.sleep(500);loopMax--;} catch (Exception e) {}
            }
        }
        return state;
    }
    private int getWifiAPState() {
        int state = WIFI_AP_STATE_UNKNOWN;
        try {
            Method method2 = wifi.getClass().getMethod("getWifiApState");
            state = (Integer) method2.invoke(wifi);
        } catch (Exception e) {}
        Log.d("Wifi", "getWifiAPState.state " + (-1==state?"UNKNOWN":WIFI_STATE_TEXTSTATE[state]));
        return state;
    }
    private void updateStatusDisplay() {
        if (getWifiAPState()==WIFI_AP_STATE_ENABLED || getWifiAPState()==WIFI_AP_STATE_ENABLING) {
            Log.i("WifiSevice","Turn off");       
        } else {
            Log.i("WifiSevice","Turn on");
        }
    }
    /**
     * 开启WIFIAP线程
     * @author Moonlight
     */
    class SetWifiAPTask extends AsyncTask<Void, Void, Void> {
        boolean mMode;
        boolean mFinish;
        public SetWifiAPTask(boolean mode, boolean finish) {
            mMode = mode;
            mFinish = finish;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        	Log.i("WIFI管理","WIFI启动中...");
        }
        @Override
        protected Void doInBackground(Void... params) {
            setWifiApEnabled(mMode);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
            	Log.i("WIFI管理","WIFI启动完毕");
            } catch (IllegalArgumentException e) {
            	e.printStackTrace();
            };
            updateStatusDisplay();
            if (mFinish) ;
            	/**
            	 * 执行完毕，关闭app
            	 */
        }
    }
}