package com.smartcare.bookings;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SmartCareBookingsApplication extends Application {
	public String username;
	
	public static final String PREF_APPOINTMENTS = "appointments";
	public static final String APPOINTMENT_DATE_FORMAT = "yyyy-MM-dd kk:mm";
	public static final String APPOINTMENT_DATE_FORMAT_AA = "yyyy-MM-dd hh:mmaa";

	public static final String EXTRA_LOCATION = "com.smartcare.bookings.LOCATION";
	public static final String EXTRA_BENEFIT = "com.smartcare.bookings.BENEFIT";
	
	public Appointment[] getBookedAppointments() {
    	String appointmentsJson = null;
    	
    	// Retrieve the JSON-encoded appointments from the preferences file
    	SharedPreferences preferences = getSharedPreferences(SmartCareBookingsApplication.PREF_APPOINTMENTS, MODE_PRIVATE);
    	
    	try {
    		appointmentsJson = preferences.getString("appointments", null);
    	} catch (Exception e) {
    		return new Appointment[]{};
    	}
    	
    	if (appointmentsJson == null) {
    		return new Appointment[]{};
    	}
        
    	// Serialize the JSON string into appointment objects
    	GsonBuilder builder = new GsonBuilder();
    	builder.setDateFormat(APPOINTMENT_DATE_FORMAT);
    	Gson gson = builder.create(); 
    	Appointment[] appointments = gson.fromJson(appointmentsJson, Appointment[].class);
    	
    	return appointments;
    }
	
	public void saveBookedAppointments(Appointment[] appointments) {
		// Android SDK doesn't know how to serialize objects, so we convert them to JSON
    	GsonBuilder builder = new GsonBuilder();
    	builder.setDateFormat(APPOINTMENT_DATE_FORMAT);
    	Gson gson = builder.create(); 
    	String appointmentsJson = gson.toJson(appointments);
        
		// Save the appointment
    	SharedPreferences.Editor prefEditor = getSharedPreferences(SmartCareBookingsApplication.PREF_APPOINTMENTS, MODE_PRIVATE).edit();
    	prefEditor.putString("appointments", appointmentsJson);
		prefEditor.commit();
	}
	
	public void saveBookedAppointments(List<Appointment> appointments) {
		saveBookedAppointments(appointments.toArray(new Appointment[]{}));
	}
	
	public void addBookedAppointment(Appointment appointment) {
		List<Appointment> appointments = new LinkedList<Appointment>(Arrays.asList(getBookedAppointments()));
		
		appointments.add(appointment);
		
		saveBookedAppointments(appointments);
		
	}
	
}
