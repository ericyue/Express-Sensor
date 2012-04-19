package info.ericyue.es.activity;

import info.ericyue.es.R;
import info.ericyue.es.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TradeStatus extends Activity {
	private ListView listView;
	private TextView infoWall;
	private ProgressBar progressbar;
	private Bundle bundle;
	private String trade_number,user_id;
	  
	private NotificationManager m_NotificationManager;
	private String[] str=new String[8];
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, TradeStatus.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.about);
        bundle=this.getIntent().getExtras();
        trade_number=bundle.getString("trade_number");
        user_id=bundle.getString("user_id");
        m_NotificationManager=(NotificationManager)this.getSystemService(NOTIFICATION_SERVICE); 
    	m_NotificationManager.cancel(0); 
    	progressbar=(ProgressBar) findViewById(R.id.ProgressBar);
		infoWall=(TextView) findViewById(R.id.infoWall);
        listView=(ListView) findViewById(R.id.aboutList);
        listView.setCacheColorHint(0);
		headInfo(true,"邮单"+trade_number+"签收完毕");
        fillItemList();
	}
	public void headInfo(boolean run,String msg){
		if(!run)
			progressbar.setVisibility(View.GONE);
		else 
			progressbar.setVisibility(View.VISIBLE);
		infoWall.setText(msg);
	}
	private void fillItemList(){
		queryDatabase();
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		String[] itemStr={"邮包编号","签收人","签收人电话","拒签"};
  		for(int i=0;i<4;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();  
		    map.put("ItemTitle", itemStr[i]);
		    map.put("ItemText", str[i]);
		    listItem.add(map); 
		}
	    SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
	    	R.layout.aboutlist,  
 	    	new String[] {"ItemTitle","ItemText"},   
 	    	new int[] {R.id.ItemTitle,R.id.ItemText}  
	    ); 
		listView.setAdapter(listItemAdapter);
		listView.setOnItemClickListener(itemListener);
	} 
	public void queryDatabase(){
		str[0]="邮包编号: "+trade_number;
		str[1]="包裹签收人: "+HttpUtil.queryTradeInfo("trade_number", trade_number, "receiver_name");
		str[2]="包裹签收人: "+HttpUtil.queryTradeInfo("trade_number", trade_number, "receiver_phone");
		str[3]="改设拒签";
	}
	private OnItemClickListener itemListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			switch(position){
			case 3:
				HttpUtil.UpdateTradeStatus(trade_number, "3");
				Toast.makeText(TradeStatus.this, "拒签修改成功！", Toast.LENGTH_LONG).show();
 				break;
			}	
		}
	};

 }