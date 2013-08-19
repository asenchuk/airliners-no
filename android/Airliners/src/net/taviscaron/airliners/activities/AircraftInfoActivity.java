package net.taviscaron.airliners.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import net.taviscaron.airliners.fragments.AircraftInfoFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        } else {
            // fragment is already created - just restore state
            setIntent(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        String id = null;

        Intent intent = getIntent();
        if(intent != null) {
            if(AIRCRAFT_INFO_ACTION.equals(intent.getAction())) {
                id = intent.getStringExtra(AIRCRAFT_ID_KEY);
            } else if(Intent.ACTION_VIEW.equals(intent.getAction())) {
                Uri uri = intent.getData();
                Matcher matcher = Pattern.compile("/photo(/.*?)*?/(\\d+)").matcher(uri.getPath());
                if(matcher.find()) {
                    id = matcher.group(2);
                }
            }
        }

        if(id != null) {
            fragment.loadAircraftInfo(id);
        }
    }
}
