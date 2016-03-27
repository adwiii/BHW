package com.adwiii.bhw.game;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by Trey on 3/8/2016.
 */
public class BHRect implements Expandable {

    private Rect me;
    private ArrayList<Point> pts = new ArrayList<>();
    private int rad = 1;

    public BHRect(int x, int y) {
        me = new Rect(x, y, x, y);
        pts = new ArrayList<>();
        pts.add(new Point(x, y));
    }

    @Override
    public void expand() {
        me.left--;
        me.right++;
        me.top--;
        me.bottom++;
        //add the points to the array to return
        for (int i = 0; i <= me.right - me.left; i++) {
            pts.add(new Point(me.left + i, me.top));
            pts.add(new Point(me.left + i, me.bottom));
        }
        for (int i = 1; i <= me.bottom - me.top - 1; i++) {
            pts.add(new Point(me.left, me.top + i));
            pts.add(new Point(me.right, me.top + i));
        }
    }

    @Override
    public ArrayList<Point> getPoints() {
        return pts;
    }

    public int getSize() {
        return rad;
    }
}
