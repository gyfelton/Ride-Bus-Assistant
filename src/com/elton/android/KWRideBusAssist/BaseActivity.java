package com.elton.android.KWRideBusAssist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {
	
	public static Context currentContext;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		//FIXME response shows twice?!
		super.onCreate(savedInstanceState);
		currentContext = this;
		//notify user the reply of SMS, need to add to every activity
	    getSharedPreferences("S.SMS", 0).registerOnSharedPreferenceChangeListener(replyListener);
	}
	
	private OnSharedPreferenceChangeListener replyListener  = new OnSharedPreferenceChangeListener() {
		private AlertDialog m_showReply;
		@Override
	    //used to notify user the return of SMS
	    public void onSharedPreferenceChanged( SharedPreferences reply, String message) {
	    	m_showReply = new AlertDialog.Builder(currentContext)
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
	
	//to enable intercept once activity is back
    @Override
    public void onResume() {
    	Constants.SMS_INTERCEPTOR_IS_ACTIVE = true;
    	super.onResume();
    }
	
    @Override
    public void onPause() {
    	getSharedPreferences("S.SMS", 0).unregisterOnSharedPreferenceChangeListener(replyListener);
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
