package com.example.fypj14_15.Controllers;

import java.util.ArrayList;

import com.example.fypj14_15.R;
import com.example.fypj14_15.Model.ListItem;
import com.example.fypj14_15.R.id;
import com.example.fypj14_15.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListAdapter extends ArrayAdapter<ListItem>{

	private final Activity context;
	private final ArrayList<ListItem> modelsArrayList;
	
	public ListAdapter(Activity context, ArrayList<ListItem> modelsArrayList)
	{
		super(context, R.layout.fragment_main, modelsArrayList);
		
		this.context = context;
		this.modelsArrayList = modelsArrayList;
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			// 1. Create inflater
			LayoutInflater inflater = context.getLayoutInflater();
			
			// 2. Get rowView from inflater
			convertView = inflater.inflate(R.layout.template, parent, false);
		}
		
		
		//3. Get icon & title views from rowView
		ImageView imgView = (ImageView) convertView.findViewById(R.id.imgIcon);
		TextView nameView = (TextView) convertView.findViewById(R.id.childName);
		TextView idView = (TextView) convertView.findViewById(R.id.childID);
		TextView statusView = (TextView) convertView.findViewById(R.id.status);
		
		// 4. Set the text for textView
		imgView.setImageResource(modelsArrayList.get(position).getIcon());
		nameView.setText(modelsArrayList.get(position).getName());
		idView.setText(modelsArrayList.get(position).getId());
		statusView.setText(modelsArrayList.get(position).getStatus());
		// 5. return rowView
		return convertView;
	}
	
}
