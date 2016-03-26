package com.adwiii.bhw.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.adwiii.bhw.GameActivity;
import com.adwiii.bhw.game.BH;

/**
 * Created by Trey on 3/24/2016.
 *
 * See this for some basis information:
 * http://stackoverflow.com/questions/24890900/using-a-custom-surfaceview-and-thread-for-android-game-programming-example
 */
public class BHSpace extends SurfaceView implements SurfaceHolder.Callback {

    private static float MAX_SCALE = 3f; //TODO tune
    private static float MIN_SCALE = .5f;
    private static int PADDING = 5;
    BHThread bhThread;
    /*

     */
    private float cellWidth = 50;
    private float cellHeight = 50;


    private int bhWidth;
    private int bhHeight;

    private GameActivity game;

    private int offx; // used to handle scroll offsets
    private int offy;
    private int assumedWidth = 1080; // values assumed for size, good for painting
    private int assumedHeight = 1920;
    private float gscale = 1;

    private static final int BORDER = Color.GRAY;

    private Point[][] points;

    public BHSpace(Context context) {
        super(context);

        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        assumedHeight -= actionBarHeight; // we should also account for the notif bar probably.

        game = (GameActivity) context;
        getHolder().addCallback(this);
        bhThread = new  BHThread(getHolder(), this);
        setWillNotDraw(false);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        tapDetector = new GestureDetector(context, new TapListener());
//        setMinimumWidth(game.getWindowManager().getDefaultDisplay().getWidth());
    }

    public void setPoints(Point[][] points) {
        this.points = points;
        bhWidth = points.length;
        bhHeight = points[0].length;
    }
    @Override
    public void onDraw(Canvas c) {
//        Log.e("PAINT", "HALLO");
        c.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        float text = 30;
        paint.setTextSize(text);
        c.drawText(String.format("%d.0, %d.0", offx, offy), text + 5, text + 5, paint);

//        gscale = (float) Math.min((double)getWidth()/assumedWidth, (double)getHeight()/assumedHeight);

        //FIXME this ends up having the left and right fight when its a high zoom
//        if (gscale < 1) {
//            offx = Math.max(PADDING, Math.min(offx, (int) ((getWidth()-bhWidth*(int) cellWidth)/gscale)-PADDING));
//            offy = Math.max(PADDING, Math.min(offy, (int) ((getHeight()-bhHeight*(int) cellWidth)/gscale)-PADDING));
//        } else {
//
//        }

        c.translate(offx, offy);
        c.scale(gscale, gscale);

        Rect r = new Rect(); // used for convenience of passing around info to canvas

        paint.setStyle(Paint.Style.FILL);
        for (BH bh : game.getAll()) {
            Log.e("BHP", bh.getColor() + "");
            paint.setColor(bh.getColor());
//            paint.setColor(Color.MAGENTA);
            for(Point p : bh.getPoints()) {
                r.set((int) (p.x * cellWidth), (int) (p.y * cellHeight), (int) ((p.x + 1) * cellWidth), (int) ((p.y + 1) * cellHeight));
                c.drawRect(r, paint);
            }
        }

        //DRAW BORDERS
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(BORDER);
        Point p;
        if (points != null) {
            for (int i = 0; i < points.length; i++) {
                for (int j = 0; j < points[i].length; j++) {
                    p = points[i][j];
                    r.set((int) (p.x * cellWidth), (int) (p.y * cellHeight), (int) ((p.x + 1) * cellWidth), (int) ((p.y + 1) * cellHeight));
                    c.drawRect(r, paint);
                    if (j == points[i].length/2) { // draw center line
                        float temp = paint.getStrokeWidth();
                        paint.setStrokeWidth(3);
                        c.drawLine(r.left, r.top, r.right, r.top, paint);
                        paint.setStrokeWidth(temp);
                    }
                }
            }
        }
    }


    private static final int INVALID_POINTER_ID = -1;

    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector tapDetector;
    private float mScaleFactor = 1f;
    private float mLastTouchX, mLastTouchY;
    private boolean singleTap;
    /**
     * Information on the below from:
     * http://android-developers.blogspot.com/2010/06/making-sense-of-multitouch.html
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.e("TOUCH", ev.getX() + " " + ev.getY());
        // Let the ScaleGestureDetector inspect all events
        mScaleDetector.onTouchEvent(ev);
        singleTap = false;
        //single tap will be set to true by the next method call
        tapDetector.onTouchEvent(ev);

        if (singleTap) {
            float x = ev.getX() / gscale - offx; // in theory corrected for translations
            float y = ev.getY() / gscale - offy;
            x /= cellWidth;
            y /= cellHeight;
            Log.e("TOUCHYY", x + ", " + y);
            game.playBH((int) x,(int) y); // add a BH to check touch


            //TODO add turn logic here

            //select code
            return true;
        }

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
                    final int newPointerIndex = (pointerIndex == 0) ? 1 : 0;
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
            gscale = Math.max(MIN_SCALE, Math.min(gscale, MAX_SCALE));
            
            invalidate();
            return true;
        }
    }

    private class TapListener extends GestureDetector.SimpleOnGestureListener {
        //If we ever need any other detections, this class has double tap etc.
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            singleTap = true;
            return true;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bhThread.setRunning(true);
        bhThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (bhThread == null) {
            bhThread = new BHThread(this.getHolder(), this);
            bhThread.setRunning(true);
            bhThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        bhThread.setRunning(false);
        boolean retry = true;
        while (retry) {
            try{
                bhThread.join();
                retry = false;
            } catch (InterruptedException e) {
                //idrc, if this happens we have bigger problems
            }
        }
        bhThread = null;
    }
}
