package com.adwiii.bhw.game;

import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for holding Player data instance. This should be subclassed by all AIs
 * Created by Trey on 3/8/2016.
 */
public class Player {

    public static final int[] EASY = new int[]{-1};
    public static final int[] MED = new int[]{-1, 5, 3, 1};
    public static final int[] HARD = new int[]{-1, 7, 5, 3, 2, 1};
    public static final int[] EXTREME = new int[]{-1, 15, 10, 7, 5, 4, 2, 1};

    public static final int[] cols = new int[]{
            Color.CYAN,
            0xFFFF8000, //orange
            Color.GREEN,
            Color.RED,
            Color.BLUE,
            0xFFFF00FF,
            0xFF32CD32, //dark green
            Color.BLACK
    };

    private String name;
    private ArrayList<BH> bhs;
    private int[] bhc; //to be initialized based on desired choices of bh priorities available
//    private Object BHs;
    private int BHtype = 0;
    private ArrayList<Point> home;

    public Player(String name, int diff, ArrayList<Point> home, int BHtype) {
        this.name = name;
        this.BHtype = BHtype;
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
        bhc = Arrays.copyOf(bhc, bhc.length);
        bhs = new ArrayList<>();
        this.home = home;
    }

    public boolean canPlay(int priority) {
        return bhc[priority] != 0;
    }

    public void addBH(int x, int y, int priority) {
        Log.e("BH", x + ", " + y + ": " + priority);
        bhs.add(new BH(x, y, priority, cols[cols.length - bhc.length + priority], BHtype));
        bhc[priority]--;
    }

    public ArrayList<BH> getBHs() {
        return bhs;
    }

    public void removePts(ArrayList<Point> pts) {
        home.removeAll(pts);
    }

    public ArrayList<Point> getHome() {
        return home;
    }
    
    /**
     * This method should be overriden by any AI subclasses so the controller knows whether to detect inputs for it.
     * @return If this Player is an AI.
     */
    public boolean isAI() {
        return false;
    }

    public static int getNumDiffs(int diff) {
        switch (diff) {
            case 0:
                return EASY.length;
            case 1:
                return MED.length;
            case 2:
                return HARD.length;
            case 3:
                return EXTREME.length;
        }
        return -1;
    }

    public int getAvailableCount(int i) {
        return bhc[i];
    }
    
    public String getName() {
        return name;
    }

    public boolean lose() {
        return home.size() == 0;
    }
}
