package com.smartcare.bookings.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartcare.bookings.R;
import com.smartcare.bookings.SmartCareBookingsApplication;

public class MainActivity extends ActivityBase {
	private final Context context = this;
	private TextView lblLoggedInAs;
	private Button btnMyAppointments, btnAvailableAppointments;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        
        lblLoggedInAs.setText(lblLoggedInAs.getText() + " " + ((SmartCareBookingsApplication)getApplication()).username);
    }

    protected void setUiComponents () {
    	lblLoggedInAs = (TextView) findViewById(R.id.lblLoggedInAs);
    	btnMyAppointments = (Button) findViewById(R.id.btnMyAppointments);
    	btnAvailableAppointments = (Button) findViewById(R.id.btnAvailableAppointments);
    }
    
    protected void setUiEventHandlers () {
    	btnAvailableAppointments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		Intent intent = new Intent(context, SelectLocationActivity.class);
            	startActivity(intent);
            	
            }
        });
    	btnMyAppointments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        		Intent intent = new Intent(context, MyAppointmentsActivity.class);
            	startActivity(intent);
            	
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.item1:
          Toast.makeText(this, "Option1", Toast.LENGTH_SHORT).show();
          setContentView(R.layout.preferences);
          return true;
        case R.id.item2:
            Toast.makeText(this, "Option2", Toast.LENGTH_SHORT).show();
           // setContentView(R.layout.carddetails);
            return true;  
        default:
          return super.onOptionsItemSelected(item);
        } 
    }
    
}
