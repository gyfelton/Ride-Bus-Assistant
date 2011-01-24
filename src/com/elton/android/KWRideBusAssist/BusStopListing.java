package com.elton.android.KWRideBusAssist;

import com.elton.android.KWRideBusAssist.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class BusStopListing extends Activity {
	
	private SQLiteDatabase mSQLiteDatabase = null;
	
	private final static String CREATE_TABLE = "CREATE TABLE "
											   + Constants.TABLE_NAME
											   + " (" + Constants.TABLE_ID
											   + " INTEGER PRIMARY KEY,"
											   + Constants.TABLE_DETAIL + " TEXT,"
											   + Constants.TABLE_DETAIL2 + " INTEGER,"
											   + Constants.TABLE_DETAIL3 + " INTEGER)";
											   
	LinearLayout m_LinearLayout = null;
	ListView m_ListView = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.ACTIVE = true;
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set the sender num!
        Constants.SENDER_NUM = "57555";
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
        mSQLiteDatabase = this.openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
        
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
				
				SQLiteCursor curr = (SQLiteCursor)arg0.getItemAtPosition( arg2 );
			
				//get clicked item's bus stop number
				int busStopNum = curr.getInt( curr.getColumnIndex(Constants.TABLE_ID) );
				Log.i("busStopNum is: ", Integer.toString( busStopNum ) );
				
				//get clicked item's details
				String busStopDescription = curr.getString( curr.getColumnIndex(Constants.TABLE_DETAIL) );
				int oppStopNum = curr.getInt( curr.getColumnIndex(Constants.TABLE_DETAIL2) );
				
	    		//create a bundle with bus stop number, all the details
	    		Bundle busStopInfo = new Bundle();
	    		busStopInfo.putInt( "stopNumber", busStopNum );
	    		busStopInfo.putString( "description", busStopDescription );
	    		busStopInfo.putInt( "oppStopNum", oppStopNum );
	    		
	    		//create the intent to direct to the new activity
	    		Intent showBusStopDetailsAndActions = new Intent( BusStopListing.this, ShowBusStopDetailsAndActions.class );
	    		showBusStopDetailsAndActions.putExtras( busStopInfo );
	    		startActivity(showBusStopDetailsAndActions);
	    		BusStopListing.this.finish();
			}
		});
    }
    
    //update list view
    void updateAdapter() {
    	//get cursor from db
    	Cursor dbCursor = mSQLiteDatabase.query(Constants.TABLE_NAME, new String[] { Constants.TABLE_ID, Constants.TABLE_DETAIL, Constants.TABLE_DETAIL2 }, null, null, null, null, null);
    	
    	if( dbCursor != null && dbCursor.getCount() >= 0 ) {
    		ListAdapter adapter = new SimpleCursorAdapter( this, 
    													android.R.layout.simple_list_item_2, 
    													dbCursor,
    													new String[] { Constants.TABLE_DETAIL, Constants.TABLE_ID },
    													new int[] { android.R.id.text1, android.R.id.text2 } );
    		m_ListView.setAdapter(adapter);
    	}
    }
    
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
    	if( keyCode == KeyEvent.KEYCODE_BACK ) {
    		//TODO this line needs to be add to the very first screen in the future
    		Constants.ACTIVE = false;
    		//TODO sth else abt back needs to be set
//    		mSQLiteDatabase.close();
//    		this.finish();
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
    	inflater.inflate( R.menu.menuforlist, menu);
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
    
    public void onSaveInstanceState(Bundle outState) {
    	//when click HOME button, set active to false
    	Constants.ACTIVE = false;
    	Log.v("onDestroy", "set active to false");
    }
}