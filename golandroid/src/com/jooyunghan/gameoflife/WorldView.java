package com.jooyunghan.gameoflife;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class WorldView extends SurfaceView implements SurfaceHolder.Callback {

	public static final String TAG = "WorldView";

	class WorldThread extends Thread {
		private static final int UNIT = 3;
		private SurfaceHolder mSurfaceHolder;
		private Handler mHandler;
		private Context mContext;
		private Paint mLinePaint;
		private Paint mFillPaint;
		private boolean mRun = false;
		
		private float phase = 0;
		private int width;
		private int height;
		
		private World world = new World();

		public WorldThread(SurfaceHolder surfaceHolder, Context context,
				Handler handler) {
			mSurfaceHolder = surfaceHolder;
			mHandler = handler;
			mContext = context;

			mLinePaint = new Paint();
			mLinePaint.setColor(Color.BLACK);

			mFillPaint = new Paint();
			mFillPaint.setColor(Color.WHITE);
		}

		@Override
		public void run() {
			while (mRun) {
				Canvas c = null;
				try {
					c = mSurfaceHolder.lockCanvas(null);
					synchronized (mSurfaceHolder) {
						updatePhysics();
						doDraw(c);
					}
					Thread.sleep(100);
					world = world.next();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) {
						mSurfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
			Log.d(TAG, "exit from thread...");
		}

		private void doDraw(Canvas c) {
			Rect surface = new Rect();
			surface.set(0, 0, width, height);
			c.drawRect(surface, mFillPaint);
			
			
//			float x1 = 0;
//			float y1 = 100 - (float) (Math.sin(phase) * 100);
//			for (int i=1; i<100; i++) {
//				float x2 = x1 + 3;
//				float y2 = 100 - (float) (Math.sin(phase + i*0.1) * 100);
//				c.drawLine(x1, y1, x2, y2, mLinePaint);
//				
//				x1 = x2;
//				y1 = y2;
//			}
//			
			for (int x=0; x<world.width(); x++)
				for (int y=0; y<world.height(); y++)
					if (world.get(x, y) == Cell.ALIVE)
						c.drawRect(x*UNIT, y*UNIT, (x+1)*UNIT, (y+1)*UNIT, mLinePaint);
		}

		private void updatePhysics() {
			phase += 0.1;
		}

		public void setRunning(boolean b) {
			mRun = b;
		}

		public void setSurfaceSize(int width, int height) {
			synchronized (mSurfaceHolder) {
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
		}

		public void pause() {
			// TODO Auto-generated method stub
			
		}

	}
	
	private WorldThread thread;
	private TextView    mStatusText;
	
	public WorldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		// register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // create thread only; it's started in surfaceCreated()
        thread = new WorldThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
                mStatusText.setVisibility(m.getData().getInt("viz"));
                mStatusText.setText(m.getData().getString("text"));
            }
        });

        setFocusable(true); // make sure we get key events
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		thread.setSurfaceSize(width, height);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread.setRunning(true);
        thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }		
        Log.d(TAG, "surfaceDestroyed");
	}

	public WorldThread getThread() {
		return thread;
	}

}
