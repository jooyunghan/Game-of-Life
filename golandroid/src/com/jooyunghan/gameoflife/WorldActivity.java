package com.jooyunghan.gameoflife;

import android.app.Activity;
import android.os.Bundle;

public class WorldActivity extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
}