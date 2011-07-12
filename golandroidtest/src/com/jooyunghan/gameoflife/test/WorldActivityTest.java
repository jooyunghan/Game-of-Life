package com.jooyunghan.gameoflife.test;

import com.jooyunghan.gameoflife.WorldActivity;

import android.test.ActivityInstrumentationTestCase2;

public class WorldActivityTest extends ActivityInstrumentationTestCase2<WorldActivity> {

	public WorldActivityTest() {
		super("com.jooyunghan.gameoflife", WorldActivity.class);
	}
	
	
	public void testStart() throws Exception {
		assertNotNull(getActivity());
	}

}
