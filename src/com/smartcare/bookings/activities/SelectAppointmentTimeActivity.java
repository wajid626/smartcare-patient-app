package com.smartcare.bookings.activities;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartcare.bookings.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartcare.bookings.Appointment;
import com.smartcare.bookings.SmartCareBookingsApplication;
import com.smartcare.bookings.enums.Physician;
import com.smartcare.bookings.enums.Location;

public class SelectAppointmentTimeActivity extends ActivityBase {
	private final Context context = this;
	private ListView lstTurns;
	
	private Location location;
	private Physician physician;
	Appointment[] appointments;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        location = Location.valueOf(getIntent().getStringExtra(SmartCareBookingsApplication.EXTRA_LOCATION));
        physician = Physician.valueOf(getIntent().getStringExtra(SmartCareBookingsApplication.EXTRA_BENEFIT));
		appointments = getAvailableAppointments(location, physician);

        setContentView(R.layout.activity_select_slot);
        super.onCreate(savedInstanceState);
        
    }
    
    protected void setUiComponents () {
    	lstTurns = (ListView) findViewById(R.id.lstTurns);
    	fillCalendar(appointments);
    }
    
    protected void setUiEventHandlers () {
    	lstTurns.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View arg1, int position, long id) {
				// We have saved the actual appointment object in the ListView, so we retrieve it
				Appointment appointment = (Appointment)adapter.getItemAtPosition(position);
				
				// Save the appointment in the local cache
				((SmartCareBookingsApplication)getApplication()).addBookedAppointment(appointment);

        		// Display a message
				AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
				alertbox.setMessage("You have booked an appointment at " + appointment.date).show();
        		
			}
		});
    }
    
	private void fillCalendar (Appointment[] appointments) {
		// The ArrayAdapter calls .toString() on whatever is passed to it
		// See Appointment.toString()
		ArrayAdapter<Appointment> adapter = new ArrayAdapter<Appointment>(context,
	  		  android.R.layout.simple_list_item_1, android.R.id.text1, appointments);
	  
		lstTurns.setAdapter(adapter);
		
	}
	
	private Appointment[] getAvailableAppointments (Location location, Physician physician) {
		Appointment[] appointments = new Appointment[]{
				new Appointment(location, physician, new Date()),
				new Appointment(location, physician, new Date(new Date().getTime() + 1000 * 60 * 15)),
				new Appointment(location, physician, new Date(new Date().getTime() + 1000 * 60 * 15 * 2))
				};
		return appointments;
	}
	
}
