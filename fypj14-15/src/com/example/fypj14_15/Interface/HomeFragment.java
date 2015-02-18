package com.example.fypj14_15.Interface;

import java.io.IOException;
import java.util.ArrayList;

import com.example.fypj14_15.R;
import com.example.fypj14_15.Controllers.ListAdapter;
import com.example.fypj14_15.Database.ProfilerDB;
import com.example.fypj14_15.Model.ListItem;
import com.example.fypj14_15.Model.Profile;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class HomeFragment extends Fragment {
	
	private ArrayList<ListItem> childList;
	
	private String status;
	
	public HomeFragment(String status) {
		this.status = status;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		// Implements adapter for list display of data
		ListAdapter adapter = new ListAdapter(this.getActivity(),generateData(rootView));
		// 2. Get ListView from fragment_main.xml
		ListView listView = (ListView) rootView.findViewById(R.id.listView);
		// 3. setListAdapter
		listView.setAdapter(adapter);
		//Referenced from: http://stackoverflow.com/questions/20311822/nullpointer-exception-when-using-listview-in-fragment
		
		// register view for context menu
		registerForContextMenu(listView);
		
		//*4. add background color change onClick event of selected listItem
		listView.setOnItemClickListener(new OnItemClickListener(){
		
		int pos=0; int save=-1;
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.setBackgroundColor(Color.LTGRAY);
				
				int profileID = Integer.parseInt(childList.get(position).getId());
				status = childList.get(position).getStatus();
				
	            if (pos == 0) {
	                if (save != -1) {
	                    parent.getChildAt(save).setBackgroundColor(Color.TRANSPARENT);
	                }
	                save = position;
	                pos++;
	                Log.d("Pos = 0", "Running");

	            } else {
	                parent.getChildAt(save).setBackgroundColor(Color.TRANSPARENT);
	                save = position;
	                pos = 0;
	                Log.d("Pos # 0", "Running");
	            }
	            // transferring control to another activity from this fragment activity
	            Intent intent = new Intent(getActivity(), SessionActivity.class);
				intent.putExtra("profileid", profileID);
				intent.putExtra("status", status);
				// Intent to include params: COntext and target class activity redirected
				startActivity(intent);
			}
		});
		
		// Referenced from: http://stackoverflow.com/questions/2217753/changing-background-color-of-listview-items-on-android
		
		ImageButton imgBtn = (ImageButton) rootView.findViewById(R.id.addNew_Btn);
		// this method is called when addNew_Btn is clicked
		imgBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				// this method is called when addNew_Btn is clicked
				Intent intent = new Intent(getActivity(), ChildFormActivity.class);
				// Intent to include params: COntext and target class activity redirected
				startActivity(intent);
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		// provide contextual menus for action bar or view element items
	    super.onCreateContextMenu(menu, v, menuInfo);
	    menu.setHeaderTitle("Select the Action");
	    menu.add(0, v.getId(), 0, "View");
	    menu.add(0, v.getId(), 0, "Edit");
	    menu.add(0, v.getId(), 0, "Delete");
	}
	
	@Override  
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        
        //  info.position will give the index of selected item  
    	if(item.getTitle()=="View")
        {
               // Code to execute when clicked on This Item
        }
        else if(item.getTitle()=="Edit")
        {
           
          // Code to execute when clicked on This Item
        }
        else 
        {
            return false;
        }  
        return true;
    }
	// source: http://www.learn-android-easily.com/2013/01/creating-context-menu-in-android.html


	private ArrayList<ListItem> generateData(View view)
	{	
		ProfilerDB profileDb = new ProfilerDB(view.getContext());		
		childList = new ArrayList<ListItem>();
		ArrayList<Profile> profileList = new ArrayList<Profile>();
		
		try {
			profileDb.createDataBase(); // creates the instance of database on mobile
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		profileDb.openDataBase();
		if (status.equalsIgnoreCase("default"))
			profileList = profileDb.getAllProfile();
		else
			profileList = profileDb.getAllProfile(status);
		for (int i=0; i<profileList.size(); i++) {
			Profile thisProfile = profileList.get(i);
			if (thisProfile.getGender().equalsIgnoreCase("male"))
			{
				childList.add(new ListItem(R.drawable.male_avatar,thisProfile.getName(),String.valueOf(thisProfile.getId()), thisProfile.getStatus()));
			}
			else if(thisProfile.getGender().equalsIgnoreCase("female"))
			{
				childList.add(new ListItem(R.drawable.female_avatar,thisProfile.getName(),String.valueOf(thisProfile.getId()), thisProfile.getStatus()));
			}
		}
		return childList;
	}
}
