package com.smartcare.pillreminder;

import com.smartcare.bookings.R;
import com.smartcare.bookings.R.drawable;
import com.smartcare.bookings.R.id;
import com.smartcare.bookings.R.layout;
import com.smartcare.bookings.R.menu;
import com.smartcare.pillreminder.CustomLists.HomeCustomList;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Main activity of aplicaton. Sets the user interface of the homescreen and
 * allows users to select on of the features
 * 
 * @author peteryasi
 * 
 */
public class PillReminderActivity extends Activity {

	// Set the titles of the listview
	ListView list;
	String[] items = { "My Prescriptions", "Schedule", "Pharmacy",

	};

	// Set images of the listview
	Integer[] imageId = { R.drawable.myprescriptions, R.drawable.ic_schedule,
			R.drawable.ic_pharmacy, };

	/**
	 * Creates the user interface and handles the clicking of the listview rows
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pill_reminder);

		// Set the listview to the customized HomeCustomList
		HomeCustomList adapter = new HomeCustomList(PillReminderActivity.this, items, imageId);
		list = (ListView) findViewById(R.id.listView1);
		list.setAdapter(adapter);

		// Create on click listener
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// If My Prescriptions is selected go to Prescription Class
				if (items[position].equals("My Prescriptions")) {
					Log.d("MAD", "Prescriptions Clicked");
					Intent intent = new Intent(PillReminderActivity.this, Prescriptions.class);
					startActivity(intent);
				}
				// If Pharmacy is selected go to GoogleMap
				if (items[position].equals("Pharmacy")) {
					Log.d("MAD", "Pharmacy Clicked");
					Intent intent2 = new Intent(PillReminderActivity.this, MyGoogleMap.class);
					startActivity(intent2);
				}
				// TBD
				if (items[position].equals("Schedule")) {
					Log.d("MAD", "Schedule Clicked");

				}

			}
		});
	}

	/**
	 * Create and options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
