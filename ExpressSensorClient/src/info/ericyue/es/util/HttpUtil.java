package info.ericyue.es.util;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   HttpUtil.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-7-18
 */ 
public class HttpUtil{
	//public static final String BASE_URL="http://10.0.2.2:8080/ExpressSensorWeb/";
	public static final String BASE_URL="http://192.168.0.102:8080/ExpressSensorWeb/";
	public static HttpGet getHttpGet(String url){
		HttpGet request = new HttpGet(url); 
		return request;
	}
	public static HttpPost getHttpPost(String url){
		HttpPost request= new HttpPost(url);
		return request;
	}
	public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	public static String queryStringForPost(String url){
		HttpPost request = HttpUtil.getHttpPost(url);
		String result=null;		
		try{
			HttpResponse response =HttpUtil.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode()==200){
				result=EntityUtils.toString(response.getEntity());
				return result;
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
			result="-1";
	//		result="Õ¯¬Á“Ï≥£";
			return result;
		}catch(IOException e){
			e.printStackTrace();
			result="-1";
			//result="Õ¯¬Á“Ï≥£";
			return result;
		}
		return result;
	}
	public static String queryStringForGet(String url){
		HttpGet request =HttpUtil.getHttpGet(url);
		String result = null;		
		try{
			HttpResponse response=HttpUtil.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode()==200){
				result=EntityUtils.toString(response.getEntity());
				return result;
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
			result="-1";
			//result="Õ¯¬Á“Ï≥£";
			return result;
		}catch(IOException e){
			e.printStackTrace();
			result="-1";
			//result="Õ¯¬Á“Ï≥£";
			return result;
		}
		return result;
	}

	public static String queryStringForPost(HttpPost request){
		String result = null;
		
		try{
			HttpResponse response=HttpUtil.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode()==200){
				result=EntityUtils.toString(response.getEntity());
				return result;
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
			result="-1";
			//result="Õ¯¬Á“Ï≥£";
			return result;
		}catch(IOException e){
			e.printStackTrace();
			result="-1";
			//result="Õ¯¬Á“Ï≥£";
			return result;
		}
		return result;
	}
	public static String queryStatistics(int id,String queryType){
		String queryStr="id="+id+"&querytype="+queryType;
		String url=HttpUtil.BASE_URL+"servlet/QueryStatistics?"+queryStr;	
		return HttpUtil.queryStringForPost(url);
	}
	public static String queryTradeInfo(String via,String viavalue,String querytype){
		String queryStr="via="+via+"&viavalue="+viavalue+"&querytype="+querytype;
		String url=HttpUtil.BASE_URL+"servlet/QueryTradeInfo?"+queryStr;	
		return HttpUtil.queryStringForPost(url);
	}
	public static String queryRole(int id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/QueryRole?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String QueryWorkerCurrentLocation(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/QueryWorkerCurrentLocation?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String UpdateUserCurrentLocation(String id,String lat,String lng){
		String queryStr="id="+id+"&lat="+lat+"&lng="+lng;
		String url=HttpUtil.BASE_URL+"servlet/UpdateUserCurrentLocation?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String QueryBaseInfo(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/QueryBaseInfo?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String QuerySystemInfo(String type){
		String queryStr="type="+type;
		String url=HttpUtil.BASE_URL+"servlet/QuerySystemInfo?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String UpdateTradeStatus(String number,String status){
		String queryStr="number="+number+"&status="+status;
		String url=HttpUtil.BASE_URL+"servlet/UpdateTradeStatus?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String DeleteCurrentUserLocation(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/DeleteCurrentUserLocation?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String UpdateStatistics(String id,String item,String mode){
		String queryStr="id="+id+"&item="+item+"&mode="+mode;
		String url=HttpUtil.BASE_URL+"servlet/UpdateStatistics?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String UpdateCash(String id,String value){
		String queryStr="id="+id+"&value="+value;
		String url=HttpUtil.BASE_URL+"servlet/UpdateCash?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
	public static String QueryUserInfo(String id){
		String queryStr="id="+id;
		String url=HttpUtil.BASE_URL+"servlet/QueryUserInfo?"+queryStr;
		return HttpUtil.queryStringForPost(url);
	}
}