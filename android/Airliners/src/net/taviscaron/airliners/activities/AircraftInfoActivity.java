package net.taviscaron.airliners.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.fragments.AircraftInfoFragment;
import net.taviscaron.airliners.model.AircraftPhoto;

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

    private AircraftInfoFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aircraft_info_fragment);

        fragment = (AircraftInfoFragment)getSupportFragmentManager().findFragmentById(R.id.aircraft_info_fragment);

        // state is not restoring
        if(savedInstanceState == null) {
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
            case R.id.aircraft_info_action_share:
                shareAircraftPhoto();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    private void setImageAsWallpaper() {
        String photoPath = fragment.getAircraftPhotoPath();
        if(photoPath != null && new File(photoPath).exists()) {
            startActivity(new Intent(SetWallpaperActivity.SET_WALLPAPER_ACTION).putExtra(SetWallpaperActivity.IMAGE_PATH_EXTRA, photoPath));
        }
    }

    private void shareAircraftPhoto() {
        String photoPath = fragment.getAircraftPhotoPath();
        AircraftPhoto aircraftPhoto = fragment.getAircraftPhoto();
        if(photoPath == null || aircraftPhoto == null) {
            return;
        }

        File photoFile = new File(photoPath);
        if(photoFile.exists()) {
            String siteUrl = getString(R.string.config_site_photo_url, aircraftPhoto.getId());
            String text = String.format("%s %s %s", aircraftPhoto.getAirline(), aircraftPhoto.getAircraft(), siteUrl);
            Uri photoUri = Uri.fromFile(photoFile);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.action_share)));
        }
    }
}
