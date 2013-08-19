package net.taviscaron.airliners.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import net.taviscaron.airliners.fragments.AircraftInfoFragment;

/**
 * Shows aircraft information
 * @author Andrei Senchuk
 */
public class AircraftInfoActivity extends SherlockFragmentActivity {
    public static final String AIRCRAFT_INFO_ACTION = "net.taviscaron.airliners.AIRCRAFT_INFO";
    public static final String AIRCRAFT_ID_KEY = "aircraftId";
    private static final String AIRCRAFT_INFO_FRAGMENT_TAG = "aircraftInfoFragment";

    private AircraftInfoFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        fragment = (AircraftInfoFragment)fm.findFragmentByTag(AIRCRAFT_INFO_FRAGMENT_TAG);
        if(fragment == null) {
            fragment = new AircraftInfoFragment();
            fm.beginTransaction().add(android.R.id.content, fragment, AIRCRAFT_INFO_FRAGMENT_TAG).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        if(intent != null) {
            String id = intent.getExtras().getString(AIRCRAFT_ID_KEY);
            if(id != null) {
                fragment.loadAircraftInfo(id);
            }
        }
    }
}
