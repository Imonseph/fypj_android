package com.example.fypj14_15.Interface;

import com.example.fypj14_15.R;
import com.example.fypj14_15.Database.SessionDB;
import com.example.fypj14_15.Model.Session;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class TimerFragment extends Fragment {

	// GLOBAL VARIABLES
	private updateFragmentView fragmtView;
	
	// provide basis reference of this View
	private View rootView;
	private TextView timerTextView;
	private TextView timeLimitText;
	
	private Button[] engagemtOptnBtns = new Button[4];
	private Button flagBtn;
	private Button playPauseBtn;
	private Button stopResetBtn;
	private CheckBox chkbx;
	private EditText field;
	private RadioGroup radioBtns;
	boolean visibleBtn = false;
	
	// declare variables for timer	
	private int minutes; // will be widely used across 2-3 different 
	private int seconds; // methods / functions converting time
	
	private long startTime = 0L; // default when timer starts
	private long updateTime = 0L; // stores time after pause begins
	private long pasTime = 0L; // stores time when paused
	private long millisec = 0L;
	private long timeLimit = 20*60*(10^3); // stores the time limit for this session (default 10sec = 1200k milliSec)
	private long excessTime = 0L; // stores time when exceeded the timeLimit
	private int flagCount = 0;
	
	// data storage variables
	private Session thisSession;
	private int profileId;
	private int sessionId = 0, engmtIndex = -1, prompt = 1, isGraded = 0;
	private String condition = "";
	private String dateRecd = "";
	private int status;
	
	// runs without a timer by reposting this handler at the end of the runnable
	public int[] getTimeValues(long time)
	{
		int seconds = (int) (time / 1000);
		int minutes = seconds / 60;
		seconds     = seconds % 60;
		return new int[]{minutes,seconds};
	}
	
	public interface updateFragmentView{
		public void getFragmentView(int profileId,String newStatus);
	}
	
	// for alarm when timer flashes at timeLimit
		Vibrator alarm;
	
	Handler timerHandler = new Handler();
	
	Runnable timerRunnable = new Runnable(){

		@Override
		public void run() {
			millisec = System.currentTimeMillis() - startTime;
			
			// to update with pause time
			updateTime = pasTime + millisec;
			minutes = getTimeValues(updateTime)[0];
			seconds = getTimeValues(updateTime)[1];
			
			timerTextView.setText(String.format("%d:%02d", minutes, seconds));
			timerHandler.postDelayed(this, 0);
			
			if (updateTime >= timeLimit)
			{
				excessTime = updateTime - timeLimit;
				minutes = getTimeValues(timeLimit)[0];
				seconds = getTimeValues(timeLimit)[1];
				
				timerTextView.setText(String.format("%d:%02d", minutes, seconds));
				
				alarm.vibrate(excessTime);
				
				int timeCheck = getTimeValues(excessTime)[1];
				if (timeCheck % 2 == 1)
					timerTextView.setTextColor(Color.BLACK);
				else
					timerTextView.setTextColor(Color.RED);				
			}
		}
	};
	
	public TimerFragment(int profileId, int status){
	
		this.profileId = profileId;
		this.status = status;
	}
	
	@Override
	public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
        	fragmtView = (updateFragmentView) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement updateFragmentView");
        }
    }
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
        rootView = inflater.inflate(R.layout.timer_fragmt, container, false);
        
        timerTextView = (TextView) rootView.findViewById(R.id.timerView);
        timeLimitText = (TextView) rootView.findViewById(R.id.flag_text);
        
        // Initialize 4 Engagement button options
        int[] engagemtBtnIDs = new int[]{R.id.actEngage_btn,R.id.actNonEngage_btn,R.id.pasvEngage_btn,R.id.pasvNonEngage_btn};
        for (int i=0; i<4; i++) {
        	final int index = i;
        	engagemtOptnBtns[i] = (Button) rootView.findViewById(engagemtBtnIDs[i]);
        	engagemtOptnBtns[i].setTag(index); // set buttons with id tag identifiers
        	engagemtOptnBtns[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					Button option = (Button) view;
			    	int tag = Integer.parseInt(view.getTag().toString()); // get the index
			    	// set the current selected button to selected appearance
			    	option.setBackground(new Button(getActivity().getApplicationContext()).getBackground());
			    	option.setTextColor(Color.WHITE);
			    	
			    	if (engmtIndex >=0)
			    	{	// set the previous selected button to unselected appearance
			    		engagemtOptnBtns[engmtIndex].setBackground(new Button(rootView.getContext()).getBackground());
			    		engagemtOptnBtns[engmtIndex].setTextColor(Color.BLACK);
			    	}
			    	// check if same button was first selected before
			    	if (tag == engmtIndex)
			    		// unCheck this same button from selection
			    		engmtIndex = -1; // -1 is a good identifier to show nothing is selected
			    	else
			    		// set the current selected button to previous selected button for more callBacks
			    		engmtIndex = tag;
			    	
			    	// set the current tag on engagement as selectedIndex
			    	
				}
			});
        }
        
        // Initialize timer Buttons
        flagBtn = (Button) rootView.findViewById(R.id.flag_btn);
		playPauseBtn = (Button) rootView.findViewById(R.id.btnPlayPause);
		stopResetBtn = (Button) rootView.findViewById(R.id.btnStopReset);
		
		flagBtn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view) {
			
				Button thisBtn = (Button) view; // localize the button view within the handler
				
				if (flagCount <5)
				{
					// if time counter exceeds timeLimit
					if (updateTime > timeLimit){
						timerModes(1); timerModes(2); timerModes(3);
					} else{ // if time counter is less then timeLimit
						timeLimit = timeLimit + 28*60*(10^3); // add 15 secs interval to the original time limit
					}
					flagCount ++;
					// add visibility to flagCount on button
					thisBtn.setText("Flag ("+flagCount+")");
					// update the TextView of timeLimit
				}
				else
				{
					// stop the timer when flagCount exceeds 5
					timerModes(1);
					// reset the flagCount
					flagCount = 0;
					timeLimit = 20*60*(10^3); // reset timeLimit back to default
					thisBtn.setText(rootView.getResources().getString(R.string.flag));
				}
				
				timerModes(0);
			}
		});
		
		stopResetBtn.setVisibility(View.GONE);
		playPauseBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) 
			{
				Button btn = (Button) view;
				if (btn.getText().equals("Start"))
				{
					timerModes(3); // calls timerMode 3 - start time
					stopResetBtn.setVisibility(View.VISIBLE);
				}
				else if (btn.getText().equals("Stop"))
				{
					timerModes(1);
				}
				// On Start button pressed
				if (status == 1) {
					// status: Not Observed -> Paused
					fragmtView.getFragmentView(profileId, "Paused");
					status = 2; // manual update of status
				}
			}
		});
		
		stopResetBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) 
			{
				timerModes(4); // timer mode reset
				timerTextView.setTextColor(Color.BLACK);
				stopResetBtn.setVisibility(View.GONE);
				
				flagCount = 0; // reset the flagCount
				timeLimit = 20*60*(10^3); // reset timeLimit back to default
				flagBtn.setText(rootView.getResources().getString(R.string.flag));
				
				timerModes(0);
			}
		});
		
		chkbx = (CheckBox) rootView.findViewById(R.id.chkbox_yes);
		field = (EditText) rootView.findViewById(R.id.addText);
		chkbx.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				if (isChecked){
					prompt = 1;
					field.setVisibility(View.VISIBLE);
				}
				else {
					prompt = 0;
					field.setVisibility(View.GONE);
				}
			}
		});
		
		radioBtns = (RadioGroup)rootView.findViewById(R.id.radioGrp);
		for (int i=0; i<radioBtns.getChildCount();i++)
		{
			RadioButton rdbtn = (RadioButton) radioBtns.getChildAt(i);
			rdbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					condition = buttonView.getText().toString();
				}
			});
		}
		
		alarm = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		
		// this is where we begin to customize the layout from data result according to status of profile
		switch(status)
        {
        case 1: // when status: Not Observed
        	timerModes(0); // set and displays default timerLimitText
        	break;
        case 2:  // when status: Paused
        	displayPausedSession(1);
        	break;
        case 3: // when status: complete
        	displaySessionComplete();
        	break;
    	default:
        	break;
        }
		
		setRetainInstance(true);
		
        return rootView;
    }
	
	@SuppressLint("SimpleDateFormat")
	private void displayPausedSession(int step) {
    
		SessionDB sessionDb = new SessionDB(rootView.getContext());
		sessionDb.openDataBase();
		Session sessionRecd = sessionDb.getSession(profileId);
		sessionDb.close();
		if (sessionRecd != null)
		{
			sessionId = sessionRecd.getSessionId(); // store the currentSessionId
			if (step == 1){
				if (!sessionRecd.getTimePaused().isEmpty()) {	// set the TextViewTextTimer value
					String timeRecd = sessionRecd.getTimePaused();
					timerTextView.setText(timeRecd);
					// update the pasTime value for timerMode to work
					String[] timeValues = timeRecd.split(":"); // split by minutes and seconds
					pasTime = 120*(Integer.parseInt(timeValues[0])*60 + Integer.parseInt(timeValues[1]))*10^3;
					stopResetBtn.setVisibility(View.VISIBLE);
					timerModes(1);
				}
			}
			if (!sessionRecd.getEngagemt().isEmpty()) {  // set the engagement is/if selected button
				engmtIndex = sessionRecd.getEngagemtIndex();
				engagemtOptnBtns[engmtIndex].setBackground(new Button(getActivity().getApplicationContext()).getBackground());
				engagemtOptnBtns[engmtIndex].setTextColor(Color.WHITE);
			}
			if (sessionRecd.getPrompt() == 1) { // set the prompt checkBox is/if selected
				chkbx.setChecked(true);
				field.setVisibility(View.VISIBLE);
			}
			if (!sessionRecd.getCondition().isEmpty()) { // set the radioButton that is/if selected
				for (int i=0; i<radioBtns.getChildCount();i++)
				{
					RadioButton rdbtn = (RadioButton) radioBtns.getChildAt(i);
					if (rdbtn.getText().toString().equalsIgnoreCase(sessionRecd.getCondition()))
						rdbtn.setChecked(true);
				}
			}
			if (sessionRecd.getFlagCount() > 0) {
				// set the flagBtn text
				flagCount = sessionRecd.getFlagCount();
				flagBtn.setText("Flag ("+flagCount+")");
				// set the timeLimit text
				for (int i=0; i<sessionRecd.getFlagCount(); i++)
				{
					timeLimit = timeLimit + 28*60*(10^3);
				}
				timerModes(0);
			}
			
			Log.i("Timestamp:",sessionRecd.getLastDated());
			try {
				// format -> desired output sdf dateTransCoder("d MMM yyyy, hh:mm")
				// parse -> required input sdf dateTransCoder("yyyy-mm-dd hh:mm:ss")
				dateRecd = new SimpleDateFormat("EEE, d MMM yyyy, hh:mm").format(
						new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(sessionRecd.getLastDated()));
				Log.i("LastDated:",dateRecd);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			timerModes(0);
		}
    }
    
    private void displaySessionComplete() {
	
    	displayPausedSession(2);
    	// change timerTextView to display date completed
    	timerTextView.setText("Recorded on: " + dateRecd);
    	timerTextView.setTextSize(16);
    	timerTextView.setGravity(Gravity.START);
    	
    	// hide away the following:
    	playPauseBtn.setVisibility(View.GONE); 
    	stopResetBtn.setVisibility(View.GONE);
    	flagBtn.setEnabled(false); // set flagBtn disabled
	}

	private void timerModes(int num)
	{
		switch (num)
		{
			case 1: // 1) timer stop
				pasTime += millisec;
				timerTextView.setTextColor(Color.BLACK); // ensure that timer gets back to black text color
				timerHandler.removeCallbacks(timerRunnable); // prevents callback to updateTime in Runnable
				playPauseBtn.setText("Start");
				alarm.cancel();
				break;
			case 2: // 2) timer flag
				// configure changes to variables
				pasTime = updateTime - excessTime; // clock updated Time back to time limit instead of exceeded time
				timeLimit = timeLimit + 28*60*(10^3); // add 15 secs interval to the original time limit
				break;
			case 3: // 3) timer play
				startTime = System.currentTimeMillis();
				timerHandler.postDelayed(timerRunnable, 0);
				playPauseBtn.setText("Stop");
				break;
			case 4: // 4) timer reset
				timerHandler.removeCallbacks(timerRunnable);
				pasTime = 0; timerTextView.setText("0:00");
				playPauseBtn.setText("Start");
				break;
			default: // 0) displays update in timerLimit TextView
				minutes = getTimeValues(timeLimit)[0];
				seconds = getTimeValues(timeLimit)[1];
				timeLimitText.setText(String.format("%d:%02d", minutes, seconds));
				break;
		}
	}
	
	public void showSoftKeyboard(View view) {
	    if (view.requestFocus()) {
	    	
	        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	    }
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public Session getSession() {
    	return thisSession;
    }
    
    public void updateSession() {
    
    	// constructor: Session(sessionId,profileId,lastDated,engmt,prompt,condition,flagCount,timePaused,isGraded)
    	thisSession = new Session(sessionId,profileId,"","",prompt,condition,flagCount,"",isGraded);
    	// leave dateCreated (id=2) out since it's autoGen currentTimeStamp / will update later
    	thisSession.setEngagemt(engmtIndex);
    	thisSession.setPrompt(prompt);
    	thisSession.setTimePaused(timerTextView.getText().toString()); 
    }
}
