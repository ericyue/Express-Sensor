/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * Project Name:ExpressSensor
 * Create Date: 2011-7-17
 */
package info.ericyue.es.activity;

import info.ericyue.es.R;
import info.ericyue.es.util.HttpUtil;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ExpressDelivery extends Activity{
	private ListView listView;
	private Bundle bundle;
	private TextView infoWall;
	private ProgressBar progressbar;
	private String id;
	private NotificationManager m_NotificationManager;  
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//不显示标题栏
		bundle=this.getIntent().getExtras();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forsend);
		progressbar=(ProgressBar) findViewById(R.id.ProgressBar);
		infoWall=(TextView) findViewById(R.id.infoWall);
 		headInfo(false,"快递投递详情");
		listView = (ListView)findViewById(R.id.forsendlist);	
        listView.setCacheColorHint(0);

		id=getIdByUsername(bundle.getString("username"));
		m_NotificationManager=(NotificationManager)this.getSystemService(NOTIFICATION_SERVICE); 
		m_NotificationManager.cancel(0); 
		fillItemList();
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {}
	/**
	 * MenuInflater 用来解析定义在menu目录下的菜单布局文件
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home, menu);
		return super.onCreateOptionsMenu(menu);
	}
	/**
	 * 按下菜单后的响应
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.about_menu_item:
			About.launch(ExpressDelivery.this,bundle);
			break;
		case R.id.settings_menu_item:
			SettingsActivity.launch(ExpressDelivery.this,bundle);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, ExpressDelivery.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	private String getIdByUsername(String username) {
		String queryString="username="+username;
    	String url=HttpUtil.BASE_URL+"servlet/QueryUser?"+queryString;
    	String id=HttpUtil.queryStringForPost(url);
    	return id;
	}
	private void fillItemList(){

		String str=HttpUtil.queryForSend(id);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		String item[] = str.split("#");
		String[] itemAddress=new String[item.length];
		String[] itemDetail=new String[item.length];
		String[] itemNum=new String[item.length];
		String[] itemPhone=new String[item.length];
		String[] itemStatus=new String[item.length];
		Integer[] ItemIMGBar=new Integer[item.length];
		String tmp[];
		for(int i=0;i<item.length;i++){
			tmp=item[i].split("/");
			itemNum[i]=tmp[0];
			itemAddress[i]=tmp[2];
			itemDetail[i]=tmp[1];
			itemPhone[i]=tmp[3];
			itemStatus[i]=tmp[6];
			if(itemStatus[i].equals("0")){
				ItemIMGBar[i]=R.drawable.unknow;
			}
			else if(itemStatus[i].equals("1")){
				ItemIMGBar[i]=R.drawable.sending;
			}
			else if(itemStatus[i].equals("2")){
				ItemIMGBar[i]=R.drawable.ok;
			}
			else if(itemStatus[i].equals("3")){
				ItemIMGBar[i]=R.drawable.refuse;
			}
			else if(itemStatus[i].equals("4")){
				ItemIMGBar[i]=R.drawable.warning;
			}
		}
		
		for(int i=0;i<item.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();  
		    
			map.put("ItemAddress", itemAddress[i]);
		    map.put("ItemNum", itemNum[i]);
		    map.put("ItemDetail", itemDetail[i]);
		    map.put("ItemPhone",itemPhone[i]);
		    map.put("ItemIMGBar",ItemIMGBar[i]);
		    listItem.add(map); 
		}
	    SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
	    	R.layout.forsendlist,  
	    	//动态数组与ImageItem对应的子项          
	    	new String[] {"ItemAddress","ItemNum","ItemDetail","ItemPhone","ItemIMGBar"},   
	    	//ImageItem的XML文件里面的一个ImageView,两个TextView ID  
	    	new int[] {R.id.address,R.id.number,R.id.detail,R.id.phone,R.id.imagebar}  
	    ); 
		listView.setAdapter(listItemAdapter);
		listView.setOnItemClickListener(itemListener);
	}
	public void headInfo(boolean run,String msg){
		if(!run)
			progressbar.setVisibility(View.GONE);
		else 
			progressbar.setVisibility(View.VISIBLE);
		infoWall.setText(msg);
	}

	private OnItemClickListener itemListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			TextView num= (TextView) view.findViewById(R.id.number);
			TextView phone= (TextView) view.findViewById(R.id.phone);
			Bundle b =new Bundle();
			b.putString("id", id+"");
			b.putString("number", num.getText().toString());
			b.putString("phone", phone.getText().toString());
			Detail.launch(ExpressDelivery.this, b);
		}
	};
}
