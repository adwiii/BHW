package com.adwiii.bhw;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.adwiii.bhw.game.BH;
import com.adwiii.bhw.game.Player;
import com.adwiii.bhw.gui.BHSpace;

import java.util.ArrayList;

/**
 * Created by Trey on 3/8/2016.
 */
public class GameActivity extends AppCompatActivity {

    public static final String P1_NAME = "p1Name";
    public static final String P2_NAME = "p2Name";
    public static final String DIFF = "diff";

    public static final int PLAY = 1;
    public static final int PAUSE = 2;

    public int state;

    public int turn = 0;

    public int currentSelectedPriority;

    public ArrayList<Player> players;

    BHSpace space;

    int width = 0;
    int height = 0;

    int diff;

    ArrayList<Point> p1Home;
    ArrayList<Point> p2Home;
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        Log.e("BHW", "Game");

//        setContentView(R.layout.activity_game);

        diff = getIntent().getIntExtra(DIFF, 0);

//        gameLayout = (GridLayout) findViewById(R.id.gameLayout);

        space = new BHSpace(this);
        setContentView(space);

        initGUI();

        players = new ArrayList<>();
        players.add(new Player(getIntent().getStringExtra(P1_NAME), diff, p1Home));
        players.add(new Player(getIntent().getStringExtra(P2_NAME), diff, p2Home));
    }

    private void initGUI() {
        //TODO add menubar thingy
        switch (diff) {//29x60
            case 0://easy
                width = 20;
                break;
            case 1://medium
                width = 30;
                break;
            case 2://hard
                width = 45;
                break;
            case 3://extreme
                width = 50;
                break;
        }
        if (height == 0) {
            height = width * 2;
        }

        p1Home = new ArrayList<>();
        p2Home = new ArrayList<>();

//        Button b;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ((j < height / 2) ? p1Home : p2Home).add(new Point(i, j));
//                b = new Button(this);
//                b.setOnClickListener(this);
//                GridLayout.LayoutParams p = new GridLayout.LayoutParams();
//                //maybe?
//                p.rowSpec = GridLayout.spec(j, 1, 1f);
//                p.columnSpec = GridLayout.spec(i, 1, 1f);
//                b.setWidth(10);
//                b.setHeight(10);
//                b.setText("HI");
//                b.setLayoutParams(p);
//                b.setTag(R.id.XTAG, i);
//                b.setTag(R.id.YTAG, j);
//                b.setTag(R.id.PRIORITY_TAG, 0);
//                b.setBackgroundColor(Color.WHITE);
//                buttons[i][j] = b;
//                gameLayout.addView(b, i, j);
            }
        }
//        gameLayout.invalidate();
    }

    private void newTurn() {
        Player p = players.get(turn);
        for (BH bh : p.getBHs()) {
            bh.expand();
        }
        checkIntersections(); //this also kills

        checkWin();
    }

    private void checkWin() {
        ArrayList<BH> allBH = getAll();
        ArrayList<Point> pts = new ArrayList<>();
        for (BH b : allBH) {
            pts.addAll(b.getPoints());
        }
        for (int i = 0; i < players.size(); i++) {
            players.get(i).removePts(pts);
//            if (players.get(i).lose())
        }
    }

    private void checkIntersections() {
        ArrayList<BH> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            for (int j = i + 1; j < all.size(); j++) {
                if (all.get(i).intersects(all.get(j))) {
                    all.get(i).kill();
                    all.get(j).kill();
                }
            }
        }
    }

    private ArrayList<BH> getAll() {
        ArrayList<BH> all = new ArrayList<>();
        for (Player p : players) {
            all.addAll(p.getBHs());
        }
        return all;
    }
}
