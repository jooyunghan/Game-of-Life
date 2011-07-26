package com.jooyunghan.gameoflife;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WorldActivity extends Activity implements OnClickListener {

	private WorldView worldView;
	private Button startButton;
	private Button shakeButton;
	private Button stopButton;
	private Button stepButton;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        worldView = (WorldView) findViewById(R.id.world);
        
        startButton = (Button) findViewById(R.id.start);
        startButton.setOnClickListener(this);
        shakeButton = (Button) findViewById(R.id.shake);
        shakeButton.setOnClickListener(this);
        stopButton = (Button) findViewById(R.id.stop);
        stopButton.setOnClickListener(this);
        stepButton = (Button) findViewById(R.id.step);
        stepButton.setOnClickListener(this);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	SharedPreferences prefs = getSharedPreferences("world", 0);
    	SharedPreferences.Editor editor = prefs.edit();
    	
    	World w = worldView.getWorld();
    	editor.putString("world", w.toString());
    	editor.putInt("width", w.width());
    	editor.putInt("height", w.height());
    	editor.commit();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	SharedPreferences prefs = getSharedPreferences("world", 0);
    	if (prefs.contains("world")) {
    		int width = prefs.getInt("width", 10);
    		int height = prefs.getInt("height", 10);
    		String cells = prefs.getString("world", "");
    		World world = new World(width, height);
    		world.setCells(cells);
    		
    		worldView.setWorld(world);
    	} else {
    		worldView.setWorld(new World());
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inf = getMenuInflater();
    	inf.inflate(R.menu.mainmenu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.itemShake:
        	onShakeWorld();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onClick(View v) {
		if (v == startButton) 
			onStartWorld();
		else if (v == shakeButton)
			onShakeWorld();
		else if (v == stopButton)
			onStopWorld();
		else if (v == stepButton)
			onStepWorld();
	}

	private void onShakeWorld() {
		worldView.shake();
	}

	private void onStartWorld() {
		worldView.start();
	}
	
	private void onStopWorld() {
		worldView.stop();
	}
	
	private void onStepWorld() {
		worldView.step();
	}
}