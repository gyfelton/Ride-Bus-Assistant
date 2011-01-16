package com.elton.android.KWRideBusAssist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddNewBusStop extends Activity {

	private Button backButton;
	private TextView textView;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    textView = (TextView)this.findViewById(R.id.textview);
	    String string = "About: created by gyfelton";
	    textView.setText(string);
	    
	    //create a ok button to go back to previous activity
	    backButton = (Button)findViewById(R.id.Button01);
	
	    backButton.setText( "Back" );
	    
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
