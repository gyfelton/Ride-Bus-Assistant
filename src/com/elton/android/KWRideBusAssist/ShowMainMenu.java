package com.elton.android.KWRideBusAssist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ShowMainMenu extends Activity {
	private Button viewAllButton;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.mainmenu);
	    viewAllButton = (Button)findViewById(R.id.viewAllList);
	    
	    viewAllButton.setOnClickListener( new Button.OnClickListener() { 
	    	public void onClick(View v) {
    			Intent start = new Intent();
    			start.setClass( ShowMainMenu.this, BusStopListing.class );
    			startActivity(start);
    			ShowMainMenu.this.finish();
	    	}
	    });
	}

}
