package com.elton.android.KWRideBusAssist;

import com.elton.android.KWRideBusAssist.Constants;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	private static final String strRes = "android.provider.Telephony.SMS_RECEIVED";
	@Override
	public void onReceive(Context context, Intent intent) {
		if( intent.getAction().equals(strRes) && Constants.SMS_INTERCEPTOR_IS_ACTIVE ) {
			//display bus stop info
			Bundle bundle = intent.getExtras();
			if( bundle != null ) {
				Object messages[] = (Object[]) bundle.get("pdus");
				SmsMessage smsMessages[] = new SmsMessage[messages.length];
				
				for( int n = 0; n < messages.length; n++ ) {
					smsMessages[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
				}
				
				//produce a toast
				for( SmsMessage currMsg: smsMessages ) {
					String senderNum = currMsg.getDisplayOriginatingAddress();
					//case 1: 57555==57555 case 2: 57555==+157555
					if( ( senderNum.startsWith("+") && senderNum.length() <= Constants.SENDER_NUM.length()+4 && senderNum.contains( Constants.SENDER_NUM ) )
						|| (senderNum.equalsIgnoreCase( Constants.SENDER_NUM ) ) ) {
						SharedPreferences sp = context.getSharedPreferences("S.PRE", 0);
						SharedPreferences.Editor editor = sp.edit();
						editor.putString("message", currMsg.getMessageBody());
						editor.commit();
						//stop system from receiving this message TODO validate its stability
						abortBroadcast();
					}
				}
			}
		}
	}

}
