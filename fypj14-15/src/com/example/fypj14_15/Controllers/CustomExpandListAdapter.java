package com.example.fypj14_15.Controllers;


import java.util.ArrayList;

import com.example.fypj14_15.R;
import com.example.fypj14_15.Model.GroupItem;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CustomExpandListAdapter extends ArrayAdapter<GroupItem> {

	private Activity context;
	private View getView;
	private int status;
	private boolean[] canSubmit = new boolean[]{true,true,true,true,true,true};
	
	private String[] qnList = new String[6]; // store question list
	private String[] rmkList = new String[5]; // store remarks list
	private TextView[] rating = new TextView[5]; // initialize 5 number labels
	
	private ArrayList<GroupItem> dataList = null;
	
	public CustomExpandListAdapter(Activity context, ArrayList<GroupItem> dataList, int status) {
		super(context, R.layout.grouplist_item, dataList);
		this.context = context;
		this.dataList = dataList;
		this.status = status;
	}
	
	public int getCount(){
		return dataList.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.grouplist_item, parent, false);
			getView = convertView;
		}
		Initialize_TextViewArray(convertView);
		
		final int index= position;
		
		// initialize data string arrays
		qnList = convertView.getResources().getStringArray(R.array.questions);
		rmkList = convertView.getResources().getStringArray(R.array.ratings);
		
		final LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.child_item);
		if (dataList.get(position).getOnSelect())
			layout.setVisibility(View.VISIBLE);
		else
			layout.setVisibility(View.GONE);
		
		SeekBarStatus_StyleChanged(dataList.get(index).getValue());
		
		// initialize variables for screen controls
		TextView sn_counter = (TextView) convertView.findViewById(R.id.sn_counter);
		TextView question = (TextView) convertView.findViewById(R.id.question);
		Button rmkBtn = (Button) convertView.findViewById(R.id.toggle_rmk);
		rmkBtn.setFocusable(false);
		
		// logic: remark TextView set text now to the current value in relation with dataSet via group position 
		rmkBtn.setText(rmkList[dataList.get(position).getValue()]);
		
		sn_counter.setTypeface(null, Typeface.BOLD);
		rmkBtn.setTypeface(null, Typeface.BOLD);
		sn_counter.setText(String.valueOf (position + 1) + ".");
		question.setText(qnList[position]);
		
		rmkBtn.setTag(index);
		rmkBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
			
				((Button) view).setBackground(new Button(getView.getContext()).getBackground());
				int position = (Integer) view.getTag();
				int prevSelected = -1;
				for (int i=0; i<dataList.size(); i++){
					if (dataList.get(i).getOnSelect())
						prevSelected = i;
						dataList.get(i).setOnSelect(false);
						Log.e("Layout","Hide");
				}
				if (prevSelected != position)
				{
					dataList.get(position).setOnSelect(true);
					Log.e("Layout","Show");
				}
				notifyDataSetChanged();
			}
		});
		
		// change background color on validation if false
		if (!canSubmit[index])
			rmkBtn.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark));
		
		SeekBar rateBar = (SeekBar) convertView.findViewById(R.id.seekBar1);
		
		rateBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			
				if (dataList.contains(dataList.get(index)))
				{
					Log.e("Present at: ", "SeekBar "+index);
					dataList.get(index).setValue(progress);
				}
				
				// SeekBarStatus_StyleChanged(dataList.get(index).getValue());
				Log.e("Value changed to: ", String.valueOf(dataList.get(index).getValue()));
				notifyDataSetChanged();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
		});
		
		if (status == 3)
		{
			// if status: complete, hide but display read-only values
			rmkBtn.setVisibility(View.GONE);
			layout.setVisibility(View.GONE);
			((TextView) convertView.findViewById(R.id.rmklabel))
				.setText("Remark: " + rmkList[dataList.get(position).getValue()]);
		}
		
		return convertView;
	}
	
	/**
	 * Custom methods configured for desired outcomes on child and group views
	 * @param rootView
	 */
	
	// method to initialize TextView array
	private void Initialize_TextViewArray(View rootView)
	{
		rating[0] = (TextView) rootView.findViewById(R.id.label1);
		rating[1] = (TextView) rootView.findViewById(R.id.label2);
		rating[2] = (TextView) rootView.findViewById(R.id.label3);
		rating[3] = (TextView) rootView.findViewById(R.id.label4);
		rating[4] = (TextView) rootView.findViewById(R.id.label5);
	}
	
	// method to change styles of SeekBar status
	private void SeekBarStatus_StyleChanged (int position)
	{
		int index = position - 1;
		
		for (int i=0; i<rating.length; i++)
		{
			// reset all TextView to default text
			rating[i].setText(String.valueOf(i+1));
			// reset all TextView to default style
			rating[i].setBackgroundColor(Color.TRANSPARENT);
			rating[i].setTypeface(null, Typeface.NORMAL);
		}
		if (index >= 0)
		{
			// style selected TextView
			rating[index].setText("( "+ (position) +" )");
			rating[index].setBackgroundColor(Color.YELLOW);
			rating[index].setTypeface(null, Typeface.BOLD);
		}
	}
	
	public boolean validateForm() {
	
		for (int i=0; i<dataList.size(); i++)
		{
			if (dataList.get(i).getValue() > 0)
			{
				canSubmit[i] = true;
			} else {
				// default value is still selected on seekBar: *0
				canSubmit[i] = false;
			}
		}
		notifyDataSetChanged();
		
		for (int i=0; i<canSubmit.length; i++)
		{
			if (!canSubmit[i])
				return false;
		}
		return true;
	}
	
	public ArrayList<GroupItem> getDataList() {
		return dataList;
	}
}
