package com.adwiii.bhw.game;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Trey on 3/8/2016.
 */
public class BH implements Comparable<BH> {
    private boolean dead;
    private int priority;
    private Expandable me;
    private int color;

    public BH(int x, int y, int priority, int color) {
        me = new BHRect(x, y);
        this.priority = priority;
        dead = false;
        this.color = color;
    }

    public void expand() {
        if (dead) return;
        me.expand();
    }

    public boolean intersects(BH other) {
        for (Point p : this.getPoints()) {
            for (Point q : other.getPoints()){
                if (p.equals(q)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Point> getPoints() {
        return me.getPoints();
    }

    public void kill() {
        dead = true;
    }

    @Override
    public int compareTo(BH other) {
        return priority - other.priority;
    }

    public int getColor() {
        return color;
    }
}
