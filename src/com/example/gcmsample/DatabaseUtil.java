package com.example.gcmsample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUtil extends SQLiteOpenHelper {
	  private static final int DATABASE_VERSION = 1;
	    private static final String TABLE_NAME = "ENNA_OFFER";
	    private static final String ENTRY_NAME="PRODUCT_NAME";
	    private static final String ENTRY_DISCOUNT="PRODUCT_DISCOUNT";
	    private static final String ENTRY_DESCRIPTION="PRODUCT_DESCRIPTION";
	    private static final String ENTRY_DATE="PRODUCT_ADDED_DATE";
	    private static final String ENTRY_PSTATUS="PRODUCT_PUSH_STATUS";
	    private static final String ENTRY_RSTATUS="PRODUCT_READ_STATUS";
	    
	    private static final String OFFER_TABLE_CREATE =
	                "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME + " (PRODUCT_ID INTEGER PRIMARY KEY  AUTOINCREMENT, " +
	                		ENTRY_NAME + " TEXT, " + ENTRY_DISCOUNT + " TEXT,"+ ENTRY_DESCRIPTION+" TEXT,"+ ENTRY_DATE +" TEXT, "+
	                		ENTRY_PSTATUS+" TEXT, "+ENTRY_RSTATUS+" TEXT);";
	public DatabaseUtil(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(OFFER_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
 	

}
