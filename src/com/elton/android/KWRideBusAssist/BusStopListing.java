package com.elton.android.KWRideBusAssist;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class BusStopListing extends Activity {
	// private static int miscount = 0;
	private SQLiteDatabase mSQLiteDatabase = null;
	private final static String DATABASE_NAME = "KWBusStopNumberAndDescription.db";
	private final static String TABLE_NAME = "BusStopNumberAndDescriptionAndOppositeNumber";
	
	//id here refers to bus stop number
	private final static String TABLE_ID = "_id";
	private final static String TABLE_DETAIL = "busStopDescription";
	private final static String TABLE_DETAIL2 = "oppositeToThisBusStop";
	//record count of request sent for this bus stop
	private final static String TABLE_DETAIL3 = "hitCount";
	
	private final static String CREATE_TABLE = "CREATE TABLE "
											   + TABLE_NAME
											   + " (" + TABLE_ID
											   + " INTEGER PRIMARY KEY,"
											   + TABLE_DETAIL + " TEXT,"
											   + TABLE_DETAIL2 + " INTEGER,"
											   + TABLE_DETAIL3 + " INTEGER)";
											   
	LinearLayout m_LinearLayout = null;
	ListView m_ListView = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_LinearLayout = new LinearLayout(this);
        
        //set properties of m_LinearLayout
        m_LinearLayout.setOrientation(LinearLayout.VERTICAL);
        m_LinearLayout.setBackgroundColor(android.graphics.Color.BLACK);
        
        m_ListView = new ListView(this);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        m_ListView.setBackgroundColor(Color.BLACK);
        
        m_LinearLayout.addView(m_ListView, param);
        
        setContentView(m_LinearLayout);
        
        //this.deleteDatabase(DATABASE_NAME);
        mSQLiteDatabase = this.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        
        try {
        	mSQLiteDatabase.execSQL(CREATE_TABLE);
        } catch (Exception e) {
			Log.i( "TabelCreationError: ", e.toString() );
        	//display results of database here (we need the database to be already exits
        	updateAdapter();
		}
        
        //add clickListener for clicking an item on listView
        m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Log.i("item: ", arg0.getItemAtPosition( arg2 ).toString());
				showBusStopDetailsAndActions( arg2 );
				//TODO!!!
			}
		});
    }
    
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//    	switch (keyCode) {
//    		case KeyEvent.KEYCODE_DPAD_LEFT:
//    			addBusStopNumber();
//    			break;
//    		case KeyEvent.KEYCODE_DPAD_RIGHT:
//    			deleteBusStopNumber();
//    			break;
//    		case KeyEvent.KEYCODE_DPAD_UP:
//    			editDescription();
//    			break;
//    	}
//    	return true;
//    }
//    
//    //add a ew bus stop number to db
//    public void addBusStopNumber() {
//    	ContentValues cv = new ContentValues();
//    	//TODO insert data based on user input
//    	Random ran = new Random();
//    	
//    	cv.put(TABLE_ID, ran.nextInt(9999));
//    	cv.put(TABLE_DETAIL, "Test for bus stop description");
//    	cv.put(TABLE_DETAIL2, ran.nextInt(9999));
//    	
//    	//TODO handle situation when duplicated bus stop number
//    	mSQLiteDatabase.insert(TABLE_NAME, null, cv);
//    	updateAdapter();
//    }
    
    public void deleteBusStopNumber() {
    	//TODO
    }
    
    public void editDescription() {
    	//TODO
    }
    
    //update list view
    void updateAdapter() {
    	//get cursor from db
    	Cursor dbCursor = mSQLiteDatabase.query(TABLE_NAME, new String[] { TABLE_ID, TABLE_DETAIL, TABLE_DETAIL2 }, null, null, null, null, null);
    	
    	if( dbCursor != null && dbCursor.getCount() >= 0 ) {
    		ListAdapter adapter = new SimpleCursorAdapter( this, 
    													android.R.layout.simple_list_item_2, 
    													dbCursor,
    													new String[] { TABLE_DETAIL, TABLE_ID },
    													new int[] { android.R.id.text1, android.R.id.text2 } );
    		m_ListView.setAdapter(adapter);
    	}
    }
    
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
    	if( keyCode == KeyEvent.KEYCODE_BACK ) {
    		mSQLiteDatabase.close();
    		this.finish();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    public void showBusStopDetailsAndActions( int listRowID ) {
    }
    
    //create the menu
    public boolean onCreateOptionsMenu( Menu menu ) {
    	MenuInflater inflater = getMenuInflater();
    	//set res of menu to res/menu/menu.xml
    	inflater.inflate( R.menu.menu, menu);
    	return true;
    }
    
    //process events of menu
    public boolean onOptionsItemSelected( MenuItem item ) {
    	int item_id = item.getItemId();
    	
    	switch ( item_id ) {
    		case R.id.addBusStop:
    			Intent intentAddBusStop = new Intent();
    			intentAddBusStop.setClass( BusStopListing.this, AddNewBusStop.class );
    			startActivity(intentAddBusStop);
    			BusStopListing.this.finish();
    			break;
    		case R.id.about:
    			Intent intentShowAbout = new Intent();
    			intentShowAbout.setClass( BusStopListing.this, ShowAbout.class );
    			startActivity(intentShowAbout);
    			BusStopListing.this.finish();
    			break;
    	}
    	
    	return true;
    }
}