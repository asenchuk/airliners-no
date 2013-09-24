package net.taviscaron.airliners.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;
import net.taviscaron.airliners.R;

/**
 * A couple of common utils
 * @author Andrei Senchuk
 */
public class CommonUtil {
    public static boolean isAllEmpty(String... strings) {
        Validate.makeSure(strings.length > 0, "Arguments list should include at least one string");
        for(String s : strings) {
            if(!TextUtils.isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        return isNetworkAvailable(context, false);
    }


    public static boolean isNetworkAvailable(Context context, boolean showToastMessage) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isNetworkAvailable = (activeNetworkInfo != null && activeNetworkInfo.isConnected());

        if(!isNetworkAvailable && showToastMessage) {
            Toast.makeText(context, R.string.error_network_unavailable, Toast.LENGTH_SHORT).show();
        }

        return isNetworkAvailable;
    }
}
