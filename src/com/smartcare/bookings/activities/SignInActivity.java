package com.smartcare.bookings.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.smartcare.bookings.R;
import com.smartcare.bookings.SmartCareBookingsApplication;

public class SignInActivity extends ActivityBase {
	private final Context context = this;
	private Button btnAccept;
	private TextView txtUsername, txtPassword;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sign_in);
        super.onCreate(savedInstanceState);
        
        String username = getPreferences(MODE_PRIVATE).getString("username", "Wajid");
        txtUsername.setText(username);
		
    }

    protected void setUiComponents () {
    	btnAccept = (Button) findViewById(R.id.btnAccept);
    	txtUsername = (TextView) findViewById(R.id.txtUsername);
    	txtPassword = (TextView) findViewById(R.id.txtPassword);
    }
    
    protected void setUiEventHandlers () {
    	btnAccept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String username = txtUsername.getText().toString();
            	String password = txtPassword.getText().toString();
            	
            	// TODO: Validate username & password w/ rest service
            	
            	if (username.equalsIgnoreCase("Wajid") && password.equalsIgnoreCase("123")) {
            		// Save username as default, for next sessions
            		SharedPreferences.Editor prefEditor = getPreferences(MODE_PRIVATE).edit() ;
            		prefEditor.putString("username", username);
            		prefEditor.commit();
            		
            		// Also save it as an application-level variable for this session
            		((SmartCareBookingsApplication)getApplication()).username = username;
            		
            		Intent intent = new Intent(context, MainActivity.class);
                	startActivity(intent);
                	
            	} else {
            		System.out.println("Invalid data. Username: " + username + " | Password: " + password);
            	}
            	
            }
        });
    }
}
