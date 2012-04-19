/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   Detail.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-8-17
 */
package info.ericyue.es.activity;

import info.ericyue.es.R;
import info.ericyue.es.util.*;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Detail extends Activity {
	private ListView listView;
	private String[] str=new String[7];
	private Bundle bundle;
	private String paytype="";
	private String number="";
	private String name="";
	private String address="";
	private String postcode="";
	private String tel="";
	private String cash="";
	private String id="";
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, Detail.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.detailinfo);
         listView=(ListView) findViewById(R.id.detaillist);
         listView.setCacheColorHint(0);

         bundle=this.getIntent().getExtras();
         id=bundle.getString("id");
         number=bundle.getString("number");
         Toast.makeText(this, number, Toast.LENGTH_LONG).show();
         QueryDatabase();
         fillItemList();
	}
	public void QueryDatabase(){
		name=HttpUtil.queryTradeInfo("trade_number",number,"receiver_name");
		address=HttpUtil.queryTradeInfo("trade_number",number,"receiver_address");
		postcode=HttpUtil.queryTradeInfo("trade_number",number,"receiver_postcode");
		tel=HttpUtil.queryTradeInfo("trade_number",number,"receiver_phone");
		cash=HttpUtil.queryTradeInfo("trade_number",number,"trade_cash")+" 元";
		switch(Integer.parseInt(HttpUtil.queryTradeInfo("trade_number",number,"pay_type"))){
		case 0:
			paytype="未指定";
			break;
		case 1:
			paytype="网银支付";
			break;
		case 2:
			paytype="货到付款";
			break;
		case 3:
			paytype="支付宝";
			break;
		case 4:
			paytype="财付通";
			break;
		case 5:
			paytype="信用卡";
			break;
		case 6:
			paytype="邮局汇款";
			break;
		}
		str[0]=number;
		str[1]=name;
		str[2]=address;
		str[3]=postcode;
		str[4]=tel;
		str[6]=cash;
		str[5]=paytype;
	}
	private void fillItemList(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		String[] itemStr={"邮包编号","收件人姓名","收件人地址","收件人邮编","收件人电话","支付类型","支付金额(若货到付款)"};
  		for(int i=0;i<7;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();  
		    map.put("ItemTitle", itemStr[i]);
		    map.put("ItemText", str[i]);
		    listItem.add(map); 
		}
	    SimpleAdapter listItemAdapter = new SimpleAdapter(this,listItem,//数据源   
	    	R.layout.detaillist,  
 	    	new String[] {"ItemTitle","ItemText"},   
 	    	new int[] {R.id.ItemTitle,R.id.ItemText}  
	    ); 
		listView.setAdapter(listItemAdapter);
		listView.setOnItemClickListener(itemListener);
	}
	private OnItemClickListener itemListener = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			if(position==4){
				try{ 
					String strInput = bundle.getString("phone"); 
					if (strInput.length()==11){ 
						Intent myIntentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+strInput)); 
						startActivity(myIntentDial); 
					} 
					else{
						Toast.makeText(Detail.this, "电话格式不符", Toast.LENGTH_LONG).show();
					}
				} 
				catch(Exception e){
					e.printStackTrace();
				} 
			}
				
		}
	};
 }