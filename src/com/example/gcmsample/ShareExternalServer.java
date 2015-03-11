package com.example.gcmsample;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ShareExternalServer {
	static final String TAG = "Share Activity";
 public String registerIdwithAppServer(final Context context,final String regId)
 {
	 String result = "";
	 
		Map paramsMap = new HashMap();
		paramsMap.put("regId", regId);
		Log.d(TAG,"share Activity:Before Start");
		try
		{
			URL serverURL=null;
			try
			{
				serverURL=new URL(Config.APP_REG_SERVER_URL);
			} catch(MalformedURLException e){result = "Invalid URL: " + Config.APP_REG_SERVER_URL;}
			StringBuilder postBody = new StringBuilder();
			Iterator iterator = paramsMap.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry param = (Entry) iterator.next();
				postBody.append(param.getKey()).append('=')
						.append(param.getValue());
				if (iterator.hasNext()) {
					postBody.append('&');
				}
			}
			String body = postBody.toString();
			byte[] bytes = body.getBytes();
			HttpURLConnection httpCon = null;
			try {
				httpCon = (HttpURLConnection) serverURL.openConnection();
				httpCon.setDoOutput(true);
				
				httpCon.setUseCaches(false);
				httpCon.setFixedLengthStreamingMode(bytes.length);
				httpCon.setRequestMethod("POST");
				httpCon.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				OutputStream out = httpCon.getOutputStream();
				Log.d(TAG,"share Activity:inside try");
				out.write(bytes);
				out.close();

				int status = httpCon.getResponseCode();
				if (status == 200) {
					result = "Registered ";
				} else {
					result = "Register Failure." + " Status: " + status;
				}
			} finally {
				if (httpCon != null) {
					httpCon.disconnect();
				}
		}
			
}
		catch (IOException e) {
			result = "Post Failure. Error in sharing with App Server.";
			Toast.makeText(context, result, Toast.LENGTH_SHORT);
		}
		return result;

 }
 public String shareregIdwithAppServer(final Context context,final String regId,final String name,final String discount,final String description,final String date,final String pstatus,final String rstatus)
 {
	 String result = "";
	 
		Map paramsMap = new HashMap();
		paramsMap.put("regId", regId);
		paramsMap.put("productName", name);
		paramsMap.put("productDiscount", discount);
		paramsMap.put("productDescription", description);
		paramsMap.put("productDate", date);
		paramsMap.put("pushStatus", pstatus);
		paramsMap.put("readStatus", rstatus);
		Log.d(TAG,"share Activity:Before Start");
		try
		{
			URL serverURL=null;
			try
			{
				serverURL=new URL(Config.APP_SHARE_SERVER_URL);
			} catch(MalformedURLException e){result = "Invalid URL: " + Config.APP_SHARE_SERVER_URL;}
			StringBuilder postBody = new StringBuilder();
			Iterator iterator = paramsMap.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry param = (Entry) iterator.next();
				postBody.append(param.getKey()).append('=')
						.append(param.getValue());
				if (iterator.hasNext()) {
					postBody.append('&');
				}
			}
			String body = postBody.toString();
			byte[] bytes = body.getBytes();
			HttpURLConnection httpCon = null;
			try {
				httpCon = (HttpURLConnection) serverURL.openConnection();
				httpCon.setDoOutput(true);
				
				httpCon.setUseCaches(false);
				httpCon.setFixedLengthStreamingMode(bytes.length);
				httpCon.setRequestMethod("POST");
				httpCon.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				OutputStream out = httpCon.getOutputStream();
				Log.d(TAG,"share Activity:inside try");
				out.write(bytes);
				out.close();

				int status = httpCon.getResponseCode();
				if (status == 200) {
					result = "shared ";
				} else {
					result = "Post Failure." + " Status: " + status;
				}
			} finally {
				if (httpCon != null) {
					httpCon.disconnect();
				}
		}
			
 }
		catch (IOException e) {
			result = "Post Failure. Error in sharing with App Server.";
			//Toast.makeText(context, result, Toast.LENGTH_SHORT);
		}
		return result;
 }	
}
