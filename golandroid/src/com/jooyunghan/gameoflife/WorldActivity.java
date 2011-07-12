package com.jooyunghan.gameoflife;

import com.jooyunghan.gameoflife.WorldView.WorldThread;

import android.app.Activity;
import android.os.Bundle;

public class WorldActivity extends Activity {
    private WorldView worldView;
	private WorldThread thread;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        worldView = (WorldView) findViewById(R.id.world);
        thread = worldView.getThread();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	thread.pause();
    }
}