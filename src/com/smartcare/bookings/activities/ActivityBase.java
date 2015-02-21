package com.smartcare.bookings.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.smartcare.bookings.R;

public abstract class ActivityBase extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUiComponents();
        setUiEventHandlers();
        
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	protected abstract void setUiComponents ();
    
    protected abstract void setUiEventHandlers ();
}
