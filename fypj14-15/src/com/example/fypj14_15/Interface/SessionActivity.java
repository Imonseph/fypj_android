package com.example.fypj14_15.Interface;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.fypj14_15.R;
import com.example.fypj14_15.Database.GradingDB;
import com.example.fypj14_15.Database.ProfilerDB;
import com.example.fypj14_15.Database.SessionDB;
import com.example.fypj14_15.Model.Grading;
import com.example.fypj14_15.Model.Session;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class SessionActivity extends FragmentActivity 
							implements ActionBar.TabListener, TimerFragment.updateFragmentView{

	private static int profileid;
	private static int status = 0;
	static String[] statusList = new MainActivity().statusList;
	private Activity session = this;
	
	/**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
    
    static String tabHeader[] = new String[]{"Profile", "Timer", "Grading"};
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_main);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should show on "Up" caret, indicating that touching the button
        // will take the user one step up in the application's hierarchy.
        actionBar.setHomeButtonEnabled(true);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        
        Intent intent = getIntent();
        profileid = intent.getIntExtra("profileid", -1);
        for (int i=1; i<statusList.length; i++)
        {
        	if (intent.getStringExtra("status").equals(statusList[i]))
        	{
        		status = i;
        	}
        }
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	switch (item.getItemId()) {
	    	case R.id.action_settings:
	            return true;
	    	case android.R.id.home:
	    		/**
	    		 * This is called when Home (Up) button is pressed in the action bar.
	    		 * Create a simple intent that starts the hierarchical parent activity and
	    		 * see NavUtils in the Support Package to ensure proper handling of Up/Back action.
	    		 **/
	    		Intent upIntent = new Intent(this, MainActivity.class);
	    		if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
	    			// This activity is not part of the application's task, so create a new task with a synthesized back stack.
	    			TaskStackBuilder.create(this).addNextIntent(upIntent).startActivities();
	    		} else {
	    			// This activity is part of the application's task, so simply navigate up to the hierarchical parent activity
	    			NavUtils.navigateUpTo(this, upIntent);
	    		}
	    		return true;
    		default:
    			return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new ChildViewFragment(profileid);
                case 1:
                	return new TimerFragment(profileid,status);
                case 2:
                	return new GradingFragment(profileid,status);
                default:
                    return new ChildViewFragment(profileid);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	// return the tab section header
            return tabHeader[position];
        }
    }
    
    @Override
	public void onBackPressed()
	{
    	if (status == 2)
    	{
			// use an AlertDialog to prompt of user actions: leave and save current session?
			new AlertDialog.Builder(this)
	    	.setTitle(R.string.alertmsg_leave)
	    	.setSingleChoiceItems(R.array.saveOption, 1, null)
	    	.setPositiveButton(R.string.alertmsg_accept, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                // User clicked OK button; check for saveB4exit qualifier
	            	int saveB4exit = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
	            	
	            	if (saveB4exit == 1) { // if status: paused (timer started, and counting)
	            	
	            		TimerFragment fragmt = (TimerFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+1);
	            		// instantiate Session instance before thinking of getting session
	            		fragmt.updateSession(); // update timePaused value and current timeStamp in session object
	        			
	        			// check if an existing record in database
	        			SessionDB sessionDb = new SessionDB(getApplicationContext());
	        			sessionDb.openDataBase();
	        			Session sessionRecd = sessionDb.getSession(profileid);
	        			// if yes: calls method for updating record in database
	        			if (sessionRecd != null)
	        			{
	        				sessionDb.updateSession(fragmt.getSession());
	        				Log.i("Session -","has been saved (updated)");
	        			}
	        			// if no: calls method for inserting new record into the database
	        			else {
	        				sessionDb.newSession(fragmt.getSession());
	        				Log.i("Session -","has been saved (created)");
	        			}
	        			Toast.makeText(getApplicationContext(), "Your session has been saved successfully'.", Toast.LENGTH_SHORT).show();
	        			finish();
	            	}
	            	else
	            	{
	            		dialog.dismiss();
	            	}
	            }
	        })
	    	.setNegativeButton(R.string.alertmsg_revert, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                // User cancelled the dialog; do nothing
	            }
	        })
	    	.setCancelable(false).create().show();
    	}
    	// http://developer.android.com/guide/topics/ui/dialogs.html#AlertDialog
	}
    
    /**
     * This method will be used as an interface for any of the encapsulated fragments of this activity
     * @param position, smoothScroll
     */
    public void getFragmentView(int item, boolean smoothScroll)
    {
    	mViewPager.setCurrentItem(item, smoothScroll);
    }
    
    public void onFormSubmit (View view)
    {
    	new AlertDialog.Builder(this).setTitle(R.string.alertmsg_title)
    	.setMessage(R.string.alertmsg_submit)
    	.setPositiveButton(R.string.alertmsg_accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button; submit form by inserting into database
            	if (SaveFeedback(1)) // step 1: check to see if validation passes
            	{
            		SaveSession();
            		SaveFeedback(2);
            		getFragmentView(profileid,"Completed");
            		Toast.makeText(getApplicationContext(), "New Session with feedback grading created", Toast.LENGTH_SHORT).show();
            		Intent intent = new Intent(session, MainActivity.class);
            		startActivity(intent);
            		finish();
            	}
            }
        })
    	.setNegativeButton(R.string.alertmsg_revert, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog; do nothing
            }
        }).setCancelable(false).create().show();
    	// http://developer.android.com/guide/topics/ui/dialogs.html#AlertDialog
    }
    
    public int getProfileId() {
    	return profileid;
    }
    
    public void getFragmentView(int profileId,String newStatus) {
    
    	// update profile status
    	ProfilerDB profileDb = new ProfilerDB(getApplicationContext());
    	profileDb.openDataBase();
    	profileDb.updateProfileStatus(profileId, newStatus);
    	
    	// refresh ChildViewFragment view for new profile status
    	// getting desired fragment via fragmentManager: "android:switcher:" + viewId + ":" + index;
    	ChildViewFragment fragmt = (ChildViewFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+0); // childViewFrag id = 0
    	if (fragmt != null){
    		fragmt.updateViewSet(profileId);
    		mAppSectionsPagerAdapter.notifyDataSetChanged();
    		Log.i("Status:","Notify-DataSet-Changed");
    		if (newStatus.equalsIgnoreCase("Completed"))
    			Toast.makeText(this, "Status 'Paused' has been updated to 'Completed'.", Toast.LENGTH_SHORT).show();
    		else if (newStatus.equalsIgnoreCase("Paused"))
    			Toast.makeText(this, "Status 'Not Observed' has been updated to 'Paused'.", Toast.LENGTH_SHORT).show();
    	}
    	else{
    		Log.i("Status:","Fragment cannot be found");
    	}
    }
    
    private int sessionId = 0;
    
	public void SaveSession()
    {
    	TimerFragment fragmt = (TimerFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+1); // TimerFragment id =1;
    	fragmt.updateSession(); // instantiate Session instance before thinking of getting session
    	Session saveSession = fragmt.getSession();
    	SessionDB sessionDb = new SessionDB(getApplicationContext());
    	sessionDb.openDataBase();
    	switch(status)
    	{
    	case 1: // status: Not Observed
    		// Save session by inserting directly into database
    		sessionDb.newSession(saveSession);
    		break;
    	case 2: // status: Paused
    		// Save session by updating the session record from database
    		// don't forget to update timestamp :)
    		saveSession.setLastDated(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date()).toString());
    		sessionDb.updateSession(saveSession);
    		break;
		default:
			break;
    	}
    	
    	// and don't forget to get the sessionId from saveSession
    	sessionId = saveSession.getSessionId(); // to be used later for saveFeedback();
    	sessionDb.close();
    }
    
    public boolean SaveFeedback(int step)
    {
    	GradingFragment fragmt = (GradingFragment) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+2);
    	
    	switch(step)
    	{
    	case 1:
    		return fragmt.proceed();
    	case 2:
    		Grading fdback = fragmt.getFdBackForm();
        	fdback.setSessionID(sessionId); // collect the sessionId from the previous transaction
        	GradingDB gradeDb = new GradingDB(getApplicationContext());
        	gradeDb.openDataBase();
        	gradeDb.newGrades(fdback);
        	gradeDb.close();
    		break;
    	}
		return true;
    }
}
