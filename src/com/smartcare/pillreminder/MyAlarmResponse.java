package com.smartcare.pillreminder;

import com.smartcare.bookings.R;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Menu;

/**
 * The class creates the alert dialog that appears when the alarm goes off
 * 
 * @author peteryasi
 * 
 */
public class MyAlarmResponse extends Activity {

	/**
	 * creates a dialog and displays it
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// create new alert dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MyAlarmResponse.this);

		// set title
		alertDialogBuilder.setTitle(R.string.app_name);

		// set dialog message
		alertDialogBuilder
		.setMessage(R.string.itsTime)
		.setCancelable(false)
		.setPositiveButton(R.string.taken,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// If taken is selected cancel dialog
				dialog.cancel();
				finish();
			}
		})
		.setNegativeButton(R.string.later,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if later is selected, cancel dialog
				dialog.cancel();
				finish();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

}
