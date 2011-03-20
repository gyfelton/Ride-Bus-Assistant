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
import android.widget.Toast;

public class ShowMainMenu extends BaseActivity {
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
	    cityList.add(getString(R.string.vancouver));
	    
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
	    			Toast cityNotChosen = Toast.makeText(ShowMainMenu.this, R.string.cityNotChosen, Toast.LENGTH_SHORT);
	    			cityNotChosen.show();
	    			chooseCity.setPressed(true);
	    		}
	    	}
	    });
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
}
