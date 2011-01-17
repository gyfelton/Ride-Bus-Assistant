package com.elton.android.KWRideBusAssist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Gravity;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//display bus stop info
		Bundle bundle = intent.getExtras();
		Object messages[] = (Object[]) bundle.get("pdus");
		SmsMessage smsMessage[] = new	SmsMessage[messages.length];
		
		for( int n = 0; n < messages.length; n++ ) {
			smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
		}
		
		//produce a toast
		Toast toast = Toast.makeText(context, smsMessage[0].getMessageBody(), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 200);
		toast.show();
	}

}
