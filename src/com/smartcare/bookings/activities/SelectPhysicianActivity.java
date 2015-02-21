package com.smartcare.bookings.activities;

import java.util.EnumSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.smartcare.bookings.R;
import com.smartcare.bookings.SmartCareBookingsApplication;
import com.smartcare.bookings.enums.Physician;
import com.smartcare.bookings.enums.Location;
import com.smartcare.bookings.maps.LocationPhysicianMap;

public class SelectPhysicianActivity extends ActivityBase {
	private final Activity context = this;
	
	private Location location;
	
	private RadioButton optSanjose;
	private RadioButton optSantaclara;
	private RadioButton optSunnyvale;
	private RadioButton optSanfrancisco;
	
	private Button btnNext;
    
	private TextView lblLocation;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_physician);
        super.onCreate(savedInstanceState);
        
        LocationPhysicianMap locationPhysicianMap = new LocationPhysicianMap();
        
        location = Location.valueOf(getIntent().getStringExtra(SmartCareBookingsApplication.EXTRA_LOCATION));
        EnumSet<Physician> physicians = locationPhysicianMap.get(location);
        
        lblLocation.setText(location.name());

        setPhysiciansVisibility(physicians);
        
        optSanjose.setChecked(true);
        
    }

    private void setPhysiciansVisibility(EnumSet<Physician> physicians) {
    	optSanjose.setVisibility(physicians.contains(Physician.Dr_Frank) ? View.VISIBLE : View.GONE);
        optSantaclara.setVisibility(physicians.contains(Physician.Dr_Lisa) ? View.VISIBLE : View.GONE);
        optSunnyvale.setVisibility(physicians.contains(Physician.Dr_Jeff) ? View.VISIBLE : View.GONE);
        optSanfrancisco.setVisibility(physicians.contains(Physician.Dr_Tom) ? View.VISIBLE : View.GONE);
    }
    
    protected void setUiComponents () {
    	optSanjose = (RadioButton) findViewById(R.id.radioButtonFrank);
    	optSantaclara = (RadioButton) findViewById(R.id.radioButtonLisa);
    	optSunnyvale = (RadioButton) findViewById(R.id.radioButtonJeff);
    	optSanfrancisco = (RadioButton) findViewById(R.id.radioButtonTom);
    	
    	btnNext = (Button) findViewById(R.id.btnNext);
    	
    	lblLocation = (TextView) findViewById(R.id.lblLocation);
    }
    
    protected void setUiEventHandlers () {
    	btnNext.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				// Pass Location on to next Activity
            	Intent intent = new Intent(context, SelectAppointmentTimeActivity.class);
            	intent.putExtra(SmartCareBookingsApplication.EXTRA_LOCATION, location.name());
            	intent.putExtra(SmartCareBookingsApplication.EXTRA_BENEFIT, getSelectedPhysician().name());
            	
            	startActivity(intent);
			}
    		
    	});
    }
    
    private Physician getSelectedPhysician() {
    	if (optSanjose.isChecked())
    		return Physician.Dr_Frank;
    	
    	if (optSantaclara.isChecked())
    		return Physician.Dr_Lisa;
    	
    	if (optSunnyvale.isChecked())
    		return Physician.Dr_Jeff;
    	
    	if (optSanfrancisco.isChecked())
    		return Physician.Dr_Tom;
    	
    	return null;
    }
    
    
}
