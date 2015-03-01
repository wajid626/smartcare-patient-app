package com.smartcare.bookings.activities;

import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gimbal.logging.GimbalLogConfig;
import com.gimbal.logging.GimbalLogLevel;
import com.gimbal.proximity.Proximity;
import com.gimbal.proximity.ProximityFactory;
import com.gimbal.proximity.ProximityListener;
import com.gimbal.proximity.ProximityOptions;
import com.gimbal.proximity.Visit;
import com.gimbal.proximity.VisitListener;
import com.gimbal.proximity.VisitManager;
import com.smartcare.bookings.R;
import com.smartcare.bookings.SmartCareBookingsApplication;

public class MainActivity extends ActivityBase implements ProximityListener, VisitListener{
	private final Context context = this;
	private TextView lblLoggedInAs;
	private Button btnMyAppointments, btnAvailableAppointments;
	private static final String PROXIMITY_APP_ID = "0e7b20b165e3a495d199249915365f28d81a0a0a669f55834759001ebc5a8e91";
    private static final String PROXIMITY_APP_SECRET = "e16659d95bf6dcba77970c0d56a29c0822889d718d4eee630677ef8152939ca4";
    private StringBuffer sb = new StringBuffer();
    

    private  VisitManager visitManager = null;
	
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        Log.i("wajid", "wajid123");
        
        lblLoggedInAs.setText(lblLoggedInAs.getText() + " " + ((SmartCareBookingsApplication)getApplication()).username);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		initializeProximity();
		visitManager = ProximityFactory.getInstance().createVisitManager();
        visitManager.setVisitListener(this);
        ProximityOptions options = new ProximityOptions();
        options.setOption(ProximityOptions.VisitOptionSignalStrengthWindowKey, ProximityOptions.VisitOptionSignalStrengthWindowNone);
        visitManager.startWithOptions(options);
        startProximityService();

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
          Intent intent = new Intent(this, Preferences.class);
          startActivity(intent);
          return true;
        case R.id.item2:
            Toast.makeText(this, "Option2", Toast.LENGTH_SHORT).show();
           // setContentView(R.layout.carddetails);
            return true;  
        case R.id.item3:
            Toast.makeText(this, "Option3", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_heartrate_monitor);
            Intent inent = new Intent(this, HeartRateMonitorActivity.class);
            startActivity(inent);
            return true; 
        default:
          return super.onOptionsItemSelected(item);
        } 
    }
    
 	private void initializeProximity() {
    	GimbalLogConfig.setLogLevel(GimbalLogLevel.INFO);
        GimbalLogConfig.enableFileLogging(this.getApplicationContext());
        Proximity.initialize(this, PROXIMITY_APP_ID, PROXIMITY_APP_SECRET);
        Proximity.optimizeWithApplicationLifecycle(getApplication());
    }

    private void startProximityService() {
        Log.d(MainActivity.class.getSimpleName(), "startSession");
        Proximity.startService(this);
    }
	
	@Override
	public void didArrive(Visit visit) {
		logMessage("Invoking didArrive - Beacon ID : " + visit.getTransmitter().getIdentifier()  + " [xxx]");
		//sb.append("\nBeacon  : Wajid-" +   visit.getTransmitter().getIdentifier());
		sb.append("\n checkedin");
		((TextView)findViewById(R.id.beaconMsg)).setText(sb.toString());
	}

	@Override
	public void didDepart(Visit visit) {
		logMessage("Invoking didDepart method : " + visit.getTransmitter().getIdentifier());
	}

	@Override
	public void receivedSighting(Visit visit, Date date, Integer rssi) {
		logMessage("Invoking receivedSighting : " + visit.getTransmitter().getIdentifier() + " Proximity : " + rssi);
	}

	@Override
	public void serviceStarted() {
		logMessage("Invoking serviceStarted");
	}

	@Override
	public void startServiceFailed(int arg0, String arg1) {
		//logMessage("Invoking startServiceFailed");
	}
	private void logMessage(String msg) {
		
		   
	}
	
	@Override
	public void onBackPressed() {
	}
}
