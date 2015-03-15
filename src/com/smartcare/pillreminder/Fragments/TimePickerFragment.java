package com.smartcare.pillreminder.Fragments;

import java.util.Calendar;

import com.smartcare.pillreminder.Constants.Constants;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * Fragment that appears when a user is selecting a time for their
 * prescription's alarm. Allows user to select a time of day.
 * 
 * @author peteryasi
 * 
 */
public class TimePickerFragment extends DialogFragment implements
TimePickerDialog.OnTimeSetListener {

	/**
	 * creates the dialog with the current time being the initially set value
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Get the current time
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// return dialog with the current time
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	/**
	 * Called when a time has been selected. Sets the constants chosenHour and
	 * chosenMinute to the selected time
	 */
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

		// Set the constants to the chosen time
		Constants.chosenHour = hourOfDay;
		Constants.chosenMinute = minute;

		// TODO Auto-generated method stub

	}

}
