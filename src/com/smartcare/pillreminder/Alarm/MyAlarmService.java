package com.smartcare.pillreminder.Alarm;

import com.smartcare.pillreminder.MyAlarmResponse;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Service that is activated as a response to the AlarmManager The service
 * handles the response and creates an alert dialog to inform the user
 * 
 * @author peteryasi
 * 
 */
public class MyAlarmService extends Service {

	private NotificationManager mManager;

	/**
	 * Method is called when service is created
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	/**
	 * Method is called when service is started Creates an intent to the
	 * AlarmResponse activity which will then display an alert dialog
	 */
	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Log.d("MAD", "Service Started");
		
		
		// create intent, set a flag
		Intent alertIntent = new Intent(MyAlarmService.this,
				MyAlarmResponse.class);
		alertIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		

		//Make a notification sound
		try {
		    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		    
		    //play sound
		    r.play();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		// start intent
		startActivity(alertIntent);

	}

	/**
	 * Method called when the service is destroyed
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

