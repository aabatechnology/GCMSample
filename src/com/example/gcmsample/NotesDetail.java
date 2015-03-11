package com.example.gcmsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class NotesDetail extends Activity {
	public static TextView txvProductName;
	public static TextView txvProductDisc;
	public static TextView txvProductDesc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes_detail);
		txvProductName=(TextView) findViewById(R.id.txv_product_det_nameV);
		txvProductDisc=(TextView) findViewById(R.id.txv_product_det_discountV);
		txvProductDesc=(TextView) findViewById(R.id.txv_product_det_descriptionV);
		Bundle extras=getIntent().getExtras();
		txvProductName.setText(extras.getString("productName").toString());
		txvProductDisc.setText(extras.getString("productDisc").toString());
		txvProductDesc.setText(extras.getString("productDesc").toString());
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_detail, menu);
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
}
