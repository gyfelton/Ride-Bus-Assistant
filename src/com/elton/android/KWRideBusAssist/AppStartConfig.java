package com.elton.android.KWRideBusAssist;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class AppStartConfig extends Activity {

	private Button englishButton;
	private Button chineseButton;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //init

        Constants.SMS_INTERCEPTOR_IS_ACTIVE = true;
        
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    sp = getSharedPreferences("S.PRE", 0);
	    editor = sp.edit();
	    
	    String cityName = sp.getString("S.PRE_CITY_NAME", "NOT_SPECIFIED");
	    
	  //set the sender num, if never set, set to null
	    if( cityName.equalsIgnoreCase("waterloo") ) { 
	        Constants.SENDER_NUM = Constants.WATERLOO_NUMBER;
	    } else if( cityName.equalsIgnoreCase("toronto") ) {
	    	Constants.SENDER_NUM = Constants.TORONTO_NUMBER;
	    } else {
	    	Constants.SENDER_NUM = null;
	    }
	    
        if( sp.getBoolean("S.PRE_FIRST_LAUNCH", true) ) {
    	    setContentView(R.layout.firstlunch);
    	    englishButton = (Button)findViewById(R.id.englishButton);
    	    //english.setMinimumWidth(200);
    	    englishButton.setOnClickListener( new Button.OnClickListener() { 
    	    	public void onClick(View v) {
    	    		editor.putBoolean("S.PRE_CHINESE", false);
    	    		editor.putBoolean("S.PRE_FIRST_LAUNCH", false);
    	    		editor.commit();
        			Intent start = new Intent();
        			start.setClass( AppStartConfig.this, AppStartConfig.class );
        			startActivity(start);
        			AppStartConfig.this.finish();
    	    	}
    	    });
    	    
    	    chineseButton = (Button)findViewById(R.id.chineseButton);
    	    //english.setMinimumWidth(200);
    	    
    	    chineseButton.setOnClickListener( new Button.OnClickListener() { 
    	    	public void onClick(View v) {
    	    		editor.putBoolean("S.PRE_CHINESE", true);
    	    		editor.putBoolean("S.PRE_FIRST_LAUNCH", false);
    	    		editor.commit();
        			Intent start = new Intent();
        			start.setClass( AppStartConfig.this, AppStartConfig.class );
        			startActivity(start);
        			AppStartConfig.this.finish();
    	    	}
    	    });
        } else {
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
    			Intent start = new Intent();
    			start.setClass( AppStartConfig.this, ShowMainMenu.class );
	    		startActivity(start);
	    		AppStartConfig.this.finish();
        }
	}
	
	@Override
    //need to be included in every activity
    public void onSaveInstanceState(Bundle outState) {
    	//when click HOME button, set active to false
    	Constants.SMS_INTERCEPTOR_IS_ACTIVE = false;
    	Log.d("onSaveInstance", "set active to false");
    }
}
