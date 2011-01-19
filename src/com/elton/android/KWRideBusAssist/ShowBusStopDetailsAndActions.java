package com.elton.android.KWRideBusAssist;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowBusStopDetailsAndActions extends Activity {

	private TextView showBusStopNum;
	private TextView showDescription;
	private TextView showOppBusStopNum;
	private Button sendSMSButton;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.showbusstopdetailsandactions);
	    
	    Bundle busStopInfo = getIntent().getExtras();
	    
	    final int busStopNumber = busStopInfo.getInt( "stopNumber" );
		String description = busStopInfo.getString( "description" );
		//oppNum may be null
		int oppBusStopNum = busStopInfo.getInt( "oppStopNum" );
		
		//show the info we got 
		showBusStopNum = (TextView)findViewById(R.id.showBusStopNumber);
		showBusStopNum.setText(Integer.toString(busStopNumber));
		showBusStopNum.setMinimumWidth(450);
		
		showDescription = (TextView)findViewById(R.id.showDescription);
		if( description != null ) {
			showDescription.setText( description );
			showBusStopNum.setMinimumWidth(450);
		}
		
		showOppBusStopNum = (TextView)findViewById(R.id.showOppBusStopNum);
		if( oppBusStopNum != 0 ) {
			showOppBusStopNum.setText(Integer.toString(oppBusStopNum));
			showOppBusStopNum.setMinimumWidth(450);
		}
		
		sendSMSButton = (Button)findViewById(R.id.sendSMS);
		sendSMSButton.setMinimumWidth(450);
		
		sendSMSButton.setOnClickListener( new Button.OnClickListener() {
			public void onClick(View v) {
				//TODO click now trigger sending sms
				//now send the busstop num to 57555!
				sendSMS( busStopNumber );
			}
		});
	}
	
    private boolean sendSMS( int busStopNum ) {
    	SmsManager sms = SmsManager.getDefault();
    	sms.sendTextMessage("57555", null, Integer.toString(busStopNum), null, null);
    	Log.i("sendSMS", "message send!");
    	return true;
    }

}
