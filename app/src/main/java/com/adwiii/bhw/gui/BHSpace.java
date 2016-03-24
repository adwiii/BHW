package com.adwiii.bhw.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Trey on 3/24/2016.
 */
public class BHSpace extends SurfaceView implements SurfaceHolder.Callback {

    BHThread bhThread;

    public BHSpace(Context c) {
        super(c);
        getHolder().addCallback(this);
        bhThread = new  BHThread(getHolder(), this);
    }

    @Override
    public void onDraw(Canvas c) {
        c.drawColor(Color.BLACK);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        c.drawText("HALLO", 5f, 5f, p);
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
