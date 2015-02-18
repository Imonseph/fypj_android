package com.example.fypj14_15.Interface;

import com.example.fypj14_15.R;
import com.example.fypj14_15.Database.ProfilerDB;
import com.example.fypj14_15.Model.Profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public  class ChildViewFragment extends Fragment {

	private View rootView;
	
	private int profileid;
	
	public ChildViewFragment(int profileid){
		this.profileid = profileid;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       
		rootView = inflater.inflate(R.layout.childetail_fragmt, container, false);
		ProfilerDB profileDb = new ProfilerDB(rootView.getContext());
		profileDb.openDataBase();
		Profile profile = profileDb.getProfile(profileid);
        
		((TextView)rootView.findViewById(R.id.textViewID)).setText(String.valueOf(profileid));
		((TextView)rootView.findViewById(R.id.textViewName)).setText(profile.getName());
		((TextView)rootView.findViewById(R.id.textViewGender)).setText(profile.getGender());
		((TextView)rootView.findViewById(R.id.textViewStatus)).setText(profile.getStatus());
		((TextView)rootView.findViewById(R.id.textViewPriDiag)).setText(profile.getPriDiagnos());
		((TextView)rootView.findViewById(R.id.textViewSecDiag)).setText(profile.getSecDiagnos());
		((TextView)rootView.findViewById(R.id.textViewRemarks)).setText(profile.getRemarks());
		((TextView)rootView.findViewById(R.id.textViewInspector)).setText(profile.getInspector());
		((TextView)rootView.findViewById(R.id.textViewVenue)).setText(profile.getVenue());
		((TextView)rootView.findViewById(R.id.textViewActivity)).setText(profile.getActivity());
		((TextView)rootView.findViewById(R.id.textViewNoAdults)).setText(profile.getAdultNo() + "");
		((TextView)rootView.findViewById(R.id.textViewNoChildren)).setText(profile.getChildrenNo() + "");
		
        
        Button imgBtn = (Button) rootView.findViewById(R.id.buttonStart);
        imgBtn.setOnClickListener(new View.OnClickListener() {
		
			@Override
			public void onClick(View view) {
				((SessionActivity)getActivity()).getFragmentView(1, true);
			}
		});
        
        return rootView;
    }
	
	public void updateViewSet(int profileId)
	{
		Profile profile = (new ProfilerDB(rootView.getContext())).getProfile(profileid);		
		((TextView) rootView.findViewById(R.id.textViewStatus)).setText(profile.getStatus());
	}
}
