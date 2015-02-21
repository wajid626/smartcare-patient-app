package com.smartcare.bookings.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartcare.bookings.R;
import com.smartcare.bookings.Appointment;
import com.smartcare.bookings.SmartCareBookingsApplication;

public class MyAppointmentsActivity extends ActivityBase {
	private final Context context = this;
	ListView lstAppointments;
	
	Appointment[] appointments;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        appointments = ((SmartCareBookingsApplication)getApplication()).getBookedAppointments();

        setContentView(R.layout.activity_my_appointments);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_appointments, menu);
        return true;
    }
    
    protected void setUiComponents () {
    	lstAppointments = (ListView) findViewById(R.id.lstAppointments);
    	fillListAppointments(appointments);
    }
    
    protected void setUiEventHandlers () {
    	lstAppointments.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View arg1, int position, long id) {
				// We have saved the actual appointment object in the ListView, so we retrieve it
				Appointment appointment = (Appointment)adapter.getItemAtPosition(position);
				
        		// Display a message
				AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
				
				alertbox.setMessage("You have booked an appointment at " + DateFormat.format(SmartCareBookingsApplication.APPOINTMENT_DATE_FORMAT, appointment.date) + "hs at " + appointment.location + " for the " + appointment.physician).show();
        		
			}
		});
    }
    
    private void fillListAppointments (Appointment[] appointments) {
		// The ArrayAdapter calls .toString() on whatever is passed to it
		// See Appointment.toString()
		ArrayAdapter<Appointment> adapter = new ArrayAdapter<Appointment>(context,
	  		  android.R.layout.simple_list_item_1, android.R.id.text1, appointments);
	  
		lstAppointments.setAdapter(adapter);
		
	}
}
