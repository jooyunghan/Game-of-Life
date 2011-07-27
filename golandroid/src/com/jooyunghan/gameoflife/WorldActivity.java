package com.jooyunghan.gameoflife;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WorldActivity extends Activity implements OnClickListener {

	private WorldView worldView;
	private Button startButton;
	private Button shakeButton;
	private Button stopButton;
	private Button stepButton;
	private Button saveButton;
	private Button loadButton;

	WorldDbHelper dbHelper;

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
		saveButton = (Button) findViewById(R.id.save);
		saveButton.setOnClickListener(this);
		loadButton = (Button) findViewById(R.id.load);
		loadButton.setOnClickListener(this);

		dbHelper = new WorldDbHelper(this);
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
		else if (v == saveButton)
			onSaveWorld();
		else if (v == loadButton)
			onLoadWorld();
	}

	private void onLoadWorld() {
		showSavedList();
	}

	private void onSaveWorld() {
		showPromptForName();
	}
	
	private void showSavedList() {
		final CharSequence[] names = getSavedWorldNames();
		if (names.length == 0)
			return;

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Load");
		alert.setItems(names, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				World w = loadWorld(names[which]);
				worldView.setWorld(w);
			}
		});
		
		alert.show();
	}
	
	protected World loadWorld(CharSequence name) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		World w;
		
	    Cursor c = db.query(WorldDbHelper.DB_TABLE_NAME, null, WorldDbHelper.KEY_NAME + "=\"" + name + "\"", null, null, null, null);
	    if (c.moveToNext()) {
	    	int width = c.getInt(c.getColumnIndex(WorldDbHelper.KEY_WIDTH));
	    	int height = c.getInt(c.getColumnIndex(WorldDbHelper.KEY_HEIGHT));
	    	String text = c.getString(c.getColumnIndex(WorldDbHelper.KEY_WORLD));
	    	w = new World(width, height);
	    	w.setCells(text);
	    } else {
	    	w = new World();
	    }
	    
	    c.close();
	    db.close();
		return w;
	}

	private CharSequence[] getSavedWorldNames() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query(WorldDbHelper.DB_TABLE_NAME, null, null, null, null, null, null);
		ArrayList<CharSequence> names = new ArrayList<CharSequence>();
		
		while (c.moveToNext()) {  // 
			names.add(c.getString(c.getColumnIndex(WorldDbHelper.KEY_NAME)));
		}
		c.close();
		db.close();
		return (CharSequence[]) names.toArray(new CharSequence[names.size()]);
	}

	private void saveTheWorld(String name) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		World w = worldView.getWorld();
		ContentValues values = new ContentValues();
		values.put(WorldDbHelper.KEY_NAME, name);
		values.put(WorldDbHelper.KEY_WIDTH, w.width());
		values.put(WorldDbHelper.KEY_HEIGHT, w.height());
		values.put(WorldDbHelper.KEY_WORLD, w.toString());
		db.insertOrThrow(WorldDbHelper.DB_TABLE_NAME, null, values);
		db.close();
	}

	private void showPromptForName() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Name");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				saveTheWorld(input.getText().toString());
				Toast.makeText(WorldActivity.this, "Saved.", Toast.LENGTH_SHORT).show();
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {			  
		  }
		});

		alert.show();
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