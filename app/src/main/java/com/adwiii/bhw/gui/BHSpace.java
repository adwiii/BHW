package com.adwiii.bhw.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.adwiii.bhw.GameActivity;
import com.adwiii.bhw.game.BH;
import com.adwiii.bhw.game.Player;

/**
 * Created by Trey on 3/24/2016.
 */
public class BHSpace extends SurfaceView implements SurfaceHolder.Callback {

    BHThread bhThread;
    /*

     */
    private float cellWidth = 50;
    private float cellHeight = 50;

    private int rowWidth;
    private int columnHeight;

    private GameActivity game;

    private int offx; // used to handle scroll offsets
    private int offy;
    private int assumedWidth = 1080;
    private int assumedHeight = 1920;
    private float gscale = 1;

    private static final int BORDER = Color.GRAY;

    public BHSpace(Context c) {
        super(c);
        game = (GameActivity) c;
        getHolder().addCallback(this);
        bhThread = new  BHThread(getHolder(), this);
        setWillNotDraw(false);

        mScaleDetector = new ScaleGestureDetector(c, new ScaleListener());
//        setMinimumWidth(game.getWindowManager().getDefaultDisplay().getWidth());
    }

    @Override
    public void onDraw(Canvas c) {
//        Log.e("PAINT", "HALLO");
        c.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
//        c.drawText("HALLO", 5f, 5f, paint);

//        gscale = (float) Math.min((double)getWidth()/assumedWidth, (double)getHeight()/assumedHeight);
        c.translate(offx, offy);
        c.scale(gscale, gscale);

        Rect r = new Rect(); // used for convenience of passing around info to canvas

        paint.setStyle(Paint.Style.FILL);
        for (BH bh : game.getAll()) {
            paint.setColor(bh.getColor());
            for(Point p : bh.getPoints()) {
                r.set((int) (p.x * cellWidth), (int) (p.y * cellHeight), (int) ((p.x + 1) * cellWidth), (int) ((p.y + 1) * cellHeight));
                c.drawRect(r, paint);
            }
        }

        //DRAW BORDERS
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(BORDER);
        for (Player player : game.getPlayers()) {
            for (Point p : player.getHome()) {
                r.set((int) (p.x * cellWidth), (int) (p.y * cellHeight), (int) ((p.x + 1) * cellWidth), (int) ((p.y + 1) * cellHeight));
                c.drawRect(r, paint);
            }
        }

        //draw center line
//        c.drawLine(0f, getHeight() / 2f, getWidth(), getHeight() / 2f, paint);
    }

    private static final int INVALID_POINTER_ID = -1;

    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private float mLastTouchX, mLastTouchY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("TOUCH", ev.getX() + " " + ev.getY());
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    offx += dx;
                    offy += dy;

                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }
    
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            gscale *= detector.getScaleFactor();
            
            // Don't let the object get too small or too large.
            gscale = Math.max(0.1f, Math.min(gscale, 5.0f));
            
            invalidate();
            return true;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bhThread.setRunnable(true);
        bhThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        bhThread.setRunnable(false);
        boolean retry = true;
        while (retry) {
            try{
                bhThread.join();
                retry = false;
            } catch (InterruptedException e) {

            }
            break;
        }
        bhThread = null;
    }
}
