package com.elton.android.KWRideBusAssist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;
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
	    
	    //notify user the reply of SMS, need to add to every activity
	    getSharedPreferences("S.PRE", 0).registerOnSharedPreferenceChangeListener(replyListener);
	}
	
	private OnSharedPreferenceChangeListener replyListener  = new OnSharedPreferenceChangeListener() {
		private AlertDialog m_showReply;
		@Override
	    //used to notify user the return of SMS
	    public void onSharedPreferenceChanged( SharedPreferences reply, String message) {
	    	m_showReply = new AlertDialog.Builder(ShowMainMenu.this)
	    								.setTitle(R.string.receiveSMSDialogTitle)
	    								.setMessage(reply.getString(message, "Opps! Something is wrong! pleace contact me!"))
	    								.setNegativeButton("Ok", new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												m_showReply.dismiss();
											}
										}).create();
	    	m_showReply.show();
	    }
	};
	
    @Override
    public void onPause() {
    	getSharedPreferences("S.PRE", 0).unregisterOnSharedPreferenceChangeListener(replyListener);
    	super.onPause();
    }
    
	@Override
    //need to be included in every activity
    public void onSaveInstanceState(Bundle outState) {
    	//when click HOME button, set active to false
    	Constants.SMS_INTERCEPTOR_IS_ACTIVE = false;
    	Log.d("onSaveInstance", "set active to false");
    }
}
