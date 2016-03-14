package com.adwiii.bhw.game;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Trey on 3/8/2016.
 */
public class Player {

    public static final int[] EASY = new int[]{-1};
    public static final int[] MED = new int[]{-1, 5, 3, 1};
    public static final int[] HARD = new int[]{-1, 7, 5, 3, 2, 1};
    public static final int[] EXTREME = new int[]{-1, 15, 10, 7, 5, 4, 2, 1};

    public static final int[] cols = new int[]{
            Color.CYAN,
            0xFF8000, //orange
            Color.GREEN,
            Color.RED,
            Color.BLUE,
            0x32CD32, //dark green
            Color.BLACK
    };

    private String name;
    private ArrayList<BH> bhs;
    private int[] bhc; //to be initialized based on desired choices of bh priorities available
    private Object BHs;

    private ArrayList<Point> home;

    public Player(String name, int diff, ArrayList<Point> home) {
        this.name = name;
        switch (diff) {
            case 0:
                bhc = EASY;
                break;
            case 1:
                bhc = MED;
                break;
            case 2:
                bhc = HARD;
                break;
            case 3:
                bhc = EXTREME;
        }
        this.home = home;
    }

    public boolean canPlay(int priority) {
        return bhc[priority - 1] != 0;
    }

    public void addBH(int x, int y, int priority) {
        bhs.add(new BH(x, y, priority, cols[cols.length - bhc.length - priority + 1]));
        bhc[priority - 1]--;
    }

    public ArrayList<BH> getBHs() {
        return bhs;
    }

    public void removePts(ArrayList<Point> pts) {
        home.removeAll(pts);
    }
}
