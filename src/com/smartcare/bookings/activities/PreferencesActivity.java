package com.smartcare.bookings.activities;

import com.smartcare.bookings.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class PreferencesActivity extends Activity {

	private CheckBox chkBeacons, chkPayments, chkPacemaker;
	private Button btnDisplay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences);

		addListenerOnChkBeacons();
		addListenerOnButton();
	}

	public void addListenerOnChkBeacons() {

		chkBeacons = (CheckBox) findViewById(R.id.chk_checkin);

		chkBeacons.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (((CheckBox) v).isChecked()) {
					Toast.makeText(PreferencesActivity.this,
							"Bro, try Android :)", Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	public void addListenerOnButton() {

		chkBeacons = (CheckBox) findViewById(R.id.chk_checkin);
		chkPayments = (CheckBox) findViewById(R.id.chk_payments);
		chkPacemaker = (CheckBox) findViewById(R.id.chk_pacemaker);
		btnDisplay = (Button) findViewById(R.id.savePreferences);

		btnDisplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				StringBuffer result = new StringBuffer();
				result.append("Beacons check : ")
						.append(chkBeacons.isChecked());
				result.append("\nPayments check : ").append(
						chkPayments.isChecked());
				result.append("\nPacemaker check :").append(
						chkPacemaker.isChecked());

				Toast.makeText(PreferencesActivity.this, result.toString(),
						Toast.LENGTH_LONG).show();

			}
		});

	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(PreferencesActivity.this, MainActivity.class);
	    startActivity(intent);
	}
}