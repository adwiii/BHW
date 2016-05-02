package com.adwiii.bhw.game;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Trey on 3/8/2016.
 */
public class BH implements Comparable<BH> {
    private boolean dead;
    private int priority;
    private Expandable me;
    private int color, deadColor;
    private Point center;
    public BH(int x, int y, int priority, int color, int type) {
        center = new Point(x, y);
        switch (type) {
            case 0: me = new BHRect(x, y);
                break;
            case 1: me = new BHCircle(x, y);
                break;
            default: me = new BHRect(x, y);
                break;
        }
        this.priority = priority;
        dead = false;
        this.color = color;
        // red * 0.299 + green * 0.587 + blue * 0.114 ( this did not work very well)
        deadColor = Color.argb(0xff, (Color.red(color) + 128) / 2, (Color.green(color) + 128) / 2, (Color.blue(color) + 128) / 2); // ???
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

    public Point getCenter() {
        return center;
    }

    public void kill() {
        dead = true;
    }

    public int getSize() {
        return me.getSize();
    }
    @Override
    public int compareTo(BH other) {
        return priority - other.priority;
    }

    public int getColor() {
        if (dead) {
            return deadColor;
        } else {
            return color;
        }
    }
}
