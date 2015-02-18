package com.example.fypj14_15.Database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.fypj14_15.Model.Grading;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GradingDB extends SQLiteOpenHelper{

	//The Android's default system path of your application database.
	private static String DB_PATH = "";
 
    private static String DB_NAME = "ObservationDB";
 
    private SQLiteDatabase myDataBase; 
    
    private Context myContext;
	
	public GradingDB(Context context) {
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
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
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
	
	// INSERT new Grading data into SQLiteDatabase
	
	public boolean newGrades(Grading grades) {
	
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues colmValues = new ContentValues();
		Log.d("Reading:","Grading data to DataBuffer");
		
		colmValues.put(grades.getSingleColumn(1), grades.getSessionID());
		colmValues.put(grades.getSingleColumn(2), grades.getQn1Ans());
		colmValues.put(grades.getSingleColumn(3), grades.getQn2Ans());
		colmValues.put(grades.getSingleColumn(4), grades.getQn3Ans());
		colmValues.put(grades.getSingleColumn(5), grades.getQn4Ans());
		colmValues.put(grades.getSingleColumn(6), grades.getQn5Ans());
		colmValues.put(grades.getSingleColumn(7), grades.getQn6Ans());
		
		Log.d("Inserting:", "Grading record from DataBuffer to Database");
		try{
			// Inserting Data Buffer Reader
			db.insert("Grading", null, colmValues);
	        db.close(); // Closing database connection
	        Log.d("DB Status:","Success! New Grading created in ObservationDB!");
			return true;
		}
		catch(SQLException ex){
		Log.d("DB Error trace:",ex.toString());
			return false;
		}
	}
	
	// VIEW a Grading from SQLiteDatabase
	public Grading getGrading(int id1) throws SQLException {
	
		SQLiteDatabase db = this.getReadableDatabase();
		Grading fdback = null;
		Log.d("Reading:","Grading record from Database to DataReader");
		try{
			// query(table,columns,selection,selectArgs,GroupBy,Having,OrrderBy)
			Cursor cursor = db.query("Grading", new Grading().getAllColumns(), new Grading().getSingleColumn(1) +"=?",
					new String[]{String.valueOf(id1)}, null, null, null);
			if (cursor != null && cursor.moveToFirst())
			{
				Log.d("DB Status:","Success! A Grading record read from ObservationDB!");
				fdback = new Grading(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),cursor.getInt(3),
						cursor.getInt(4),cursor.getInt(5),cursor.getInt(6),cursor.getInt(7));
				Log.d("Transferring:","record from DataReader to Grading object");
			}
		}
		catch(SQLException ex) {
			Log.d("DB Error trace:",ex.toString());
		}
		return fdback;
	}
	
	// Getting Grade count
	public int getGradinCount() {
	
		String countQuery = "SELECT  * FROM Grading";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        
        // return count
        return cursor.getCount();
	}
}
