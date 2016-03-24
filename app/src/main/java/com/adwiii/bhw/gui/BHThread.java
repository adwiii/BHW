package com.adwiii.bhw.gui;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Trey on 3/24/2016.
 */
public class BHThread extends Thread{
    private SurfaceHolder sh;
    private BHSpace space;
    private boolean run;
    private Canvas canvas;

    public BHThread(SurfaceHolder sh, BHSpace space) {
        this.space = space;
        this.sh = sh;
    }

    public void setRunning(boolean run) {
        this.run = run;
    }

    public void run() {
        while(run) {
            canvas = null;
            try {
                canvas = sh.lockCanvas(null);
                synchronized (sh) {
//                    space.onDraw(canvas);
                    space.postInvalidate();
                }
            } finally {
                if (canvas != null) {
                    sh.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
