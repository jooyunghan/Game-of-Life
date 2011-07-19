package com.jooyunghan.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WorldView extends SurfaceView implements SurfaceHolder.Callback {

	public static final String TAG = "WorldView";
	public static final int UNIT = 20;
	public static final int STEP = 200;
	
	public World world = new World();
	
	public Paint mLinePaint = new Paint();
	public Paint mFillPaint = new Paint();
	{
		mLinePaint.setColor(Color.BLACK);
		mFillPaint.setColor(Color.WHITE);
	}
	
	public int width;
	public int height;
	
	public Handler mHandler = new Handler();
	public Runnable mDraw   = new Runnable() {
		@Override
		public void run() {
			long start = System.currentTimeMillis();
			
			Canvas c = null;
			SurfaceHolder holder = getHolder();
			try {
				c = holder.lockCanvas(null);
				synchronized (holder) {
					updatePhysics();
					doDraw(c);
				}

			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					holder.unlockCanvasAndPost(c);
				}
			}
			
			long elapsed = System.currentTimeMillis() - start;
			
			mHandler.removeCallbacks(mDraw);
			mHandler.postDelayed(mDraw, STEP - elapsed);
		}
	};


	private void updatePhysics() {
		world = world.next();
	}
	
	private void doDraw(Canvas c) {
		Rect surface = new Rect();
		surface.set(0, 0, width, height);
		c.drawRect(surface, mFillPaint);
		
		for (int x=0; x<world.width(); x++)
			for (int y=0; y<world.height(); y++)
				if (world.get(x, y) == Cell.ALIVE)
					c.drawRect(x*UNIT, y*UNIT, (x+1)*UNIT, (y+1)*UNIT, mLinePaint);
	}

	public void setSurfaceSize(int width, int height) {

		this.width = width;
		this.height = height;
		World newWorld = new World(width/UNIT, height/UNIT);
		// copy old world to new world by mapping
		for (int x=0; x<world.width(); x++)
			for (int y=0; y<world.height(); y++)
				newWorld.set(x, y, world.get(x,y));
		world = newWorld;
		// for now
		world.shake();
	}

	public WorldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        setFocusable(true); // make sure we get key events
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		setSurfaceSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mHandler.post(mDraw);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHandler.removeCallbacks(mDraw);
	}


}
