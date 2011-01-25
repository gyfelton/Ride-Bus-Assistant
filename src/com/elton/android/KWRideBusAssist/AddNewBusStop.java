package com.elton.android.KWRideBusAssist;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;

public class AddNewBusStop extends Activity {

	//TODO duplicated declare of database
	private SQLiteDatabase mSQLiteDatabase = null;
	
	private EditText busStopNum;
	private EditText busStopDescription;
	private EditText busDirection;
	
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
	    //when focus on edit box, hint disappear
	    busStopNum.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( busStopNum.getHint() != null ) {
					busStopNum.setHint( null );
				}
			}
	    });
	    busStopNum.setMinimumWidth(450);
	    
	    busStopDescription = (EditText)findViewById(R.id.busStopDescription);
	    //when focus on edit box, hint disappear
	    busStopDescription.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if( !hasFocus ) {
					busStopDescription.setHint( R.string.descriptionExample);
				} else {
					if( busStopDescription.getHint() != null ) {
						busStopDescription.setHint( null );
					}
				}
			}
		});
	    busStopDescription.setMinimumWidth(450);
	    
	    busDirection = (EditText)findViewById(R.id.busStopDirection);
	    //when focus on edit box, hint disappear
	    busDirection.setOnFocusChangeListener( new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if( !hasFocus ) {
					busDirection.setHint( R.string.directionExample);
				} else {
					if( busDirection.getHint() != null ) {
						busDirection.setHint( null );
					}
				}
			}
		});
	    busDirection.setMinimumWidth(450);
	    
	    //create a save button
	    saveNewBusStop = (Button)findViewById(R.id.save);
	    saveNewBusStop.setMinimumWidth(450);
	    
	    saveNewBusStop.setOnClickListener( new Button.OnClickListener() {
	    	public void onClick(View v) {
	        	ContentValues cv = new ContentValues();
	        	
	        	cv.put(Constants.TABLE_ID, Integer.valueOf( busStopNum.getText().toString() ) );
	        	cv.put(Constants.TABLE_DESCRIPTION, busStopDescription.getText().toString());
	        	cv.put(Constants.TABLE_DIRECTION, busDirection.getText().toString());
	        	//TODO add the opp bus stop num if have
	        	cv.put(Constants.TABLE_OPPBUSSTOP, "NULL");
	        	//init count to 0
	        	cv.put(Constants.TABLE_HITCOUNT, 0);
	        	
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
