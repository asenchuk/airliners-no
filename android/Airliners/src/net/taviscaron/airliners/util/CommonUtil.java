package net.taviscaron.airliners.util;

import android.text.TextUtils;

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
}
