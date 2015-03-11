package com.example.gcmsample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.gcmsample.NotesHome.NotificationReciever;
import com.google.android.gms.gcm.GoogleCloudMessaging;
public class GCMNotificationIntentService extends IntentService {
	public static  int NOTIFICATION_ID = 1;
    public static int count=1;
	private static NotificationManager mNotificationManager;
	public static NotificationCompat.Builder mBuilder;
	private static final String TABLE_NAME = "ENNA_OFFER";
    private static final String ENTRY_NAME="PRODUCT_NAME";
    private static final String ENTRY_DISCOUNT="PRODUCT_DISCOUNT";
    private static final String ENTRY_DESCRIPTION="PRODUCT_DESCRIPTION";
    private static final String ENTRY_DATE="PRODUCT_ADDED_DATE";
    private static final String ENTRY_PSTATUS="PRODUCT_PUSH_STATUS";
    private static final String ENTRY_RSTATUS="PRODUCT_READ_STATUS";
	private static final String DATABASE_NAME = "OFFERDB.db";
	public static final int DATABASE_VERSION = 1;
	private static  SQLiteDatabase db=null;
	public static  DatabaseUtil dbUtil=null;
	public static Bundle extras;
	public GCMNotificationIntentService() {
		super("GcmIntentService");
		// TODO Auto-generated constructor stub
	}
	public static final String TAG = "GCMNotificationIntentService";
@Override
protected void onHandleIntent(Intent intent) {
	// TODO Auto-generated method stub
	//Toast.makeText(getApplicationContext(), "Recieved",	Toast.LENGTH_LONG).show();
	
	extras=intent.getExtras();
	GoogleCloudMessaging gcm=GoogleCloudMessaging.getInstance(this) ;
	String messageType=gcm.getMessageType(intent);
	
	if(!extras.isEmpty())
	{
		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
				.equals(messageType)) {
			//sendNotification("Send error: " + extras.toString());
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
				.equals(messageType)) {
			//sendNotification("Deleted messages on server: "+ extras.toString());
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
				.equals(messageType)) {
			//sendNotification("Gcm says:"+extras.getString("m").toString());
			
			sendNotification(intent);
		}
	}
	GCMbroadcastReciever.completeWakefulIntent(intent);
	
}
public void insertToDB(Intent intent)
{
	 dbUtil=new DatabaseUtil(getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
		db=dbUtil.getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put(ENTRY_NAME, intent.getExtras().getString("productName").toString());
		 cv.put(ENTRY_DISCOUNT, intent.getExtras().getString("productDiscount").toString());
		 cv.put(ENTRY_DESCRIPTION, intent.getExtras().getString("productDescription").toString());
		 cv.put(ENTRY_DATE,intent.getExtras().getString("productDate").toString());
		 cv.put(ENTRY_PSTATUS,intent.getExtras().getString("pushStatus").toString());
		 cv.put(ENTRY_RSTATUS,intent.getExtras().getString("readStatus").toString());
		 db.insert(TABLE_NAME, null, cv);
		 db.close();
		 }
private void sendNotification(Intent intent) {
	//String msg=extras.getString("productName").toString();
	//String msg="success";
	if(mNotificationManager==null)
	 { 
		 mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		    Intent messageIntent=new Intent(this,NotesHome.class); 
		   // messageIntent.putExtra(Config.MESSAGE_KEY, intent.getExtras().getString("productName").toString());
			PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,messageIntent, 0);
		    mBuilder = new NotificationCompat.Builder(
					this).setSmallIcon(R.drawable.ic_launcher_offer)
					.setContentTitle("GCM Notification")
					.setStyle(new NotificationCompat.BigTextStyle().bigText(intent.getExtras().getString("productName").toString()))
					.setContentText(intent.getExtras().getString("productName").toString())
					.setAutoCancel(true);
		    mBuilder.setContentIntent(contentIntent);
		     // insertToDB(extras);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
			Log.d("Notification", "Notification sent successfully:::::."+NOTIFICATION_ID);
			
	 }
	 else	
	 {
		 count=count+1;
		mBuilder .setContentTitle(count+" New Messages.. ");
		mBuilder.setContentText(count+" Messages has unread..");
		//insertToDB(extras);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	 }
	Uri notification_ring = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification_ring);
	r.play();
	insertToDB(intent);
	//Intent intent1=new Intent();
	//intent1.setAction("com.example.gcmsample.REFRESH_CONTENT");
	//sendBroadcast(intent1);
}
}
