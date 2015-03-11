package com.example.gcmsample;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter {
	public ArrayList<HashMap<String,String>> list;
    Activity context;
    public MyListAdapter(Activity context, ArrayList<HashMap<String,String>> list) {
        super();
        this.context = context;
        this.list = list;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewDate;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		LayoutInflater inflater =  context.getLayoutInflater();

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.list_item_row, parent,false);
			//convertView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 60));
			holder = new ViewHolder();
			holder.txtViewTitle = (TextView) convertView.findViewById(R.id.lst_product_name);
			holder.txtViewDate = (TextView) convertView.findViewById(R.id.lst_product_date);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
        
		holder.txtViewTitle.setText(list.get(position).get("productName").toString());
		holder.txtViewDate.setText(list.get(position).get("productDate").toString());

	return convertView;
	}

}
