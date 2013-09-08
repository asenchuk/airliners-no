package net.taviscaron.airliners.util;

import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Different UI util
 * @author Andrei Senchuk
 */
public class UIUtil {
    public static void setAlpha(View view, float alpha) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setAlpha(alpha);
        } else {
            Animation animation = new AlphaAnimation(1, alpha);
            animation.setDuration(0);
            animation.setFillAfter(true);
            view.startAnimation(animation);
        }
    }
}
