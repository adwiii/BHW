package com.adwiii.bhw.game;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Trey on 3/8/2016.
 */
public interface Expandable {
    public void expand();
    public ArrayList<Point> getPoints();
    public int getSize();
}
