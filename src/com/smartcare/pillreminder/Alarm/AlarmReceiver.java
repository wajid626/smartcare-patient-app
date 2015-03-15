package com.smartcare.pillreminder.Alarm;

import java.util.Calendar;

import com.smartcare.pillreminder.Constants.Constants;
import com.smartcare.pillreminder.Helpers.PrescriptionsClass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	/**
	 * When the broadcast receiver receives a broadcast start the Alarm Service
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("MAD", "Received");

		
			
		// Begin service
		Intent service1 = new Intent(context, MyAlarmService.class);
		context.startService(service1);
	}

}
