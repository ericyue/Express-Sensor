/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   WorkStatistics.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-7-17
 */
package info.ericyue.es.activity;

import java.util.ArrayList;
import java.util.HashMap;

import info.ericyue.es.R;
import info.ericyue.es.util.HttpUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class WorkStatistics extends Activity {
	private ListView listView;
	private int id;
	private String[] str=new String[6];
	private Bundle bundle;
//	private WebView webview;  
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, WorkStatistics.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.workstatistics);  
        bundle=this.getIntent().getExtras();
        listView=(ListView) findViewById(R.id.statList);
        listView.setCacheColorHint(0);

        listView.setAlwaysDrawnWithCacheEnabled(true);
//        webview=(WebView)findViewById(R.id.webview);  
        id=Integer.parseInt(getIdByUsername());
        queryDatebase();
		fillItemList();
//		drawChart();
	}
	public void queryDatebase(){
		str[0]="今日第"+HttpUtil.queryStatistics(id,"today_rank")+"名";
		str[1]="本周第"+HttpUtil.queryStatistics(id,"week_rank")+"名";
		str[2]="本月第"+HttpUtil.queryStatistics(id,"month_rank")+"名";
		str[3]="今日共投递"+HttpUtil.queryStatistics(id,"today_sent")+"件";
		str[4]="本周共投递"+HttpUtil.queryStatistics(id,"week_total")+"件";
		str[5]="本月共投递"+HttpUtil.queryStatistics(id,"month_total")+"件";
	}
//	public void drawChart(){
//		String date="4,5,6,44";
//		webview.loadUrl( "http://chart.apis.google.com/chart?cht=p3&chd=t:"+date+"&chs=250x100&chl=完成配送|剩余配送|拒绝接受|投递失败");  
//	}
	private String getIdByUsername() {
		String queryString="username="+bundle.getString("username");
    	String url=HttpUtil.BASE_URL+"servlet/QueryUser?"+queryString;
    	String id=HttpUtil.queryStringForPost(url);
    	return id;
	}
	private void fillItemList(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		String[] itemStr={"今日排名","本周排名","本月排名","今日投递总数","本周投递总数","本月投递总数"};
  		for(int i=0;i<6;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();  
		    map.put("ItemTitle", itemStr[i]);
		    map.put("ItemText", str[i]);
		    listItem.add(map); 
		}
	    SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
	    	R.layout.worklist,  
 	    	new String[] {"ItemTitle","ItemText"},   
 	    	new int[] {R.id.ItemTitle,R.id.ItemText}  
	    ); 
		listView.setAdapter(listItemAdapter);
	} 
	
}