package net.taviscaron.airliners.activities;

import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import net.taviscaron.airliners.data.ImageLoader;
import net.taviscaron.airliners.fragments.FirstFragment;
import net.taviscaron.airliners.fragments.SecondFragment;
import net.taviscaron.airliners.util.TabUtil;

public class MainActivity extends SherlockFragmentActivity {
    private ImageLoader imageLoader;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        TabUtil.addFragmentTab(this, "First", "first", FirstFragment.class);
        TabUtil.addFragmentTab(this, "Second", "second", SecondFragment.class);

        imageLoader = new ImageLoader(this, ImageLoader.IMAGE_CACHE_TAG);

    }

}
