package com.elton.android.KWRideBusAssist;

import java.util.Locale;
import java.util.logging.Logger;

import com.elton.android.KWRideBusAssist.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BusStopListing extends BaseActivity {
	
	private SQLiteDatabase mSQLiteDatabase = null;
	
	private ProgressDialog m_sendingDialog;
	private AlertDialog m_busStopDetailAndActions;
	private final static String CREATE_TABLE = "CREATE TABLE "
											   + Constants.TABLE_NAME
											   + " (" + Constants.TABLE_ID
											   + " INTEGER PRIMARY KEY,"
											   + Constants.TABLE_DESCRIPTION + " TEXT,"
											   + Constants.TABLE_DIRECTION + " TEXT,"
											   + Constants.TABLE_OPPBUSSTOP + " INTEGER,"
											   + Constants.TABLE_HITCOUNT + " INTEGER)";
											   
	LinearLayout m_LinearLayout = null;
	ListView m_ListView = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	 //for update language change if there is any TODO: check it's affect on speed
	   	 SharedPreferences sp;
		 sp = getSharedPreferences("S.PRE", 0);
	     if( sp.getBoolean("S.PRE_CHINESE", false) ) {
	       	 Locale locale =  Locale.SIMPLIFIED_CHINESE;
	       	 Locale.setDefault(locale);
	       	 Configuration config = new Configuration();
	       	 config.locale = locale;
	       	 getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	   	} else {
	       	 Locale locale = Locale.ENGLISH;
	       	 Locale.setDefault(locale);
	       	 Configuration config = new Configuration();
	       	 config.locale = locale;
	       	 getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	   	}
     
        super.onCreate(savedInstanceState);
        
        //for language change!
		this.setTitle(R.string.busStopListing);
		
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_LinearLayout = new LinearLayout(this);
        
        //set properties of m_LinearLayout
        m_LinearLayout.setOrientation(LinearLayout.VERTICAL);
        m_LinearLayout.setBackgroundColor(android.graphics.Color.BLACK);
        
        m_ListView = new ListView(this);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        m_ListView.setBackgroundColor(Color.BLACK);
        
        m_LinearLayout.addView(m_ListView, param);
        
        setContentView(m_LinearLayout);
        
        //this.deleteDatabase(Constants.DATABASE_NAME);
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
				Log.i("listAdapter","item clicked at: " + Integer.toString(arg2));
				//SQLiteCursor curr = (SQLiteCursor)arg0.getItemAtPosition( arg2 );
				Cursor curr = (Cursor) arg0.getItemAtPosition(arg2);
			
				//get clicked item's bus stop number
				final int busStopNum = curr.getInt( curr.getColumnIndex(Constants.TABLE_ID) );
				Log.i("busStopNum is: ", Integer.toString( busStopNum ) );
				
				//get clicked item's details
				String busStopDescription = curr.getString( curr.getColumnIndex(Constants.TABLE_DESCRIPTION) );
				String busDirection = curr.getString( curr.getColumnIndex(Constants.TABLE_DIRECTION) );
				final int oppStopNum = curr.getInt( curr.getColumnIndex(Constants.TABLE_OPPBUSSTOP) );
				
				LayoutInflater factory = LayoutInflater.from(BusStopListing.this);
				final View DialogView = factory.inflate(R.layout.showbusstopdetailsandactions, null);
				
				if( busDirection != null ) {
					TextView description = (TextView)DialogView.findViewById(R.id.busStopDescription);
					description.setText(busStopDescription);
				}
				
				if( busDirection != null ) {
					TextView direction = (TextView)DialogView.findViewById(R.id.busDirection);
					direction.setText(busDirection);
				}
				
				Button sendBusNum = (Button)DialogView.findViewById(R.id.sendSMS);
				Button sendOppBusNum = (Button)DialogView.findViewById(R.id.sendSMSForOppBusStop);
				Button back = (Button)DialogView.findViewById(R.id.back);
				
				sendBusNum.setText( getString(R.string.sendSMS) + " " + Constants.SENDER_NUM );
				sendOppBusNum.setText( getString(R.string.sendSMSForOppBusStop) + " " + Constants.SENDER_NUM );
				if( oppStopNum != 0 ) {
					TextView oppBusNum = (TextView)DialogView.findViewById(R.id.oppBusNum);
					oppBusNum.setText(Integer.toString(oppStopNum) );
				} else {
					sendOppBusNum.setEnabled(false);
				}
				
				sendBusNum.setOnClickListener( new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						m_busStopDetailAndActions.dismiss();
						showSendingDialogAndResult( Integer.toString(busStopNum) );
					}
				});
				
				sendOppBusNum.setOnClickListener( new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						m_busStopDetailAndActions.dismiss();
						showSendingDialogAndResult( Integer.toString(oppStopNum) );
					}
				});
				
				back.setOnClickListener( new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						m_busStopDetailAndActions.dismiss();
					}
				});
				
				m_busStopDetailAndActions = new AlertDialog.Builder(BusStopListing.this)
											.setTitle(getString(R.string.busStopNum)+ " " + Integer.toString( busStopNum ) )
											.setCancelable(true)
											.setView(DialogView).create();
				
				m_busStopDetailAndActions.show();
			}
		});
        
//        m_ListView.setOnCreateContextMenuListener( new View.OnCreateContextMenuListener() {
//			@Override
//			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//				MenuInflater inflater = new MenuInflater(getApplicationContext());
//				inflater.inflate(R.menu.menufordetailpage, menu);
//			}
//		});
//        
//        m_ListView.setOnItemLongClickListener( new OnItemLongClickListener() {
//        	@Override
//			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        		m_ListView.showContextMenu();
//        		return true;
//        	}
//		});
    }
    
    //update list view
    void updateAdapter() {
    	//get cursor from db
    	Cursor dbCursor = mSQLiteDatabase.query(Constants.TABLE_NAME, new String[] { Constants.TABLE_ID, Constants.TABLE_DESCRIPTION, Constants.TABLE_DIRECTION, Constants.TABLE_OPPBUSSTOP }, null, null, null, null, null);
    	
    	if( dbCursor != null && dbCursor.getCount() >= 0 ) {
//    		ListAdapter adapter = new SimpleCursorAdapter( this, 
//    													R.layout.my_stop_list,
//    													dbCursor,
//    													new String[] { Constants.TABLE_DESCRIPTION, Constants.TABLE_ID },
//    													new int[] { android.R.id.text1, android.R.id.text2 } );
    		BusStopListAdapter adapter = new BusStopListAdapter( this, R.layout.my_stop_list, dbCursor);
    		m_ListView.setAdapter(adapter);
    	}
    }
    
    public boolean onKeyDown( int keyCode, KeyEvent event ) {
    	if( keyCode == KeyEvent.KEYCODE_BACK ) {
    		//TODO this line needs to be add to the very first screen in the future
    		Constants.SMS_INTERCEPTOR_IS_ACTIVE = false;
    		//TODO sth else abt back needs to be set
    		mSQLiteDatabase.close();
    		this.finish();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
   
    //create the menu
    public boolean onCreateOptionsMenu( Menu menu ) {
    	//menu.clear();
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
    
	public void showSendingDialogAndResult(String busStopNum) {
		//creating a progress dialog
		final String m_busStopNum = busStopNum;
		m_sendingDialog = new ProgressDialog(BusStopListing.this);
		m_sendingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		m_sendingDialog.setTitle(R.string.sendSMSDialogTitle);
		m_sendingDialog.setMessage(getString(R.string.sendSMSDialogBody));
		m_sendingDialog.setIndeterminate(false);
		m_sendingDialog.setCancelable(false);
		m_sendingDialog.show();
		//use handler to implement showing of result toast after dialog;
		final Handler myhandler = new Handler() {
			public void handleMessage(Message m) {
				Bundle b = m.getData();
				int resultCode = b.getInt("resultCode");
				Toast sendResultToast;
				switch( resultCode ) {
					case 0:
						sendResultToast = Toast.makeText(BusStopListing.this, R.string.sendSMSSuccess, Toast.LENGTH_SHORT);
						sendResultToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_VERTICAL, 0, 200);
						sendResultToast.show();
						break;
					default:
						sendResultToast = Toast.makeText(BusStopListing.this, R.string.sendSMSFail, Toast.LENGTH_LONG);
						sendResultToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_VERTICAL, 0, 200);
						sendResultToast.show();
						break;
				}
			}
		};
		Thread t = new Thread() {
			public void run() {
				//now send the busstop num to 57555!
				int result = sendSMS( m_busStopNum );
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				m_sendingDialog.dismiss();
				Message m = myhandler.obtainMessage();
				Bundle b = new Bundle();
				b.putInt("resultCode", result);
				m.setData(b);
				myhandler.sendMessage(m);
			}
		};
		t.start();
	}
	
    private int m_resultCode;
    private int sendSMS( String busStopNum ) {
    	SmsManager sms = SmsManager.getDefault();
    	String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    	// create the sentIntent parameter
    	Intent sentIntent = new Intent(SENT_SMS_ACTION);
    	PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,0);
    	// register the Broadcast Receivers
    	registerReceiver(new BroadcastReceiver() {
    		@Override
    		public void onReceive(Context _context, Intent _intent) {
    			switch (getResultCode()) {
    			case Activity.RESULT_OK:
    				m_resultCode = 0;
    				//Toast.makeText(getBaseContext(),"SMS sent success actions", Toast.LENGTH_SHORT).show();
    				break;
    			case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
    				m_resultCode = 1;
    				//Toast.makeText(getBaseContext(),"SMS generic failure actions", Toast.LENGTH_SHORT).show();
    				break;
    			case SmsManager.RESULT_ERROR_RADIO_OFF:
    				m_resultCode = 2;
    				//Toast.makeText(getBaseContext(),"SMS radio off failure actions",Toast.LENGTH_SHORT).show();
    				break;
    			case SmsManager.RESULT_ERROR_NULL_PDU:
    				m_resultCode = 3;
    				//Toast.makeText(getBaseContext(),"SMS null PDU failure actions", Toast.LENGTH_SHORT).show();
    				break;
    			}
    		}
    	}, new IntentFilter(SENT_SMS_ACTION));
    	
    	sms.sendTextMessage(Constants.SENDER_NUM, null, busStopNum, sentPI, null);
    	return m_resultCode;
    }
}