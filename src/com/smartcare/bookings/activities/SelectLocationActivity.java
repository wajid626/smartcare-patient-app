package com.smartcare.bookings.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.smartcare.bookings.R;
import com.smartcare.bookings.SmartCareBookingsApplication;
import com.smartcare.bookings.enums.Location;

public class SelectLocationActivity extends ActivityBase {
    final Context context = this;
    
	private Button btnNext;
    private RadioButton radioButtonSanjose;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_location);
        super.onCreate(savedInstanceState);
    	
        radioButtonSanjose.setChecked(true);
        
        String defaultLocationName = getPreferences(MODE_PRIVATE).getString("location", null);
        
        // See if a default location was saved from a previos session
        if (defaultLocationName != null) {
        	try {
        		Location defaultLocation = Location.valueOf(defaultLocationName);
        		
        		// We have a default location, so we just skip this activity.
        		// If the user wishes to change location, pressing "return" on the android will
        		// bring them back to this screen.
        		// TODO: this works, but it's uglier than my mother. Freaking fix it.
        		
        		Intent intent = new Intent(context, SelectPhysicianActivity.class);
            	intent.putExtra(SmartCareBookingsApplication.EXTRA_LOCATION, defaultLocation.name());
            	startActivity(intent);
        		
        	} catch (Exception ex) {
        		
        	}
        	
        }
        
        
    }

//    public void callWebService(){  
//    	RestClient client = new RestClient("http://search.twitter.com/search.json");
//    	client.addParam("q", "lautaro_d");
//    	client.addParam("count", "5");
//    	
//    	try {
//    		client.execute(RestClient.RequestMethod.GET);
//    		System.out.println(client.getResponse());
//    	} catch (Exception ex) {
//    		System.out.println(ex.getMessage());
//    	}
//    	
//    }     
    
//    public void fillCalendar() {
//    	List<CalendarEvent> calendarEvents = CalendarReader.readCalendar(context);
//    	List<String> calendarEventsAsStrings = new LinkedList<String>();
//    	
//    	for(CalendarEvent e : calendarEvents) {
//    		calendarEventsAsStrings.add(e.title + " " + e.begin);
//    	}
//    	
//    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
//      		  android.R.layout.simple_list_item_1, android.R.id.text1, calendarEventsAsStrings.toArray(new String[0]));
//      
//    	listView.setAdapter(adapter);
//    }
    
    public Location getSelectedLocation() {
    	final RadioButton radioButtonSanjose = (RadioButton) findViewById(R.id.radioButtonSanjose);
    	final RadioButton radioButtonSunnyvale = (RadioButton) findViewById(R.id.radioButtonSunnyvale);
    	final RadioButton radioButtonSantaclara = (RadioButton) findViewById(R.id.radioButtonSantaclara);
    	final RadioButton radioButtonSanfrancisco = (RadioButton) findViewById(R.id.radioButtonSanfrancisco);
    	
    	if (radioButtonSanjose.isChecked())
    		return Location.SanJose;
    	
    	if (radioButtonSunnyvale.isChecked())
    		return Location.Sunnyvale;
    	
    	if (radioButtonSantaclara.isChecked())
    		return Location.SantaClara;
    	
    	if (radioButtonSanfrancisco.isChecked())
    		return Location.SanFrancisco;
    	
    	return null;
    }

	@Override
	protected void setUiComponents() {
		btnNext = (Button) findViewById(R.id.btnNext);
	    radioButtonSanjose = (RadioButton) findViewById(R.id.radioButtonSanjose);
		
	}

	@Override
	protected void setUiEventHandlers() {
		btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Location location = getSelectedLocation();
            	
            	// Save default Location
            	SharedPreferences.Editor prefEditor = getPreferences(MODE_PRIVATE).edit() ;
        		prefEditor.putString("location", location.name());
        		prefEditor.commit();
            	
            	// Pass Location on to next Activity
            	Intent intent = new Intent(context, SelectPhysicianActivity.class);
            	intent.putExtra(SmartCareBookingsApplication.EXTRA_LOCATION, location.name());
            	
            	startActivity(intent);
            	
            }
        });
		
	}
}
