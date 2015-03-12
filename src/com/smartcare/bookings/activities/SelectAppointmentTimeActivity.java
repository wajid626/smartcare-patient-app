package com.smartcare.bookings.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.smartcare.bookings.rest.RestClient;

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
				saveAppointment(appointment);
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
		System.out.println("physician name is ----->" + physician.name());
		Appointment[] appointments = new Appointment[]{
				new Appointment(location, physician, new Date()),
				new Appointment(location, physician, new Date(new Date().getTime() + 1000 * 60 * 30)),
				new Appointment(location, physician, new Date(new Date().getTime() + 1000 * 60 * 30 * 2))
				};
		return appointments;
	}
	
    private List<String> saveAppointment(Appointment appointment) {
    	RestClient rsClient = new RestClient("http://smartcare-services.elasticbeanstalk.com/rest/UserService/makeAppointment");
    	List<String> slots = new ArrayList<String>();
    	SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd yyyy");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("hh:ss");
    	
    	String dateString = sdf1.format(appointment.date); // formats to 09/23/2009 13:53:28.238
    	String timeString = sdf2.format(appointment.date); 
    	
      	rsClient.addParam("physicianName", appointment.physician.name());
      	rsClient.addParam("patientName", "wajid");
    	rsClient.addParam("location", appointment.location.toString());
    	rsClient.addParam("appointmentDate", dateString);
    	rsClient.addParam("appointmentTime", timeString);
    	try {
			rsClient.execute(RestClient.RequestMethod.GET);
			System.out.println("-------------");
			System.out.println(rsClient.getErrorMessage());
			System.out.println(rsClient.getResponseCode());
			System.out.println(rsClient.toString());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception occurred");
			e.printStackTrace();
		}
 	return slots;
    }

	
}
