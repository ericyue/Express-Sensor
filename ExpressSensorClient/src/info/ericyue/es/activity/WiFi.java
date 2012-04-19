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
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

public class WiFi{
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
    /**
     * APNAME中计划加入用户ID
     * 
     */
    private static String ESCLIENTAPNAME ="\"ESCLIENTAP\"";
    private static String ESCLIENTAPPASSWORD ="11111111";
    private static String ESAPNAME ="\"MOONLIGHT\"";
    private static String ESAPPASSWORD ="\"12345678\"";
    private final String[] WIFI_STATE_TEXTSTATE = new String[] {
        "DISABLING","DISABLED","ENABLING","ENABLED","FAILED"
    };
    private WifiManager wifi;
	public WiFi(Context context){
		this.context=context;
	}
	public void connectWiFi(){
		wifi=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		connectToESMobile();
	}
 
//        getWindow().addFlags(
//        		 WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD//解除非加锁的锁屏状态
//                |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON// 当此窗口为用户可见时，保持设备常开，并保持亮度不变。
//                |WindowManager.LayoutParams.FLAG_DIM_BEHIND//亮度变暗
//        );

    /**
     * 
     * @param v
     */
    public void toggleWifi(View v) {
        boolean wifiApIsOn = getWifiAPState()==WIFI_AP_STATE_ENABLED || getWifiAPState()==WIFI_AP_STATE_ENABLING;
        new SetWifiAPTask(!wifiApIsOn,false).execute();
    }
    public void close(View v) {
        boolean wifiApIsOn = getWifiAPState()==WIFI_AP_STATE_ENABLED || getWifiAPState()==WIFI_AP_STATE_ENABLING;
        if (wifiApIsOn) {
            new SetWifiAPTask(false,true).execute();
        } else {
            
        }
    }
    /**
     * Endable/disable wifi
     * @param enabled
     * @return WifiAP state
     */
    private int setWifiApEnabled(boolean enabled) {
        if (enabled && null!=wifi.getConnectionInfo()) {
            wifi.setWifiEnabled(false);
            try {Thread.sleep(1500);} catch (Exception e) {}
        }
        int state = WIFI_AP_STATE_UNKNOWN;
        try {
            wifi.setWifiEnabled(false);
            Method method1 = wifi.getClass().getMethod("setWifiApEnabled",WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();
            netConfig.SSID =ESCLIENTAPNAME;
            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);    
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            netConfig.preSharedKey = ESCLIENTAPPASSWORD ;
            method1.invoke(wifi, netConfig, enabled);
            Method method2 = wifi.getClass().getMethod("getWifiApState");
            state = (Integer) method2.invoke(wifi);
        } catch (Exception e) {
           Log.e("WiFi监听服务", e.getMessage());
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
            Log.i("WIFI","Turn off");       
        } else {
            Log.i("WiFi","Turn on");
        }
    }
    public boolean connectToESMobile(){
    	WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    	WifiConfiguration wc = new WifiConfiguration();
    	wc.SSID = ESAPNAME;
    	wc.preSharedKey = ESAPPASSWORD ;
    	wc.hiddenSSID = true;
    	wc.status = WifiConfiguration.Status.ENABLED;
    	wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
    	wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
    	wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
    	wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
    	wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
    	wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
    	int res = wifi.addNetwork(wc);
    	return wifi.enableNetwork(res, true);
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
        ProgressDialog d = new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            d.setMessage("正在"+(mMode?"打开":"关闭") +"WiFi无线接入点"+"...");
            d.setTitle("请稍后...");
            d.show();
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
            	d.dismiss();
            } catch (IllegalArgumentException e) {
            	e.printStackTrace();
            };
            updateStatusDisplay();
            if (mFinish) ;
        }
    }
}