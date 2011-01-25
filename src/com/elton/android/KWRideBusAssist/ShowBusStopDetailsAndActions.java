package com.elton.android.KWRideBusAssist;

import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowBusStopDetailsAndActions extends Activity {

	//TODO duplicated declare of database
	private SQLiteDatabase mSQLiteDatabase = null;
	
	private TextView showBusStopNum;
	private TextView showDescription;
	private TextView showDirection;
	private TextView showOppBusStopNum;
	private Button sendSMSButton;
	private Button backButton;
	
	private ProgressDialog m_sendingDialog;
	
	private int m_busStopNumber;
	private String m_description;
	private String m_direction;
	private int m_oppBusStopNum;
	private int m_resultCode;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //this line need to be included in every activity
	    Constants.SMS_INTERCEPTOR_IS_ACTIVE = true;
	    setContentView(R.layout.showbusstopdetailsandactions);
	    
	    Bundle busStopInfo = getIntent().getExtras();
	    
	    m_busStopNumber = busStopInfo.getInt( "stopNumber" );
		m_description = busStopInfo.getString( "description" );
		m_direction = busStopInfo.getString( "busDirection" );
		//oppNum may be null
		m_oppBusStopNum = busStopInfo.getInt( "oppStopNum" );
		
		//show the info we got 
		showBusStopNum = (TextView)findViewById(R.id.showBusStopNumber);
		showBusStopNum.setText(Integer.toString(m_busStopNumber));
		
		showDescription = (TextView)findViewById(R.id.showDescription);
		if( m_description != null ) {
			showDescription.setText( m_description );
		}
		
		showDirection = (TextView)findViewById(R.id.showDirection);
		if( m_direction != null ) {
			showDirection.setText( m_direction );
		}
		
		showOppBusStopNum = (TextView)findViewById(R.id.showOppBusStopNum);
		if( m_oppBusStopNum != 0 ) {
			showOppBusStopNum.setText(Integer.toString(m_oppBusStopNum));
		}
		
		sendSMSButton = (Button)findViewById(R.id.sendSMS);
		sendSMSButton.setText(sendSMSButton.getText()+Constants.SENDER_NUM);
		sendSMSButton.setMinimumWidth(200);
		
		sendSMSButton.setOnClickListener( new Button.OnClickListener() {
			public void onClick(View v) {
				//creating a progress dialog
				m_sendingDialog = new ProgressDialog(ShowBusStopDetailsAndActions.this);
				m_sendingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				m_sendingDialog.setTitle("Please Wait");
				m_sendingDialog.setMessage("Sending Your Request to " + m_busStopNumber+ " now...");
				m_sendingDialog.setIndeterminate(false);
				m_sendingDialog.setCancelable(false);
				m_sendingDialog.show();
				Thread t = new Thread() {
					public void run() {
						//now send the busstop num to 57555!
						sendSMS( m_busStopNumber );
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//TODO show dialog message according to resultCode
						m_sendingDialog.dismiss();
					}
				};
				t.start();
			}
		});
		
	    backButton = (Button)findViewById(R.id.showBack);
	    backButton.setMinimumWidth(200);
	    
	    backButton.setOnClickListener( new Button.OnClickListener() { 
	    	public void onClick(View v) {
    			Intent intentBack = new Intent();
    			intentBack.setClass( ShowBusStopDetailsAndActions.this, BusStopListing.class );
    			startActivity(intentBack);
    			ShowBusStopDetailsAndActions.this.finish();
	    	}
	    });
	}
	
    @Override
    public void onResume() {
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
	       	 
	      	     Resources resources = getBaseContext().getResources();
	   	     Configuration config = resources.getConfiguration();
	   	     DisplayMetrics dm = resources .getDisplayMetrics(); 
	   	     config.locale = locale;
	   	     resources.updateConfiguration(config, dm);
	   	}
    	super.onResume();
    }
    
    //create the menu
    public boolean onCreateOptionsMenu( Menu menu ) {
    	MenuInflater inflater = getMenuInflater();
    	//set res of menu to res/menu/menu.xml
    	inflater.inflate( R.menu.menufordetailpage, menu);
    	return true;
    }
    
    //process events of menu
    public boolean onOptionsItemSelected( MenuItem item ) {
    	int item_id = item.getItemId();
    	
    	switch ( item_id ) {
    		case R.id.edit:
    			editBusStop();
    			break;
    		case R.id.delete:
    			deleteBusStop();
    			break;
    	}
    	
    	return true;
    }
    
    private void deleteBusStop() {
    	//TODO
    	mSQLiteDatabase = this.openOrCreateDatabase(Constants.DATABASE_NAME, MODE_PRIVATE, null);
    	mSQLiteDatabase.delete(Constants.TABLE_NAME, Constants.TABLE_ID + "=?", new String[] {Integer.toString(m_busStopNumber)});
		Intent intentBack = new Intent();
		intentBack.setClass( ShowBusStopDetailsAndActions.this, BusStopListing.class );
		startActivity(intentBack);
		ShowBusStopDetailsAndActions.this.finish();
    }
    
    private void editBusStop() {
    	//TODO
    }
    
    private boolean sendSMS( int busStopNum ) {
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
    	
    	sms.sendTextMessage("57555", null, Integer.toString(busStopNum), sentPI, null);
    	//sms.sendTextMessage("57555", null, Integer.toString(busStopNum), null, null);
    	//Toast.makeText(ShowBusStopDetailsAndActions.this, "sent", Toast.LENGTH_LONG).show();
    	Log.i("sendSMS", "message send!");
    	return true;
    }

    //need to be included in every activity
    public void onSaveInstanceState(Bundle outState) {
    	//when click HOME button, set active to false
    	Constants.SMS_INTERCEPTOR_IS_ACTIVE = false;
    	Log.d("onSaveInstance", "set active to false");
    }
}
