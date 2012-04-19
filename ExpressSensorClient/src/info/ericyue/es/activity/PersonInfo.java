package info.ericyue.es.activity;

import info.ericyue.es.R;
import info.ericyue.es.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PersonInfo extends Activity {
	private ListView listView;
	private String[] str=new String[9];
	private String id;
	private Bundle bundle;
	public static void launch(Context c,Bundle bundle){
		Intent intent = new Intent(c, PersonInfo.class);
		intent.putExtras(bundle);
		c.startActivity(intent);
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.userinfo);
        listView=(ListView) findViewById(R.id.aboutList);
        listView.setCacheColorHint(0);

        bundle=this.getIntent().getExtras();
        id=bundle.getString("id");
        fillItemList();
	
	}
	private void fillItemList(){
		queryDatabase();
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
		String[] itemStr={"ID","用户名","密码(MD5加密)","姓名","性别","角色","联系电话","电子邮箱","联系地址"};
  		for(int i=0;i<9;i++){
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
	} 
	public void queryDatabase(){
//		2/admin/96e79218965eb72c92a549dd5a330112/男/1/151233336075/hi.moonlight@gmail.com/天津市北辰区河北工业大学 计算机学院/某某
		
		String tmp=HttpUtil.QueryUserInfo(id);
		
		String[] item=tmp.split("/");
		str[0]="用户ID:"+item[0];
		str[1]="用户登录名:"+item[1];
		str[2]=item[2];
		str[3]="用户姓名:"+item[8];
		str[4]="用户性别:"+item[3];
		str[5]="账户类型:"+item[4].equals("1") != null?"快递员":"用户";
		str[6]="联系电话:"+item[5];
		str[7]="电子邮箱:"+item[6];
		str[8]="联系地址:"+item[7];
	}

 }
