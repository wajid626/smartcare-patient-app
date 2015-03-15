package com.smartcare.pillreminder;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.smartcare.bookings.R;
import com.smartcare.bookings.R.id;
import com.smartcare.bookings.R.layout;
import com.smartcare.bookings.R.menu;
import com.smartcare.pillreminder.Alarm.AlarmReceiver;
import com.smartcare.pillreminder.Constants.Constants;
import com.smartcare.pillreminder.Fragments.TimePickerFragment;
import com.smartcare.pillreminder.Helpers.PrescriptionsClass;

import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Class allows user to pick the information for a desired prescription and then
 * save it. This class sets up the alarm for said prescription as well
 * 
 * @author peteryasi
 * 
 */
public class AddPrescription extends Activity {

	// global variables
	boolean mondaySet = false;
	boolean tuesdaySet = false;
	boolean wednesdaySet = false;
	boolean thursdaySet = false;
	boolean fridaySet = false;
	boolean saturdaySet = false;
	boolean sundaySet = false;
	boolean timeClicked;
	boolean[] weekdays = new boolean[7];
	private PendingIntent pendingIntent;

	/**
	 * Called when activity starts
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_prescription);

		// Set up the action bar to have an Add Prescription title
		// And allow home button to be clicked
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(R.string.addPrescriptions);

		//
		timeClicked = false;
	}

	/**
	 * Called when the home button is clicked
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		// Intent to home screen
		Intent intent = new Intent(AddPrescription.this, PillReminderActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Creates the options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_prescription, menu);
		return true;
	}

	/**
	 * Called upon when the user clicks the time picker. Sends user to time
	 * picker fragment
	 * 
	 * @param v
	 */
	public void showTimePickerDialog(View v) {

		// Set the time clicked to be true
		timeClicked = true;

		// Create new dialog fragment
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
	}

	/**
	 * Called when the user clicks Save, and wants to complete their add
	 * prescription task. Method saves the prescription and creates the alarm
	 * 
	 * @param view
	 */
	public void onSaveClicked(View view) {

		Log.d("MAD", "save clicked");
		// Get the prescription name and put it into array
		EditText name = (EditText) findViewById(R.id.prescriptionName);
		if (name.equals("") || timeClicked == false) {
			Toast.makeText(AddPrescription.this,
					R.string.infoNotFilled, Toast.LENGTH_SHORT)
					.show();
		} else {

			String nameString = name.getText().toString();

			// Create a Prescriptions Class instance and set the name and date
			PrescriptionsClass prescription = new PrescriptionsClass(
					nameString, Constants.PRESCRIPTIONS2.size(),
					Constants.chosenHour, Constants.chosenMinute, weekdays);
			;
			Constants.PRESCRIPTIONS2.add(prescription);

			// Get the current number of prescriptions
			String size = Integer.toString(Constants.PRESCRIPTIONS2.size());

			// set the alarm
			setAlarm(Constants.chosenHour, Constants.chosenMinute, weekdays);

			// write the prescription to shared preference file
			savePreferences(size, nameString);

			// return to prescription page
			Intent intent = new Intent(AddPrescription.this,
					Prescriptions.class);
			startActivity(intent);
		}
	}

	/**
	 * Saves the prescription to the preferences, allowing the user to have
	 * their entered prescriptions remain in the application
	 * 
	 * @param key
	 * @param value
	 */
	private void savePreferences(String key, String value) {

		// Save the pill to the sharedpreferences file
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
		String prescriptionName = sharedPreferences.getString(
				Integer.toString(Constants.PRESCRIPTIONS2.size()), "");
		Constants.PRESCRIPTIONS.add(prescriptionName);

	}

	/**
	 * Sets the alarm for the correctly indicated times
	 * 
	 * @param hour
	 * @param minute
	 * @param weekdays
	 */
	public void setAlarm(int hour, int minute, boolean[] weekdays) {

		// Create new id for the alarm
		int alarmId = Constants.PRESCRIPTIONS2.size();

		// Get the indicated time for alarm
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, Constants.chosenHour);
		calendar.set(Calendar.MINUTE, Constants.chosenMinute);

		// Create Alarm
		Intent intentAlarm = new Intent(this, AlarmReceiver.class);
		intentAlarm.putExtra("id", alarmId);
		pendingIntent = PendingIntent.getBroadcast(AddPrescription.this,
				alarmId, intentAlarm, 0);

		// create the object
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		// set the alarm for particular time
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				pendingIntent);
		
		Log.d("MAD", "Alarm Set");
		

	}

	/**
	 * Set monday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void mondayClicked(View view) {

		final CheckedTextView monday = (CheckedTextView) findViewById(R.id.monday);

		if (monday.isChecked() == false) {
			monday.setChecked(true);
			mondaySet = true;
			weekdays[0] = true;

		} else {
			monday.setChecked(false);
			mondaySet = false;
			weekdays[0] = false;
		}
	}

	/**
	 * Set tuesday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void tuesdayClicked(View view) {

		CheckedTextView tuesday = (CheckedTextView) findViewById(R.id.tuesday);

		if (tuesday.isChecked() == false) {
			tuesday.setChecked(true);
			tuesdaySet = true;
			weekdays[1] = true;
		} else {
			tuesday.setChecked(false);
			tuesdaySet = false;
			weekdays[1] = false;
		}
	}

	/**
	 * Set wednesday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void wednesdayClicked(View view) {

		CheckedTextView wednesday = (CheckedTextView) findViewById(R.id.wednesday);

		if (wednesday.isChecked() == false) {
			wednesday.setChecked(true);
			wednesdaySet = true;
			weekdays[2] = true;
		} else {
			wednesday.setChecked(false);
			wednesdaySet = false;
			weekdays[2] = false;
		}
	}

	/**
	 * Set thursday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void thursdayClicked(View view) {

		CheckedTextView thursday = (CheckedTextView) findViewById(R.id.thursday);

		if (thursday.isChecked() == false) {
			thursday.setChecked(true);
			thursdaySet = true;
			weekdays[3] = true;
		} else {
			thursday.setChecked(false);
			thursdaySet = false;
			weekdays[3] = false;
		}
	}

	/**
	 * Set friday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void fridayClicked(View view) {

		CheckedTextView friday = (CheckedTextView) findViewById(R.id.friday);

		if (friday.isChecked() == false) {
			friday.setChecked(true);
			fridaySet = true;
			weekdays[4] = true;
		} else {
			friday.setChecked(false);
			fridaySet = false;
			weekdays[4] = false;
		}
	}

	/**
	 * Set saturday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void saturdayClicked(View view) {

		CheckedTextView saturday = (CheckedTextView) findViewById(R.id.saturday);

		if (saturday.isChecked() == false) {
			saturday.setChecked(true);
			saturdaySet = true;
			weekdays[5] = true;
		} else {
			saturday.setChecked(false);
			saturdaySet = false;
			weekdays[5] = false;
		}
	}

	/**
	 * Set sunday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void sundayClicked(View view) {

		CheckedTextView sunday = (CheckedTextView) findViewById(R.id.sunday);

		if (sunday.isChecked() == false) {
			sunday.setChecked(true);
			sundaySet = true;
			weekdays[6] = true;
		} else {
			sunday.setChecked(false);
			sundaySet = false;
			weekdays[6] = false;
		}
	}

}
