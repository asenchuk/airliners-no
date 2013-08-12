package net.taviscaron.airliners.test;

import net.taviscaron.airliners.activities.MainActivity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * User: andrey.senchuk
 * Date: 8/9/13
 * Time: 5:34 PM
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void testActivity() {
        MainActivity activity = getActivity();
    }
}
