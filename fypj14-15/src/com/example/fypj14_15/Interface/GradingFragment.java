package com.example.fypj14_15.Interface;

import java.util.ArrayList;

import com.example.fypj14_15.R;
import com.example.fypj14_15.Controllers.CustomExpandListAdapter;
import com.example.fypj14_15.Database.GradingDB;
import com.example.fypj14_15.Database.SessionDB;
import com.example.fypj14_15.Model.GroupItem;
import com.example.fypj14_15.Model.Grading;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class GradingFragment extends Fragment{

	private View rootView;
	private ListView gradeListView;
	private CustomExpandListAdapter adapter;
	private ArrayList<GroupItem> dataList;
	private int profileId, status;
	private int[] qnAns = new int[6];
	private Grading fdbackForm = new Grading();
	
	public GradingFragment(int profileId, int status) {
	
		this.profileId = profileId;
		this.status = status;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        rootView = inflater.inflate(R.layout.feedback_fragmt, container, false);
        
        // initialize and load data into ArrayList
        ArrayList<GroupItem> grpItemArray = new ArrayList<GroupItem>(6);
        
        if (status == 3) // status: completed (display saved data)
        {
        	GradingDB gradeDb = new GradingDB(rootView.getContext());
        	gradeDb.openDataBase();
        	Grading gradeRecd = gradeDb.getGrading(getSessionIdToken());
        	if (gradeRecd != null)
        	{
	        	qnAns[0] = gradeRecd.getQn1Ans();
	        	qnAns[1] = gradeRecd.getQn2Ans();
	        	qnAns[2] = gradeRecd.getQn3Ans();
	        	qnAns[3] = gradeRecd.getQn4Ans();
	        	qnAns[4] = gradeRecd.getQn5Ans();
	        	qnAns[5] = gradeRecd.getQn6Ans();
        	}
        }
        
        for (int i=0; i<6; i++)
        {
        	GroupItem item = new GroupItem();
        	if (status == 3) // status: completed (display saved data)
        		item.setValue(qnAns[i]);
        	
        	grpItemArray.add(item);
        }
        
        // initialize adapter to load data
        adapter = new CustomExpandListAdapter(this.getActivity(), grpItemArray,status);
        gradeListView = (ListView) rootView.findViewById(R.id.gradeList);
        gradeListView.setAdapter(adapter);
        

		// hide the submit button :D
		((Button) rootView.findViewById(R.id.submitBtn))
			.setVisibility(View.GONE);
        
        setRetainInstance(true);
        
        return rootView;
	}
	
	public int getSessionIdToken()
	{
		SessionDB sessionDb = new SessionDB(rootView.getContext());
		sessionDb.openDataBase();
		int sessionId = sessionDb.getSession(profileId).getSessionId();
		Log.i("Session Id:",sessionId+"");
		return sessionId;
	}
	
	public boolean proceed() {
		return adapter.validateForm();
	}
	
	public Grading getFdBackForm() {
	
		dataList = adapter.getDataList();
		
		fdbackForm.setQn1Ans(dataList.get(0).getValue());
		fdbackForm.setQn2Ans(dataList.get(1).getValue());
		fdbackForm.setQn3Ans(dataList.get(2).getValue());
		fdbackForm.setQn4Ans(dataList.get(3).getValue());
		fdbackForm.setQn5Ans(dataList.get(4).getValue());
		fdbackForm.setQn6Ans(dataList.get(5).getValue());
		
		return fdbackForm;
	}
}