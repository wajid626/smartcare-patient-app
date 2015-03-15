package com.smartcare.pillreminder;

import java.util.ArrayList;

import com.smartcare.bookings.R;
import com.smartcare.bookings.R.id;
import com.smartcare.bookings.R.layout;
import com.smartcare.bookings.R.menu;
import com.smartcare.pillreminder.Constants.Constants;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This activity displays the current list of prescriptions.
 * It allows users to see all of their entered prescriptions, add more
 * prescriptions, and click their perscriptions for more information
 * 
 * @author peteryasi
 *
 */
public class Prescriptions extends Activity {

	/**
	 * Called when activity is created. This method populates the 
	 * listview with the current prescriptions. It also handles the clicking
	 * of any prescription on the listview.
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prescriptions);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(R.string.addPrescriptions);
		
		
	    ListView lv = (ListView)findViewById(R.id.prescriptionsListView);
	    ArrayAdapter<String> myarrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Constants.PRESCRIPTIONS);
	    if(Constants.PRESCRIPTIONS.size()!=0){
	    lv.setAdapter(myarrayAdapter);
	    lv.setTextFilterEnabled(true);
	    
	    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            
        	@Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        				
        		Log.d("MAD", "Percription Info Clicked");
        		Intent intent= new Intent(Prescriptions.this, PrescriptionsInfo.class);
        		intent.putExtra("position", position);
        		startActivity(intent);
                   
                }
            });
	    
	    }else{
	    	Toast.makeText(Prescriptions.this, R.string.noPrescriptions, Toast.LENGTH_LONG).show();
	    }
	    
	}

	/**
	 * Overides the back button to ensure that it send the user back to the
	 * homepage
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent=new Intent(Prescriptions.this, PillReminderActivity.class);
		startActivity(intent);
	}

	/**
	 * Creates the options menu which is populated with an
	 * add button
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.prescriptions_action_bar, menu);
	    
	    
		return true;
	}
	
	/**
	 * Handles the clicking of the add button. Send thes user to the
	 * AddPrescriptions activity
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		//Switch on the itemId
		switch(item.getItemId()){
		
		//If add is clicked, go to AddPrescription
		case R.id.action_add:
			
		Log.d("MAD", "Add Clicked");
		Intent intent = new Intent(Prescriptions.this, AddPrescription.class);
		startActivity(intent);
		return false;
		
		}
		
		//If it wasnt add, go to the homescreen
		Intent intent=new Intent(Prescriptions.this, PillReminderActivity.class);
		startActivity(intent);
		return false;

	}

}
