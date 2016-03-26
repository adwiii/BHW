package com.adwiii.bhw;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.adwiii.bhw.game.BH;
import com.adwiii.bhw.game.Player;
import com.adwiii.bhw.gui.BHSpace;

import java.text.DecimalFormatSymbols;
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

    private ArrayList<Player> players;

    BHSpace space;
    RadioGroup radioGroup;

    int width = 0;
    int height = 0;

    int diff;

    ArrayList<Point> p1Home;
    ArrayList<Point> p2Home;
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        Log.e("BHW", "Game");

        diff = getIntent().getIntExtra(DIFF, 0);

        players = new ArrayList<>();
        players.add(new Player(getIntent().getStringExtra(P1_NAME), diff, p1Home));
        players.add(new Player(getIntent().getStringExtra(P2_NAME), diff, p2Home));

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.VERTICAL);
        space = new BHSpace(this);
        radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        top.addView(radioGroup);
        top.addView(space);

        initGUI();

        setContentView(top);

        hideSystemUI();

//        setContentView(R.layout.activity_game);


    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        space.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void initGUI() {
        RadioButton rb;
        for (int i = 0; i < Player.getNumDiffs(diff); i++) {
            rb = new RadioButton(this);
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_enabled}, //disabled
                            new int[]{android.R.attr.state_enabled}  //enabled
                    },
                    new int[] {
                            Color.DKGRAY //disabled
                            , Player.cols[Player.cols.length - Player.getNumDiffs(diff) + i] //enabled
                    }
            );
            rb.setButtonTintList(colorStateList);
            int z = getCurrentPlayer().getAvailableCount(i);
            rb.setText(""+(z == -1 ? DecimalFormatSymbols.getInstance().getInfinity() : z)
            ); // THIS MUST BE A STRING
            Log.e("RADIO", rb.getText()+"");
//            rb.setRotation(270f);
            radioGroup.addView(rb);
        }

        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                currentSelectedPriority = group.indexOfChild(findViewById(checkedId));
            }
        });

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
        Point[][] points = new Point[width][height];
        Point p;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ((j < height / 2) ? p1Home : p2Home).add(p = new Point(i, j));
                points[i][j] = p;
            }
        }
        space.setPoints(points);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (space != null) {
            this.hideSystemUI();
        }
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

    public ArrayList<BH> getAll() {
        ArrayList<BH> all = new ArrayList<>();
        for (Player p : players) {
            all.addAll(p.getBHs());
        }
        return all;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void playBH(int x, int y) {
        players.get(turn).addBH(x, y, currentSelectedPriority);
    }

    public Player getCurrentPlayer() {
        return players.get(turn % players.size());
    }
}
