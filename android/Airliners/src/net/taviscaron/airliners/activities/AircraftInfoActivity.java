package net.taviscaron.airliners.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.fragments.AircraftInfoFragment;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shows aircraft information
 * @author Andrei Senchuk
 */
public class AircraftInfoActivity extends SherlockFragmentActivity {
    public static final String AIRCRAFT_INFO_ACTION = "net.taviscaron.airliners.AIRCRAFT_INFO";
    public static final String AIRCRAFT_ID_KEY = "aircraftId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aircraft_info_fragment);

        // state is not restoring
        if(savedInstanceState == null) {
            AircraftInfoFragment fragment = (AircraftInfoFragment)getSupportFragmentManager().findFragmentById(R.id.aircraft_info_fragment);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.aircraft_info_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = true;
        switch (item.getItemId()) {
            case R.id.aircraft_info_action_set_wallpaper:
                setImageAsWallpaper();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    private void setImageAsWallpaper() {
        AircraftInfoFragment fragment = (AircraftInfoFragment)getSupportFragmentManager().findFragmentById(R.id.aircraft_info_fragment);
        String photoPath = fragment.getAircraftPhotoPath();
        if(photoPath != null && new File(photoPath).exists()) {
            startActivity(new Intent(SetWallpaperActivity.SET_WALLPAPER_ACTION).putExtra(SetWallpaperActivity.IMAGE_PATH_EXTRA, photoPath));
        }
    }
}
