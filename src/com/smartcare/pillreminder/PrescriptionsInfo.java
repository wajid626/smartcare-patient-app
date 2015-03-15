package com.smartcare.pillreminder;

import com.smartcare.bookings.R;
import com.smartcare.bookings.R.id;
import com.smartcare.bookings.R.layout;
import com.smartcare.bookings.R.menu;
import com.smartcare.pillreminder.Constants.Constants;
import com.smartcare.pillreminder.Helpers.PrescriptionsClass;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckedTextView;
import android.widget.TextView;

/**
 * This activity displays the attributes selected for a given prescription.
 * It determines the prescription selected to view and then displays it's 
 * characteristics. This activity also contains an action bar that allows for 
 * the editting of a prescription or the deletion of a prescription
 * 
 * @author peteryasi
 *
 */
public class PrescriptionsInfo extends Activity {

	/**
	 * Called when the activity is created. Populates the views
	 * on the screen with the information of the selected Prescription.
	 * Sets the name, checks the weekdays, and displays the alarm time
	 * of the prescription
	 */
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prescriptions_info);
		
		//Set the title header
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(R.string.infoPrescriptions);

		//retrieve the id of selected prescription
		int position;
		Bundle bundle= getIntent().getExtras();
		if(bundle!=null){
			position=bundle.getInt("position");
			
			//Gets the prescription selected from id (position)
			PrescriptionsClass current=Constants.PRESCRIPTIONS2.get(position);

			//Sets the name
			TextView title=(TextView)findViewById(R.id.title);
			title.setText(current.getPrescriptionName());

			//Sets the hour and minute
			TextView hourMinute=(TextView)findViewById(R.id.hourMinute);
			String time=Integer.toString(current.getHour()) + ":" + Integer.toString(current.getMinute());
			hourMinute.setText(time);

			//initializes the days of the week checkedTextViews
			CheckedTextView monday=(CheckedTextView)findViewById(R.id.monday2);
			CheckedTextView tuesday=(CheckedTextView)findViewById(R.id.tuesday2);
			CheckedTextView wednesday=(CheckedTextView)findViewById(R.id.wednesday2);
			CheckedTextView thursday=(CheckedTextView)findViewById(R.id.thursday2);
			CheckedTextView friday=(CheckedTextView)findViewById(R.id.friday2);
			CheckedTextView saturday=(CheckedTextView)findViewById(R.id.saturday2);
			CheckedTextView sunday=(CheckedTextView)findViewById(R.id.sunday2);


			//Checks the necessary days of the week
			if(current.getWeekdays()[0]==true){
				monday.setChecked(true);
			}
			if(current.getWeekdays()[1]==true){
				tuesday.setChecked(true);
			}
			if(current.getWeekdays()[2]==true){
				wednesday.setChecked(true);
			}
			if(current.getWeekdays()[3]==true){
				thursday.setChecked(true);
			}

			if(current.getWeekdays()[4]==true){
				friday.setChecked(true);
			}
			if(current.getWeekdays()[5]==true){
				saturday.setChecked(true);
			}
			if(current.getWeekdays()[6]==true){
				sunday.setChecked(true);
			}
		}

	}

	/**
	 * Creates the menu bar. sets it to have an edit and a 
	 * delete icon
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prescriptions_info_action_bar, menu);
		return true;
	}

	/**
	 * Handles the selection of a menu icon. If edit is selected it will go to
	 * editPrescriptions. If delete is selected, it will delete the current
	 * prescription and go to the Prescriptions page
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch(item.getItemId()) {

		//if edit is selected
		case R.id.action_edit:
			
			//create a bundle to hold data
			Log.d("MAD", "edit");
			int position;
			Bundle bundle= getIntent().getExtras();
			if(bundle!=null){
				
				//Get all of the attributes from the current prescription
				position=bundle.getInt("position");
				PrescriptionsClass current=Constants.PRESCRIPTIONS2.get(position);
				String name=current.getPrescriptionName();
				int hour=current.getHour();
				int minute=current.getMinute();
				boolean[] weekdays=current.getWeekdays();

				//Add all of the attributes of the prescription to the intent's bundle
				Intent intent=new Intent(PrescriptionsInfo.this, EditPrescriptions.class);
				intent.putExtra("weekdays", weekdays);
				intent.putExtra("hour", hour);
				intent.putExtra("minute", minute);
				intent.putExtra("position", position);
				intent.putExtra("name", name);
				
				//Start the editPrescription activity
				startActivity(intent);
				break;
			}
		case R.id.action_delete:
			
			Log.d("MAD", "delete");
			
			//Create dialog to ask the user if they are sure they want to delete
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set title
			alertDialogBuilder.setTitle(R.string.app_name);

			// set dialog message
			alertDialogBuilder
			.setMessage(R.string.sureToDelete)
			.setCancelable(false)
			.setPositiveButton(R.string.delete,
					new DialogInterface.OnClickListener() {
				
				//If they select to delete
				public void onClick(DialogInterface dialog, int id) {
					int position2;
					Bundle bundle2= getIntent().getExtras();
					if(bundle2!=null){
						position2=bundle2.getInt("position");

						//Remove prescription from both lists
						Constants.PRESCRIPTIONS2.remove(position2);
						Constants.PRESCRIPTIONS.remove(position2);

						//Start Prescription Activity
						Intent intent=new Intent(PrescriptionsInfo.this, Prescriptions.class);
						startActivity(intent);
					}
				}
			})
			.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
				//if cancel is selected, simply exit dialog
				public void onClick(DialogInterface dialog, int id) {
					
					//end the alert dialog
					dialog.cancel();
				}
			
			});
		
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

			}
		return super.onOptionsItemSelected(item);
	}
	
}
