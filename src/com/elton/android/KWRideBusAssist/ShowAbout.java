package com.elton.android.KWRideBusAssist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowAbout extends Activity {
	
	private TextView textView;
	private Button okButton;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    textView = (TextView)this.findViewById(R.id.textview);
	    String string = "About: created by gyfelton";
	    textView.setText(string);
	    
	    //create a ok button to go back to previous activity
	    okButton = (Button)findViewById(R.id.Button01);
	
	    okButton.setText( "Ok" );
	    
	    okButton.setMinimumWidth(450);
	    
	    okButton.setOnClickListener( new Button.OnClickListener() { 
	    	public void onClick(View v) {
    			Intent intentBack = new Intent();
    			intentBack.setClass( ShowAbout.this, BusStopListing.class );
    			startActivity(intentBack);
    			ShowAbout.this.finish();
	    	}
	    });
	}
	
}
