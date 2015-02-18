package com.example.fypj14_15.Database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fypj14_15.Model.Profile;

public class ProfilerDB extends SQLiteOpenHelper{

	//The Android's default system path of your application database.
	private static String DB_PATH = "";
 
    private static String DB_NAME = "ObservationDB";
 
    private SQLiteDatabase myDataBase; 
    
    private Context myContext;
	
	public ProfilerDB(Context context) {
		super(context, DB_NAME, null, 1);
		DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		Log.i("DB_PATH:",context.getApplicationInfo().dataDir.toString());
		myContext = context;
	}
	 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
    		this.getWritableDatabase();
 
        	try {
    			copyDataBase();
    			
    		} catch (IOException e) {
        		throw new Error("Error copying database");
        	}
    	}
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
    		//database does't exist yet.
    	}
 
    	if(checkDB != null){	 
    		checkDB.close();
     	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }

	
	public void openDataBase() throws SQLException{
		 
		//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	// Main CRUD (Create,Read,Update,Delete) functions
	
	// INSERT new Profile data into SQLiteDatabase
	public boolean newProfile(Profile profile) throws SQLException{
	
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues colmValues = new ContentValues();
		Log.d("Reading:","Profile data to DataBuffer");
		
		colmValues.put(profile.getSingleColumn(1), profile.getName());
		colmValues.put(profile.getSingleColumn(2), profile.getGender());
		colmValues.put(profile.getSingleColumn(3), profile.getPriDiagnos());
		colmValues.put(profile.getSingleColumn(4), profile.getSecDiagnos());
		colmValues.put(profile.getSingleColumn(5), profile.getRemarks());
		colmValues.put(profile.getSingleColumn(6), profile.getInspector());
		colmValues.put(profile.getSingleColumn(7), profile.getVenue());
		colmValues.put(profile.getSingleColumn(8), profile.getActivity());
		colmValues.put(profile.getSingleColumn(9), profile.getAdultNo());
		colmValues.put(profile.getSingleColumn(10), profile.getChildrenNo());
		colmValues.put(profile.getSingleColumn(11), profile.getStatus());
		
		Log.d("Inserting:" ,"Profile record from DataBuffer to Database");
		try{
			// Inserting Data Buffer Reader
	        db.insert("Profile", null, colmValues);
	        db.close(); // Closing database connection
	        Log.d("DB Status:","Success! New Profile created in ObservationDB!");
			return true;
		}
		catch(SQLException ex){
		Log.d("DB Error trace:",ex.toString());
			return false;
		}
	}
	
	// VIEW single Profile record from SQLiteDatabase
	public Profile getProfile(int id) throws SQLException {
	
		SQLiteDatabase db = this.getReadableDatabase();
		Profile record = new Profile();
		Log.d("Reading:", "Profile record from Database to DataReader");
		try{
			// query(table,columns,selection,selectionArgs,GroupBy,Having,OrderBy)
			Cursor cursor = db.query("Profile", record.getAllColumns(), "childID=?",
					new String[] { String.valueOf(id) }, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();
			Log.d("DB Status:","Success! 1 Profile record read from ObservationDB!");
			record = new Profile(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), 
					cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10), cursor.getString(11));
			Log.d("Transferring:","record from DataReader to Profile object");
		}
		catch(SQLException ex) {
			Log.d("DB Error trace:",ex.toString());
		}
		return record;
	}
	
	// READ multiple Profile records from SQLiteDatabase
		public ArrayList<Profile> getAllProfile() throws SQLException {
		
			ArrayList<Profile> profileList = new ArrayList<Profile>();
			
			SQLiteDatabase db = this.getWritableDatabase();
			// select query, Order By child names
			Cursor cursor = db.query("Profile", new Profile().getAllColumns(), null, null, null, null, new Profile().getSingleColumn(0));
			
			try{
				int rowCount = 0;
				if (cursor.moveToFirst()) {
					Log.d("Reading:","Multiple Profile record from Database to DataReader");
					do {
						Profile profile = new Profile(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), 
								cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10), cursor.getString(11));
						profileList.add(profile);
						rowCount ++;
					
					} while(cursor.moveToNext());
				}
				Log.d("DB Status:","Success! " + String.valueOf(rowCount) + " Profile record read from ObservationDB!");
				
			} catch(SQLException ex) {
				Log.d("DB Error trace:",ex.toString());
			}
			return profileList;
		}
		
		public ArrayList<Profile> getAllProfile(String status) throws SQLException {
			
			ArrayList<Profile> profileList = new ArrayList<Profile>();
			
			SQLiteDatabase db = this.getWritableDatabase();
			
			// data query: select * from Profile <all columns> 
			Cursor cursor = db.query("Profile", new Profile().getAllColumns(), "status=?", new String[]{status},
					null, null, new Profile().getSingleColumn(0));
			
			if (cursor.moveToFirst()) {
				do {
					Profile profile = new Profile(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), 
							cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10), cursor.getString(11));
					profileList.add(profile);
				
				} while(cursor.moveToNext());
			}
			return profileList;
		}
	
	// UPDATE a Profile record from SQLiteDatbase
	public boolean updateProfile(Profile profile) throws SQLException {
	
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Log.d("Reading:","Profile data to DataBuffer");
		
		// load columnValues
		values.put(profile.getSingleColumn(1), profile.getName());
		values.put(profile.getSingleColumn(2), profile.getGender());
		values.put(profile.getSingleColumn(3), profile.getPriDiagnos());
		values.put(profile.getSingleColumn(4), profile.getSecDiagnos());
		values.put(profile.getSingleColumn(5), profile.getRemarks());
		values.put(profile.getSingleColumn(6), profile.getInspector());
		values.put(profile.getSingleColumn(7), profile.getVenue());
		values.put(profile.getSingleColumn(8), profile.getActivity());
		values.put(profile.getSingleColumn(9), profile.getAdultNo());
		values.put(profile.getSingleColumn(10), profile.getChildrenNo());
		values.put(profile.getSingleColumn(11), profile.getStatus());
		
		Log.d("Updating:","Profile record from DataBuffer to Database");
		try{
			// update(table,columnValues,whereClause,whereArgs)
			int updateRecd = db.update("Profile", values, "childID=?", new String[]{String.valueOf(profile.getId())});
			Log.d("DB Status:","Success! '" +updateRecd+ "' Profile record updated in ObservationDB!");
			return true;
		}
		catch(SQLException ex) {
			Log.d("DB Error trace:",ex.toString());
			return false;
		}
	}
	
	public boolean updateProfileStatus(int profileId, String status) throws SQLException {
	
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		Log.d("Reading:","Profile status to DataBuffer - '" + status + "'");
		
		// load the columnValues that needs updating (status)
		values.put(new Profile().getSingleColumn(11), status);
		
		Log.d("Updating:","Profile record from DataBuffer to Database");
		try{
			// update(table,columnValues,whereClause,whereArgs)
			int updateRecd = db.update("Profile", values, "childID=?", new String[]{String.valueOf(profileId)});
			Log.d("DB Status:","Success! '" +updateRecd+ "' Profile record updated in ObservationDB!");
			return true;
		}
		catch(SQLException ex) {
			Log.d("DB Error trace:",ex.toString());
			return false;
		}
	}
	
	// Getting Profile count
	public int getProfileCount(){
	
		String countQuery = "SELECT  * FROM Profile";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        
        // return count
        return cursor.getCount();
	}
}
