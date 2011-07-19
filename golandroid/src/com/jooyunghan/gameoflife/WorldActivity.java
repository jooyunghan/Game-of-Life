package com.jooyunghan.gameoflife;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class WorldActivity extends Activity {

	private WorldView worldView;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        worldView = (WorldView) findViewById(R.id.world);
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
        	worldView.world.shake();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}