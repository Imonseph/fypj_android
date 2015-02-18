package com.example.fypj14_15.Interface;

import com.example.fypj14_15.R;
import com.example.fypj14_15.Database.ProfilerDB;
import com.example.fypj14_15.Model.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;	
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ChildFormActivity extends Activity{
	
	Spinner spinnerDropDown;
	Spinner spinnerDropDown1;
	Spinner spinnerDropDown2;
	String[] venueSpinner = {
			"Ngee Ann Child Centre",
			"Bedok North Care Centre",
			"Eng Neo Child Centre",
			"Alekandra Hospital",
			"Stamford Junior school",
	 
	};
	 
	 String[] activitySpinner = {
			"Hearing Test",
			"Interaction Screening",
			"Sensory-motor Evaluation",
			"Cognitive Evaluation Test",
			"Interest Screening Test",
			"Adaptive Function Assessment",
			"Adaptive Behaviour Screening Test",
			 
	 };
	 
	 String[] genderSpinner = {
				"Male",
				"Female",	 
		 };
	 
	 private Button addNewBtn;
	 private Editable name, priDiagnos, secDiagnos, remarks, inspector, adultsNo, childrenNo;
	 private int childID=0;
	 String status ="Not Observed";
	 private String gender, venue, activity;
	 private Context context;
	 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.childinfo_form);
		 context = this.getApplicationContext();
 
		 // Get reference of SpinnerView from layout/main_activity.xml
		 
		 // for venue
		 spinnerDropDown = (Spinner)findViewById(R.id.spinner1);
		 
		 ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
		 R.layout.simple_spinner_dropdown_item ,venueSpinner);
		 
		 spinnerDropDown.setAdapter(adapter);
	 
		 // for activity
		spinnerDropDown1 = (Spinner)findViewById(R.id.spinner2);
		
		ArrayAdapter<String> adapter1= new ArrayAdapter<String>(this,android.
		R.layout.simple_spinner_dropdown_item ,activitySpinner);
		
		spinnerDropDown1.setAdapter(adapter1);
		
		// for gender
		spinnerDropDown2 = (Spinner)findViewById(R.id.spinner3);

		ArrayAdapter<String> adapter2= new ArrayAdapter<String>(this,android.
		R.layout.simple_spinner_dropdown_item ,genderSpinner);
		
		spinnerDropDown2.setAdapter(adapter2);
		
		//reminder of maximum number limit for no_adults/children
//		no_adults.setOnClickListener(new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//		Toast msg = Toast.makeText(getBaseContext(), "9 People MAX", Toast.LENGTH_LONG);
//		msg.show();
//		}
//		});
//		no_children.setOnClickListener(new OnClickListener() {
//		@Override
//		public void onClick(View v) {
//		Toast msg = Toast.makeText(getBaseContext(), "9 People MAX", Toast.LENGTH_LONG);
//		msg.show();
//		}
//		});
		spinnerDropDown.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				venue = parent.getItemAtPosition(position).toString();
				// Toast.makeText(parent.getContext(), "Venue: " + venue, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		spinnerDropDown1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				activity = parent.getItemAtPosition(position).toString();
				// Toast.makeText(parent.getContext(), "Activity: " + activity, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}
		});
		spinnerDropDown2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				gender = parent.getItemAtPosition(position).toString();
				// Toast.makeText(parent.getContext(), "Gender: " + gender, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
	 
		//childID = ((EditText) findViewById(R.id.editTextid)).getText();
		name = ((EditText) findViewById(R.id.editTextname)).getText();
		priDiagnos = ((EditText) findViewById(R.id.editTextpridiag)).getText();
		secDiagnos = ((EditText) findViewById(R.id.editTextsecdiag)).getText();
		remarks = ((EditText) findViewById(R.id.editTextremarks)).getText();
		inspector = ((EditText) findViewById(R.id.editTextinspector)).getText();
		adultsNo = ((EditText) findViewById(R.id.editTextadult)).getText();
		childrenNo = ((EditText) findViewById(R.id.editTextchildren)).getText();
		
		addNewBtn = (Button) findViewById(R.id.button1);
		addNewBtn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				
				EditText childName = (EditText)findViewById(R.id.editTextname);
				EditText priDiag = (EditText)findViewById(R.id.editTextpridiag);
				EditText inspectors = (EditText)findViewById(R.id.editTextinspector);
				EditText adults = (EditText)findViewById(R.id.editTextadult);
				EditText children = (EditText)findViewById(R.id.editTextchildren);
				
				String adultValidate;
				String childValidate;
				adultValidate = adults.getText().toString();
				childValidate = children.getText().toString();
				
				 if(childName.getText().toString().length() == 0 )
					childName.setError( "Child's Name is required." );
				 if(priDiag.getText().toString().length() == 0 )
					priDiag.setError( "Diagnosis is required." );
				 if(inspectors.getText().toString().length() == 0 )
					inspectors.setError( "Inspector Name is required." );
				 
				 if(adults.getText().toString().length() == 0 && adultValidate != "0" )
					adults.setError( "Number of Adult is required. Max:9" );
				 
				
//				 if (adultValidate != "0" )
//						adults.setError( "Please input numbers 1 - 9." );
				 
				 
				 if(children.getText().toString().length() == 0 && childValidate != "0" )
						children.setError( "Number of Children is required. Max:9" );
				 
//				 if(childValidate != "0" )
//					children.setError( "Please input numbers 1 - 9." ); 
				 
				
				else {
					Profile myprofile = new Profile(
							childID, name.toString(), gender.toString(), priDiagnos.toString(), secDiagnos.toString(), 
							remarks.toString(), inspector.toString(), venue.toString(), activity.toString(), 
							Integer.parseInt(adultsNo.toString()),Integer.parseInt(childrenNo.toString()), status);
					
					boolean id;
					ProfilerDB db = new ProfilerDB(context);
					db.openDataBase();
					id = db.newProfile(myprofile);
					db.close();
					
					Toast.makeText(context, "Child Information Recorded.", Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent (getApplicationContext(), MainActivity.class);
					startActivity(intent);
				}
					
				
	
			}
		});
	}
	 
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
