package com.smartcare.pillreminder;

import com.smartcare.bookings.R;
import com.smartcare.bookings.R.id;
import com.smartcare.bookings.R.layout;
import com.smartcare.bookings.R.menu;
import com.smartcare.pillreminder.Constants.Constants;
import com.smartcare.pillreminder.Fragments.TimePickerFragment;
import com.smartcare.pillreminder.Helpers.PrescriptionsClass;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;

/**
 * Allows user to change information previously entered. The user can alter the
 * information of a given prescriptions. The activity essentially mimics the
 * AddPrescription activity except instead of adding another prescription it
 * updates an existing one
 * 
 * @author peteryasi
 * 
 */
public class EditPrescriptions extends Activity {

	boolean[] newWeekdays = new boolean[7];
	boolean timeChanged = false;

	/**
	 * Called when activity is created. This method sets all of the information
	 * on the screen to the information previously entered for the prescription
	 * 
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_prescriptions);

		// display screen title
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(R.string.editPrescriptions);

		// Get all information previously entered
		Bundle bundle = getIntent().getExtras();
		int position = bundle.getInt("position");
		boolean[] weekdays = bundle.getBooleanArray("weekdays");
		String name = bundle.getString("name");
		newWeekdays = weekdays;

		// Set the EditText to contain the current name
		EditText currentName = (EditText) findViewById(R.id.prescriptionName2);
		currentName.setText(name);

		// Set the CheckedTextViews to their current state
		CheckedTextView monday = (CheckedTextView) findViewById(R.id.monday3);
		CheckedTextView tuesday = (CheckedTextView) findViewById(R.id.tuesday3);
		CheckedTextView wednesday = (CheckedTextView) findViewById(R.id.wednesday3);
		CheckedTextView thursday = (CheckedTextView) findViewById(R.id.thursday3);
		CheckedTextView friday = (CheckedTextView) findViewById(R.id.friday3);
		CheckedTextView saturday = (CheckedTextView) findViewById(R.id.saturday3);
		CheckedTextView sunday = (CheckedTextView) findViewById(R.id.sunday3);
		if (weekdays[0] == true) {
			monday.setChecked(true);
		}
		if (weekdays[1] == true) {
			tuesday.setChecked(true);
		}
		if (weekdays[2] == true) {
			wednesday.setChecked(true);
		}
		if (weekdays[3] == true) {
			thursday.setChecked(true);
		}

		if (weekdays[4] == true) {
			friday.setChecked(true);
		}
		if (weekdays[5] == true) {
			saturday.setChecked(true);
		}
		if (weekdays[6] == true) {
			sunday.setChecked(true);
		}
	}

	/**
	 * Set the home button to return to home screen
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(EditPrescriptions.this, PillReminderActivity.class);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Creates the options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_prescriptions, menu);
		return true;
	}

	/**
	 * Creates a time picker dialog so user can select new time for the alarm
	 * 
	 * @param v
	 */
	public void showTimePickerDialog(View v) {
		DialogFragment newFragment = new TimePickerFragment();
		newFragment.show(getFragmentManager(), "timePicker");
		timeChanged = true;
	}

	/**
	 * Called when the user is done updating their prescription
	 * 
	 * @param view
	 */
	public void onEditSaveClicked(View view) {

		// Get the prevously entered information
		Bundle bundle = getIntent().getExtras();
		int position = bundle.getInt("position");
		int hour = bundle.getInt("hour");
		int minute = bundle.getInt("minute");

		// Get the id of the prescription in question
		PrescriptionsClass current = Constants.PRESCRIPTIONS2.get(position);

		// Set the prescription to the new name and new weekdays
		EditText newName = (EditText) findViewById(R.id.prescriptionName2);
		current.setPrescriptionName(newName.getText().toString());
		current.setWeekdays(newWeekdays);

		// set the prescription to the new time
		if (timeChanged == true) {
			current.setHour(Constants.chosenHour);
			current.setMinute(Constants.chosenMinute);
		}

		// Set the prescription to the new name
		Constants.PRESCRIPTIONS.set(position, newName.getText().toString());

		// Go back to the Prescriptions screen
		Intent intent = new Intent(EditPrescriptions.this, Prescriptions.class);
		startActivity(intent);

	}

	/**
	 * Set monday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void mondayClicked(View view) {

		CheckedTextView monday = (CheckedTextView) findViewById(R.id.monday3);

		if (monday.isChecked() == false) {
			monday.setChecked(true);

			newWeekdays[0] = true;

		} else {
			monday.setChecked(false);

			newWeekdays[0] = false;
		}
	}

	/**
	 * Set tuesday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void tuesdayClicked(View view) {

		CheckedTextView tuesday = (CheckedTextView) findViewById(R.id.tuesday3);

		if (tuesday.isChecked() == false) {
			tuesday.setChecked(true);

			newWeekdays[1] = true;
		} else {
			tuesday.setChecked(false);

			newWeekdays[1] = false;
		}
	}

	/**
	 * Set wednesday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void wednesdayClicked(View view) {

		CheckedTextView wednesday = (CheckedTextView) findViewById(R.id.wednesday3);

		if (wednesday.isChecked() == false) {
			wednesday.setChecked(true);
			newWeekdays[2] = true;
		} else {
			wednesday.setChecked(false);

			newWeekdays[2] = false;
		}
	}

	/**
	 * Set thursday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void thursdayClicked(View view) {

		CheckedTextView thursday = (CheckedTextView) findViewById(R.id.thursday3);

		if (thursday.isChecked() == false) {
			thursday.setChecked(true);
			newWeekdays[3] = true;
		} else {
			thursday.setChecked(false);

			newWeekdays[3] = false;
		}
	}

	/**
	 * Set friday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void fridayClicked(View view) {

		CheckedTextView friday = (CheckedTextView) findViewById(R.id.friday3);

		if (friday.isChecked() == false) {
			friday.setChecked(true);

			newWeekdays[4] = true;
		} else {
			friday.setChecked(false);

			newWeekdays[4] = false;
		}
	}

	/**
	 * Set saturday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void saturdayClicked(View view) {

		CheckedTextView saturday = (CheckedTextView) findViewById(R.id.saturday3);

		if (saturday.isChecked() == false) {
			saturday.setChecked(true);
			newWeekdays[5] = true;
		} else {
			saturday.setChecked(false);
			newWeekdays[5] = false;
		}
	}

	/**
	 * Set sunday to true in the array of weekdays clicked
	 * 
	 * @param view
	 */
	public void sundayClicked(View view) {

		CheckedTextView sunday = (CheckedTextView) findViewById(R.id.sunday3);

		if (sunday.isChecked() == false) {
			sunday.setChecked(true);
			newWeekdays[6] = true;
		} else {
			sunday.setChecked(false);
			newWeekdays[6] = false;
		}
	}

}
