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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ShowMainMenu extends Activity {
	private Button viewAllButton;
	private Spinner chooseCity;
	private ArrayAdapter<String> cityList;
	private boolean cityAlreadySelected;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.mainmenu);
	    
	    chooseCity = (Spinner)findViewById(R.id.chooseCity);
	    cityList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
	    cityList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    cityList.add(getString(R.string.prompt_choose_city));
	    cityList.add(getString(R.string.waterlooKitchener));
	    cityList.add(getString(R.string.toronto));
	    
	    chooseCity.setAdapter(cityList);
	    
	    if( Constants.SENDER_NUM != null ) {
	    	if( Constants.SENDER_NUM.equalsIgnoreCase( Constants.WATERLOO_NUMBER) ) {
	    		chooseCity.setSelection(1);
	    	} else if( Constants.SENDER_NUM.equalsIgnoreCase( Constants.TORONTO_NUMBER) ) {
	    		chooseCity.setSelection(2);
	    	}
	    	cityAlreadySelected = true;
	    } else {
	    	chooseCity.setSelection(0);
	    	cityAlreadySelected = false;
	    }
	    
	    chooseCity.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch ( arg2 ) {
					case 0: cityAlreadySelected = false;break;
					case 1: {
								setDefaultCity(Constants.WATERLOO_NAME);
								cityAlreadySelected=true;
								break;
							}
					case 2: {
								setDefaultCity(Constants.TORONTO_NAME);
								cityAlreadySelected=true;
								break;
							}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    viewAllButton = (Button)findViewById(R.id.viewYourList);
	    
	    viewAllButton.setOnClickListener( new Button.OnClickListener() { 
	    	public void onClick(View v) {
	    		if( cityAlreadySelected ) {
	    			Intent start = new Intent();
	    			start.setClass( ShowMainMenu.this, BusStopListing.class );
	    			startActivity(start);
	    			ShowMainMenu.this.finish();
	    		} else {
	    			chooseCity.setPressed(true);
	    		}
	    	}
	    });
	    
	    //notify user the reply of SMS, need to add to every activity
	    getSharedPreferences("S.SMS", 0).registerOnSharedPreferenceChangeListener(replyListener);
	}
	
	private int setDefaultCity( String city ) {
		SharedPreferences sp = getSharedPreferences("S.PRE", 0);
	    SharedPreferences.Editor editor = sp.edit();
	    if( city.equalsIgnoreCase(Constants.TORONTO_NAME) ) {
	    	editor.putString( "S.PRE_CITY_NAME" , Constants.TORONTO_NAME);
	    	Constants.SENDER_NUM = Constants.TORONTO_NUMBER;
	    }
	    if( city.equalsIgnoreCase(Constants.WATERLOO_NAME) ) {
	    	editor.putString( "S.PRE_CITY_NAME" , Constants.WATERLOO_NAME);
	    	Constants.SENDER_NUM = Constants.WATERLOO_NUMBER;
	    }
	    editor.commit();
		return 0;
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
