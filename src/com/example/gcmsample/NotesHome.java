package com.example.gcmsample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TableLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
public class NotesHome extends Activity implements OnItemClickListener  {
	private static final String TABLE_NAME = "ENNA_OFFER";
    private static final String ENTRY_NAME="PRODUCT_NAME";
    private static final String ENTRY_DISCOUNT="PRODUCT_DISCOUNT";
    private static final String ENTRY_DESCRIPTION="PRODUCT_DESCRIPTION";
    private static final String ENTRY_DATE="PRODUCT_ADDED_DATE";
	private static final String DATABASE_NAME = "OFFERDB.db";
	public static final int DATABASE_VERSION = 1;
	private static  SQLiteDatabase db=null;
	public static  DatabaseUtil dbUtil=null;
	public static ListView listView;
	public  static ArrayList<HashMap<String,String>> notelistData;//=new ArrayList<HashMap<String,String>>();
	private static  Cursor c=null;
	public static NotificationReciever reciever;
	public static NotificationReciever reciever1;
	 ShareExternalServer appUtil;
	 AsyncTask sharedRegidTask;
	 public static final String REG_ID = "regId";
	 public static  String regId;;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes_home);
		listView=(ListView) findViewById(R.id.listview1);
		listView.setOnItemClickListener(this);
		listViewData();	
		
		
		
		}

public void pushDatatoList(){
	
	HashMap<String,String> item=new HashMap<String,String>();
	String productName=c.getString(c.getColumnIndex(ENTRY_NAME));
	String productDiscount=c.getString(c.getColumnIndex(ENTRY_DISCOUNT));
	String productDescription=c.getString(c.getColumnIndex(ENTRY_DESCRIPTION));
	String productDate=c.getString(c.getColumnIndex(ENTRY_DATE));
	item.put("productName", productName);
	item.put("productDisc", productDiscount);
	item.put("productDesc", productDescription);
	item.put("productDate", productDate);
	notelistData.add(item);	
	}
public void listViewData()
{ 
	notelistData=null;
	notelistData=new ArrayList<HashMap<String,String>>();
	dbUtil=new DatabaseUtil(getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
	db=dbUtil.getWritableDatabase();
	 
		
	   String query="select *from "+TABLE_NAME+";";
	     c=db.rawQuery(query, null);

			if(c.getCount()>0){
			Log.d("DBcount","row count"+c.getCount());	
				//pushToMap(c);
				c.moveToLast();
				pushDatatoList();
			while (c.moveToPrevious()) {
				pushDatatoList();
			
			
			}
			c.close();
			}
	
			setmyListAdapter();		
		
		
		
}
public boolean checkConnecticity()
{
	ConnectivityManager cm =
	        (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	 
	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	boolean isConnected = activeNetwork != null &&
	                      activeNetwork.isConnectedOrConnecting();
	return isConnected;
	
}
public void shareWithServer( final Intent data)
{
	appUtil = new ShareExternalServer();
	final SharedPreferences prefs = getSharedPreferences("NotesPref", Context.MODE_PRIVATE);
	 regId = prefs.getString(REG_ID, "empty");
	
	final Context context = getApplicationContext();
	//Toast.makeText(getApplicationContext(), "RegId to share:"+regId,Toast.LENGTH_LONG).show();
	sharedRegidTask = new AsyncTask() {			
    
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			String result = appUtil.shareregIdwithAppServer(context, regId,data.getExtras().getString("productName").toString(),
					data.getExtras().getString("productDiscount").toString(),data.getExtras().getString("productDescription").toString(),
					data.getExtras().getString("productDate").toString(),data.getExtras().getString("productPushStatus").toString(),
					data.getExtras().getString("productReadStatus").toString());
			return (Object)result;
		}
		@Override
	protected void onPostExecute(Object params) {
    	// TODO Auto-generated method stub
    	   sharedRegidTask = null;
			Toast.makeText(getApplicationContext(), params.toString(),	Toast.LENGTH_LONG).show();
    }

	};
	sharedRegidTask.execute(null, null, null);
}
   @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	if(resultCode==100)
	{
		//String action=data.getExtras().getString("Title");
		   //pushDatatoList(data);
			listViewData();
			if(checkConnecticity())
			shareWithServer(data);
			else
			Toast.makeText(getApplicationContext() , "Connection problem", Toast.LENGTH_SHORT).show();
			
		
	}
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_home, menu);
		return true;
	}
  public void setmyListAdapter()
  {
	//  ListAdapter adapter=new SimpleAdapter(this,notelistData,R.layout.list_item_row,new String[]{"productName","productDate"},new int[]{R.id.lst_product_name,R.id.lst_product_date});
		MyListAdapter adapter=new MyListAdapter(this, notelistData);
	    listView.setAdapter(adapter);
	    
  }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.men_note_addNew) {
			Intent addIntent=new Intent(NotesHome.this,NotesAdd.class);
			startActivityForResult(addIntent, 100);				
			}
		return super.onOptionsItemSelected(item);
	}
	public void refreshData()
	{
		listViewData();
	    setmyListAdapter();	
	}
	public  static class NotificationReciever extends BroadcastReceiver{
        NotesHome home=new NotesHome();
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
		      //   home.refreshData();			
			 //   Toast.makeText(context, "Refreshed", Toast.LENGTH_SHORT).show();	
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int index=position;
		Intent intent=new Intent(getApplicationContext(),NotesDetail.class);
		intent.putExtra("productName",notelistData.get(position).get("productName"));
		intent.putExtra("productDisc",notelistData.get(position).get("productDisc"));
		intent.putExtra("productDesc",notelistData.get(position).get("productDesc"));
		startActivity(intent);
		// TODO Auto-generated method stub
		
	}
	
	
}
