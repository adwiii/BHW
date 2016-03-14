package com.adwiii.bhw;

import android.app.Activity;
import android.view.Window;

/**
 * Created by Trey on 3/9/2016.
 */
public class Util {
    public static void removeTitle(Activity a) {
        a.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        a.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
