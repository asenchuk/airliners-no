package net.taviscaron.airliners.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.adapters.SearchResultsAdapter;
import net.taviscaron.airliners.fragments.SearchResultsFragment;
import net.taviscaron.airliners.model.SearchParams;
import net.taviscaron.airliners.util.TabUtil;

public class MainActivity extends SherlockFragmentActivity implements SearchResultsFragment.OnShowAircraftInfoListener {
    private static final String TOP15_TAB_TAG = "top15";
    private static final String TOP_TAB_TAG = "top";
    private static final String SAVED_TAB_KEY = "savedTab";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        TabUtil.addFragmentTab(this, R.string.tab_top15_title,  TOP15_TAB_TAG, new TabUtil.FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return SearchResultsFragment.newInstance(SearchResultsAdapter.LoaderType.TOP15, new SearchParams());
            }
        });

        TabUtil.addFragmentTab(this, R.string.tab_top_title, TOP_TAB_TAG, new TabUtil.FragmentCreator() {
            @Override
            public Fragment createFragment() {
                return SearchResultsFragment.newInstance(SearchResultsAdapter.LoaderType.TOP, new SearchParams());
            }
        });

        if(savedInstanceState != null) {
            int selectedTab = savedInstanceState.getInt(SAVED_TAB_KEY, 0);
            actionBar.getTabAt(selectedTab).select();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_TAB_KEY, getSupportActionBar().getSelectedTab().getPosition());
    }

    @Override
    public void showAircraftInfo(String id) {
        startActivity(new Intent(AircraftInfoActivity.AIRCRAFT_INFO_ACTION).putExtra(AircraftInfoActivity.AIRCRAFT_ID_KEY, id));
    }
}
