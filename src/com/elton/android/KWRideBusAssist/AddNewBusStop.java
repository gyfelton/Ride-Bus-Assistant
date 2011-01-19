package com.elton.android.KWRideBusAssist;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewBusStop extends Activity {

	//TODO duplicated declare of database
	private SQLiteDatabase mSQLiteDatabase = null;
	
	private EditText busStopNum;
	private EditText busStopDescription;
	
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
	    busStopNum.setMinimumWidth(450);
	    
	    busStopDescription = (EditText)findViewById(R.id.busStopDescription);
	    busStopDescription.setMinimumWidth(450);
	    
	    //create a save button
	    saveNewBusStop = (Button)findViewById(R.id.save);
	    saveNewBusStop.setMinimumWidth(450);
	    
	    saveNewBusStop.setOnClickListener( new Button.OnClickListener() {
	    	public void onClick(View v) {
	        	ContentValues cv = new ContentValues();
	        	
	        	cv.put(Constants.TABLE_ID, Integer.valueOf( busStopNum.getText().toString() ) );
	        	cv.put(Constants.TABLE_DETAIL, busStopDescription.getText().toString());
	        	//add the opp bus stop num if have
	        	cv.put(Constants.TABLE_DETAIL2, "NULL");
	        	//init count to 0
	        	cv.put(Constants.TABLE_DETAIL3, 0);
	        	
	        	//TODO handle situation when duplicated bus stop number
	        	mSQLiteDatabase.insert(Constants.TABLE_NAME, null, cv);
	        	
	        	//go back to listing
    			Intent intentBack = new Intent();
    			intentBack.setClass( AddNewBusStop.this, BusStopListing.class );
    			startActivity(intentBack);
    			AddNewBusStop.this.finish();
	    	}
	    });
	    
	    //create an ok button to go back to previous activity
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
