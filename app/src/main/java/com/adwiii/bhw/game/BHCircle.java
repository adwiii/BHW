package com.adwiii.bhw.game;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Trey on 5/1/2016.
 */
public class BHCircle implements Expandable {
    private ArrayList<Point> pts = new ArrayList<>();
    private int rad = 1;
    int x, y;
    public BHCircle(int x, int y) {
        pts = new ArrayList<>();
        pts.add(new Point(x, y));
        this.x = x;
        this.y = y;
    }

    @Override
    public void expand() {
        rad++;
        Point p;
        double angle;
        int max = pts.size() * pts.size() + 10;
        for (int i = -rad - 1; i <= rad + 1; i++) {
            for (int j = -rad - 1; j <= rad + 1; j++) {
                p = new Point(x + i, y + j);
                if (Math.hypot(p.x - x, p.y - y) <= rad - .9) {
                    if (!pts.contains(p)) {
                        pts.add(p);
                    }
                }
            }
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
