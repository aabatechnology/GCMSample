package com.example.gcmsample;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.google.android.gms.internal.mb;




import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class NotesAdd extends Activity implements android.view.View.OnClickListener {
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
	public static EditText etxProductName;
    public static EditText etxProductDiscount;
    public static EditText etxProductDescription;
    public static  int NOTIFICATION_ID = 1;
    public static int count=1;
	private static NotificationManager mNotificationManager;
	public static NotificationCompat.Builder mBuilder;
	//NotificationCompat.Builder builder;
	public static final String GROUP_KEY = "GCMNotification";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes_add);
		etxProductName=(EditText) findViewById(R.id.etx_product_name);
		etxProductDiscount=(EditText) findViewById(R.id.etx_product_discount);
		etxProductDescription=(EditText) findViewById(R.id.etx_product_description);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_add, menu);
		return true;
	}
 public void insertToDB()
 {
	    dbUtil=new DatabaseUtil(getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
		db=dbUtil.getWritableDatabase();
		DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate1 = df1.format(new Date());
		ContentValues cv=new ContentValues();
		 cv.put(ENTRY_NAME, etxProductName.getText().toString());
		 cv.put(ENTRY_DISCOUNT, etxProductDiscount.getText().toString());
		 cv.put(ENTRY_DESCRIPTION, etxProductDescription.getText().toString());
		 cv.put(ENTRY_DATE,formattedDate1);
		 cv.put(ENTRY_PSTATUS,"LOCAL");
		 cv.put(ENTRY_RSTATUS,"UNREAD");
	  //      db.insert(TABLE_NAME, null, cv);
		    db.close();
		   Log.d("NotesAdd", "msg:Added");    
		    Intent intent=new Intent(getApplicationContext(),NotesHome.class);
		    intent.putExtra("productName",etxProductName.getText().toString());
			intent.putExtra("productDiscount",etxProductDiscount.getText().toString());
			intent.putExtra("productDescription",etxProductDescription.getText().toString());
			intent.putExtra("productDate",formattedDate1);
			intent.putExtra("productPushStatus","LOCAL");
			intent.putExtra("productReadStatus","UNREAD");
			setResult(100, intent);
			finish();
 }
 
 private void sendNotification(String  extras) {
		String msg=extras;
		//String msg="success";
		if(mNotificationManager==null)
		 { 
			 mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
			    Intent messageIntent=new Intent(this,NotesHome.class); 
			    messageIntent.putExtra(Config.MESSAGE_KEY, msg);
				PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,messageIntent, 0);
			    mBuilder = new NotificationCompat.Builder(
						this).setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle("GCM Notification")
						.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
						.setContentText(msg)
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
		/*Intent intent=new Intent();
		intent.setAction("com.example.gcmsample.REFRESH_CONTENT");
		sendBroadcast(intent);*/
	}
 public boolean checkValues(){
	 if((etxProductName.getText().toString().equals("") || etxProductDiscount.getText().toString().equals("")||
			 etxProductDescription.getText().toString().equals(""))){
		 Toast.makeText(getApplicationContext(), "Details Empty!!", Toast.LENGTH_SHORT).show();
	 return false;}
	 //Toast.makeText(getApplicationContext(), etxProductName.getText().toString(), Toast.LENGTH_SHORT).show();
	 return true;
 }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		int id = item.getItemId();
		switch(id)
		{
		case android.R.id.home:	
			Intent intent=new Intent(getApplicationContext(),NotesHome.class);
			startActivity(intent);
			finish();
			break;
		case R.id.men_offer_send:
			if(checkValues())
			insertToDB();
			//sendNotification("hello world");
			//finish();
		break;
			}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	
	
}
