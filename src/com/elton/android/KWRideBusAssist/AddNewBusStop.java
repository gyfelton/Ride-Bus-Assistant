package com.elton.android.KWRideBusAssist;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewBusStop extends BaseActivity {

	//TODO duplicated declare of database
	private SQLiteDatabase mSQLiteDatabase = null;
	
	private EditText busStopNum;
	private EditText busStopDescription;
	private EditText busDirection;
	private EditText oppBusStopNum;
	
	private Button saveNewBusStop;
	private Button backButton;
	
	//private TextView textView;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.addnewbusstopwindow);
	    mSQLiteDatabase = this.openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
	    
	    //create two editTet boxes
	    busStopNum = (EditText)findViewById(R.id.busStopNum);
	    
	    busStopDescription = (EditText)findViewById(R.id.busStopDescription);
	    
	    busDirection = (EditText)findViewById(R.id.busStopDirection);
	    
	    oppBusStopNum = (EditText)findViewById(R.id.oppBusStopNum);
	    //create a save button
	    saveNewBusStop = (Button)findViewById(R.id.save);
	    
	    saveNewBusStop.setOnClickListener( new Button.OnClickListener() {
	    	public void onClick(View v) {
	    		//check whether the fields are filled
	    		
	        	ContentValues cv = new ContentValues();
	        	
	        	cv.put(Constants.TABLE_ID, Integer.valueOf( busStopNum.getText().toString() ) );
	        	cv.put(Constants.TABLE_DESCRIPTION, busStopDescription.getText().toString());
	        	cv.put(Constants.TABLE_DIRECTION, busDirection.getText().toString());
	        	
	        	try {
	        		cv.put(Constants.TABLE_OPPBUSSTOP, Integer.valueOf( oppBusStopNum.getText().toString() ) );
	        	} catch (NumberFormatException e) {
	        		//ignore it
	        		//TODO add error message!
	        	}
	        	cv.put(Constants.TABLE_OPPBUSSTOP, "NULL");
	        	//init count to 0
	        	cv.put(Constants.TABLE_HITCOUNT, 0);
	        	
	        	//TODO handle situation when duplicated bus stop number
	        	if( mSQLiteDatabase.insert(Constants.TABLE_NAME, null, cv) >= 0 ) {
	        		Toast addSuccess = Toast.makeText(AddNewBusStop.this, R.string.addSuccess, Toast.LENGTH_SHORT);
	        		addSuccess.show();
	        	}
	        	
	        	//go back to listing
    			Intent intentBack = new Intent();
    			intentBack.setClass( AddNewBusStop.this, BusStopListing.class );
    			startActivity(intentBack);
    			AddNewBusStop.this.finish();
	    	}
	    });
	    
	    //create an back button to go back to previous activity
	    backButton = (Button)findViewById(R.id.back);
	    backButton.setMinimumWidth(450);
	    
	    backButton.setOnClickListener( new Button.OnClickListener() { 
	    	public void onClick(View v) {
    			Intent intentBack = new Intent();
    			intentBack.setClass( AddNewBusStop.this, BusStopListing.class );
    			startActivity(intentBack);
    			AddNewBusStop.this.finish();
	    	}
	    });
	}
}
