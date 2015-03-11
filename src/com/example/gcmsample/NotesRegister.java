package com.example.gcmsample;

import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NotesRegister extends Activity implements OnClickListener {
  
	public static Button btnGCMRegister;
	public static final String REG_ID = "regId";
	private static final String APP_VERSION = "appVersion";
	public String regId;
	Context context;
	public GoogleCloudMessaging gcm;
	 ShareExternalServer appUtil;
	 AsyncTask sharedRegidTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		regId=getRegistrationId(getApplicationContext());
		if(regId.equals("empty")){
			setContentView(R.layout.activity_notes_register);
			btnGCMRegister=(Button) findViewById(R.id.btn_notes_regGCM);
			context=getApplicationContext();
			btnGCMRegister.setOnClickListener(this);
		}		
		else
		{
			Intent home=new Intent(getApplicationContext(),NotesHome.class);startActivity(home);
			finish();
			//Toast.makeText(getApplicationContext(), "already installed", Toast.LENGTH_SHORT);
		}
		
	}
   public boolean checkFirstInstallation()
   {   
	   SharedPreferences pref=getSharedPreferences("config", Context.MODE_PRIVATE);
	   String result=pref.getString("first_time_installation", "yes");
	   if(result.equals("yes"))
	   return true ;	
	   
	   return false;
   }
   public void setFirstInstallation()
   {
	   final SharedPreferences prefs = getSharedPreferences(
				"config", Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("first_time_installation", "installed");		
		editor.commit();
   }
   public boolean checkConnecticity()
	{
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		return isConnected;
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		return super.onOptionsItemSelected(item);
	}
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
				"NotesPref", Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(
				"NotesPref", Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "empty");
		/*if (registrationId.isEmpty()) {
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return "";
		}*/
		return registrationId;
	}
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	public String registerGCM() {
     //int attempts=1;
		gcm = GoogleCloudMessaging.getInstance(this);
		regId = getRegistrationId(context);
		 if (regId.equals("empty")) {
		   registerInBackground();
		   regId = getRegistrationId(context);
		   	   appUtil=new ShareExternalServer();
			 /*  sharedRegidTask = new AsyncTask() {			
				    
					@Override
					protected Object doInBackground(Object... params) {
						// TODO Auto-generated method stub
						String result = appUtil.registerIdwithAppServer(context, regId);
						return (Object)result;
					}
					@Override
				protected void onPostExecute(Object params) {
			    	// TODO Auto-generated method stub
			    	   sharedRegidTask = null;
						Toast.makeText(getApplicationContext(), params.toString(),	Toast.LENGTH_LONG).show();
			    }

				};
				sharedRegidTask.execute(null, null, null);*/
		   }
		   
		   	   
		  // regId = getRegistrationId(context);
		
		 return regId; 
	}
	
   public void buttonRegListener()
   {
	   regId = getRegistrationId(getApplicationContext());
	   if (regId.equals("empty")) {	
		   if(checkConnecticity()){
			   regId = registerGCM();
		   }
			  		   else{Toast.makeText(getApplicationContext(),
					"connection problem..try again",
					Toast.LENGTH_SHORT).show();}
			
			} else {
				Toast.makeText(getApplicationContext(),
						"Already Registered with GCM Server!"+regId,
						Toast.LENGTH_SHORT).show();
				Intent home=new Intent(getApplicationContext(),NotesHome.class);startActivity(home);
			}
   }
   private void registerInBackground() {
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
				String msg = "";
				try {	
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regId = gcm.register(Config.GOOGLE_PROJECT_ID);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					msg = "Device registered";

					storeRegistrationId(context, regId);
					Intent home=new Intent(getApplicationContext(),NotesHome.class);startActivity(home);
					//setFirstInstallation();
				} catch (IOException ex) {
					//String  temp="APA91bF8G8_1rW95Emmhu9vkSeggeMwUJdprUmLb2bClz01D61vYRjK3IMdEO6VRP_FTsKAL9Dv7L9I3KthKsz1KZ6JqS2oWqc7GKBmB7M8xBB3mhgrSrxmma2KF1DEsyvmxfbyeLYFpUVovqpoRliK-7-lR8FOE1w";
					//storeRegistrationId(context, temp);
					//Toast.makeText(getApplicationContext(),"Unable to reach GCM server..please try again",Toast.LENGTH_SHORT).show();
					
					msg = "Unable to reach GCM server..please try again";
					
				}
				
				return (Object)msg;
			}
			@Override
			protected void onPostExecute(Object result) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),result.toString(), Toast.LENGTH_SHORT).show();
				
			}
			
	}.execute(null,null,null);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.btn_notes_regGCM)
		{
			buttonRegListener();
		}
	}
}
