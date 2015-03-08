package com.smartcare.bookings.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.smartcare.bookings.R;
import com.smartcare.bookings.Appointment;
import com.smartcare.bookings.SmartCareBookingsApplication;
import com.smartcare.bookings.rest.RestClient;

public class MyAppointmentsActivity extends ActivityBase {
	private final Context context = this;
	ListView lstAppointments;
	
	//Appointment[] appointments;
	List<String> appointments;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //appointments = ((SmartCareBookingsApplication)getApplication()).getBookedAppointments();
       appointments = getAppointments("Wajid");

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
    	fillListAppointmentsNew(appointments);
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
 
    private void fillListAppointmentsNew (List<String> appointments) {
		// The ArrayAdapter calls .toString() on whatever is passed to it
		// See Appointment.toString()
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
	  		  android.R.layout.simple_list_item_1, android.R.id.text1, appointments);
		lstAppointments.setAdapter(adapter);
	}

    
    private List<String> getAppointments(String patientName) {
    	RestClient rsClient = new RestClient("http://smartcare-services.elasticbeanstalk.com/rest/AdminService/getAppointmentDetails");
    	List<String> slots = new ArrayList<String>();
    	rsClient.addParam("patientName", "wajid");
    	try {
			rsClient.execute(RestClient.RequestMethod.GET);
			System.out.println("-------------");
			System.out.println(rsClient.getErrorMessage());
			System.out.println(rsClient.getResponseCode());
			System.out.println(rsClient.getResponse());
			String responseArray =rsClient.getResponse();
			 Log.i("Raje-Restclient", rsClient.getResponse().toString());
			 Appointment[] app = new Appointment[10];
			 
			 
			 JSONArray jArray = new JSONArray(rsClient.getResponse().toString());
			 boolean flag=true;
			 
			 for (int i=0; i < jArray.length() && flag; i++)
			 {
			     try {
			    	 StringBuffer s = new StringBuffer("Appontment with ");
						
			    	 //Parsing the Json array from the service response
			    	 JSONObject json_data = jArray.getJSONObject(i);
			    	  String physicianName = json_data.getString("PhysicianName");
			          String appointmentDateTime =  json_data.getString("AppointmentDateTime");
			          s.append(physicianName);
			          s.append(" on ");
			          s.append(appointmentDateTime);
			          s.append(" at ");
			          s.append("San Jose");
			          //app[i] = new Appointment(null, null, appointmentDateTime);
			          slots.add(s.toString());
			     } catch (JSONException e) {
			         // Oops
			     }
			 }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception occurred");
			e.printStackTrace();
		}
       // return new PayPalPayment(new BigDecimal("10.75"), "USD", "My medical charges",
       //         paymentIntent);
	return slots;
    }
}
