package com.jooyunghan.gameoflife;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WorldDbHelper extends SQLiteOpenHelper {
	private static final String TAG = "WorldDbHelper";	
	private static final String DB_NAME = "world.db";
	private static final int DB_VERSION = 1;
    static final String DB_TABLE_NAME = "world";
    static final String KEY_NAME = "name";
    static final String KEY_WIDTH = "width";
	static final String KEY_HEIGHT = "height";
	static final String KEY_WORLD = "world";
    private static final String DB_TABLE_CREATE =
                "CREATE TABLE " + DB_TABLE_NAME + " (" +
                KEY_NAME + " TEXT, " +
                KEY_WIDTH + " int, " +
                KEY_HEIGHT+ " int, " + 
                KEY_WORLD + " TEXT) ";

	public WorldDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + DB_TABLE_NAME); // drops the old database
	    Log.d(TAG, "onUpdated");
	    onCreate(db); // run onCreate to get new database
	}

}
