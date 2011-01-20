package com.elton.android.KWRideBusAssist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowBusStopDetailsAndActions extends Activity {

	private TextView showBusStopNum;
	private TextView showDescription;
	private TextView showOppBusStopNum;
	private Button sendSMSButton;
	private Button backButton;
	
	private int m_busStopNumber;
	private String m_description;
	private int m_oppBusStopNum;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.showbusstopdetailsandactions);
	    
	    Bundle busStopInfo = getIntent().getExtras();
	    
	    m_busStopNumber = busStopInfo.getInt( "stopNumber" );
		m_description = busStopInfo.getString( "description" );
		//oppNum may be null
		m_oppBusStopNum = busStopInfo.getInt( "oppStopNum" );
		
		//show the info we got 
		showBusStopNum = (TextView)findViewById(R.id.showBusStopNumber);
		showBusStopNum.setText(Integer.toString(m_busStopNumber));
		showBusStopNum.setMinimumWidth(450);
		
		showDescription = (TextView)findViewById(R.id.showDescription);
		if( m_description != null ) {
			showDescription.setText( m_description );
			showBusStopNum.setMinimumWidth(450);
		}
		
		showOppBusStopNum = (TextView)findViewById(R.id.showOppBusStopNum);
		if( m_oppBusStopNum != 0 ) {
			showOppBusStopNum.setText(Integer.toString(m_oppBusStopNum));
			showOppBusStopNum.setMinimumWidth(450);
		}
		
		sendSMSButton = (Button)findViewById(R.id.sendSMS);
		sendSMSButton.setMinimumWidth(450);
		
		sendSMSButton.setOnClickListener( new Button.OnClickListener() {
			public void onClick(View v) {
				//TODO click now trigger sending sms
				//now send the busstop num to 57555!
				sendSMS( m_busStopNumber );
			}
		});
		
	    backButton = (Button)findViewById(R.id.showBack);
	    backButton.setMinimumWidth(450);
	    
	    backButton.setOnClickListener( new Button.OnClickListener() { 
	    	public void onClick(View v) {
    			Intent intentBack = new Intent();
    			intentBack.setClass( ShowBusStopDetailsAndActions.this, BusStopListing.class );
    			startActivity(intentBack);
    			ShowBusStopDetailsAndActions.this.finish();
    			ShowBusStopDetailsAndActions.this.finish();
	    	}
	    });
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
    }
    
    private void editBusStop() {
    	//TODO
    }
    
    private boolean sendSMS( int busStopNum ) {
    	SmsManager sms = SmsManager.getDefault();
    	sms.sendTextMessage("57555", null, Integer.toString(busStopNum), null, null);
    	Log.i("sendSMS", "message send!");
    	return true;
    }

}
