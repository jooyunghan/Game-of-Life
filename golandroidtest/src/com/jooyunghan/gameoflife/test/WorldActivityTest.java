package com.jooyunghan.gameoflife.test;

import com.jooyunghan.gameoflife.World;
import com.jooyunghan.gameoflife.WorldActivity;
import com.jooyunghan.gameoflife.WorldView;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.MenuItem;

public class WorldActivityTest extends ActivityInstrumentationTestCase2<WorldActivity> {

	public WorldActivityTest() {
		super("com.jooyunghan.gameoflife", WorldActivity.class);
	}
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
	}
	
	public void testStart() throws Exception {
		assertNotNull(getActivity());
	}

	@UiThreadTest
	public void testPauseAndResume() throws Exception {
		Instrumentation inst = getInstrumentation();
		WorldActivity activity = getActivity();
		World w = getWorld(activity);
		String original = w.toString();
		
		inst.callActivityOnPause(activity);
		activity.setVisible(false);
		w.shake();
		activity.setVisible(true);
		inst.callActivityOnResume(activity);
		
		w = getWorld(activity);
		assertEquals(original, w.toString());
	}
	
	public void testDestroyAndCreate() throws Exception {
		WorldView.run = false;
		
		WorldActivity activity = getActivity();		
		World w = getWorld(activity);
		w.shake();
		
		activity.finish();
		setActivity(null);
		
		activity = getActivity();
		World w2 = getWorld(activity);
		assertEquals(w, w2);
	}

	private World getWorld(WorldActivity activity) {
		World w = ((WorldView) activity.findViewById(com.jooyunghan.gameoflife.R.id.world)).getWorld();
		return w;
	}
}
