package com.tixon.barchart;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by tikhon.osipov on 12.08.2016
 */
public class Utils {
    public static float dpToPx(float dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return dp * (displayMetrics.xdpi / (float)DisplayMetrics.DENSITY_DEFAULT);
    }
}
