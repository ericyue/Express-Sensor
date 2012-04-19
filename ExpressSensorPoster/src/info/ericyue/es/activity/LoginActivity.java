/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * Project Name:ExpressSensor
 * Create Date: 2011-7-17
 */
package info.ericyue.es.activity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import info.ericyue.es.util.HttpUtil;
import info.ericyue.es.R;
import info.ericyue.es.util.EncryptString;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
    public ProgressDialog welcomeDialog=null;
    public AlertDialog dialogBuilder;
	public EditText userName,passWord;
	public LayoutInflater li;
	public View myView;
	private boolean cancel=false;
	Handler handler=new Handler();
    Runnable runnable=new Runnable(){
       @Override
       public void run() {
    	   if(cancel)
    		   showDialog("远程服务器未就绪!",true);
       } 
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.welcome);  
    	li=LayoutInflater.from(LoginActivity.this);
    	myView=li.inflate(R.layout.login_dialog, null);
        userName=(EditText) myView.findViewById(R.id.userEditText);
        passWord=(EditText) myView.findViewById(R.id.pwdEditText);
        /**
         * 测试代码 等待删除
         */
        userName.setText("admin");
        passWord.setText("111111");
 
        loginDialog();
	}
    private void showDialog(String msg,final boolean close){
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("错误信息").setMessage(msg).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(close){
					finish();
				}
			}
		});
		AlertDialog alert=builder.create();
		alert.show();
    }
	private void showDialog(String msg){
		showDialog(msg,false);
	}
    private boolean login() throws Exception{
    	String username=userName.getText().toString();
    	String pwd=passWord.getText().toString();
    	pwd=EncryptString.encryptString(pwd, "MD5");//To Upper Case
    	String result=query(username,pwd);  	
		handler.removeCallbacks(runnable);
		cancel=false;
    	if(result!=null&&result.equals("1")){
    		return true;
    	} 		
    	if(result!=null&&result.equals("0")){
    		return false;
    	}
    		return false;
    }
	private boolean validate(){
		String username=userName.getText().toString();
		String pwd=passWord.getText().toString();
		if(username.equals("")&&!pwd.equals("")){
			showDialog("用户名必填！");
			return false;
		}
		if(pwd.equals("")&&!username.equals("")){
			showDialog("密码必填！");
			return false;
		}
		if(username.equals("")&&pwd.equals("")){
			showDialog("用户名和密码必填！");
			return false;
		}	
		return true;
	}
    private String query(String username,String password){
    	String queryString="username="+username+"&password="+password;
    	String url=HttpUtil.BASE_URL+"servlet/LoginVerify?"+queryString;
    	Log.i("Login地址", url);
    	Log.i("xinxi", HttpUtil.queryStringForPost(url));
    	return HttpUtil.queryStringForPost(url);
    }
    private void loginDialog(){
    	dialogBuilder=new AlertDialog.Builder(this)
    	.setView(myView)
    	.setCancelable(false).setNegativeButton("退出",
    	new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		})
    	.setPositiveButton("登陆",
    	new DialogInterface.OnClickListener() {
    		@Override
			public void onClick(DialogInterface arg0, int arg1) {
    			if(validate()){
    				cancel=true;
    				handler.postDelayed(runnable, 5000);
    				LoginThread loginthread=new LoginThread();
    				loginthread.execute();
    			}
			}
		}).create();
    	try{
    		Field field = dialogBuilder.getClass().getDeclaredField("mAlert");
            field.setAccessible(true);
            Object obj = field.get(dialogBuilder);
            field = obj.getClass().getDeclaredField("mHandler");
            field.setAccessible(true);
            field.set(obj,new ButtonHandler(dialogBuilder));
        }catch(Exception e){
        	e.printStackTrace();
        }
    	dialogBuilder.show();
    }
    /**
     * Login Dialog Defend Close
     * @author Ericyue@Moonlight
     *
     */
    class  ButtonHandler  extends  Handler{
    	private WeakReference <DialogInterface> mDialog;
        public ButtonHandler(DialogInterface dialog){
        	mDialog = new WeakReference <DialogInterface> (dialog);
        }
        @Override
        public void handleMessage(Message msg){
        	switch (msg.what){
        		case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                	((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                	break;
            }
        }
    }
    /**
     * 登陆线程
     * @author Moonlight
     */
    class LoginThread extends AsyncTask<Void, Void, Void> {
        ProgressDialog d = new ProgressDialog(LoginActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            d.setMessage("正在验证用户信息...");
            d.setTitle("请稍后...");
            d.show(); 
        }
        @Override
        protected Void doInBackground(Void... params) {
        	try {
				if(login()){
					Intent intent = new Intent(LoginActivity.this,ExpressSensor.class);
					Bundle bundle=new Bundle();
					bundle.putString("username",userName.getText().toString());
					intent.putExtras(bundle);	
					startActivityForResult(intent,0);
		            finish();
				}
				else{
					showDialog("用户名或密码错误，请核对");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
        }
    }
}