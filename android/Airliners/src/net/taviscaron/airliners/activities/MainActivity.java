package net.taviscaron.airliners.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import net.taviscaron.airliners.R;
import net.taviscaron.airliners.adapters.SearchResultsAdapter;
import net.taviscaron.airliners.fragments.SearchFragment;
import net.taviscaron.airliners.fragments.SearchResultsFragment;
import net.taviscaron.airliners.model.SearchParams;

import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;

public class MainActivity extends SherlockFragmentActivity implements SearchResultsFragment.OnShowAircraftInfoListener, SearchFragment.OnSearchListener {
    private static final String SAVED_TAB_KEY = "savedTab";
    private static final String BACK_STACKS_KEY = "backStacks";

    private enum Tab {
        TOP15(R.string.tab_top15_title, SearchResultsFragment.class, new FragmentConfigurator() {
            @Override
            public void configureFragmentArguments(Bundle args) {
                args.putInt(SearchResultsFragment.LOADER_TYPE_KEY, SearchResultsAdapter.LoaderType.TOP15.ordinal());
                args.putSerializable(SearchResultsFragment.SEARCH_PARAMS_KEY, new SearchParams());
            }
        }),

        TOP(R.string.tab_top_title, SearchResultsFragment.class, new FragmentConfigurator() {
            @Override
            public void configureFragmentArguments(Bundle args) {
                args.putInt(SearchResultsFragment.LOADER_TYPE_KEY, SearchResultsAdapter.LoaderType.TOP.ordinal());
                args.putSerializable(SearchResultsFragment.SEARCH_PARAMS_KEY, new SearchParams());
            }
        }),

        SEARCH(R.string.tab_search_title, SearchFragment.class, null);

        private interface FragmentConfigurator {
            public void configureFragmentArguments(Bundle args);
        }

        private final int titleId;
        private Class<? extends Fragment> clazz;
        private FragmentConfigurator configurator;

        private Tab(int titleId, Class<? extends Fragment> clazz, FragmentConfigurator configurator) {
            this.titleId = titleId;
            this.configurator = configurator;
            this.clazz = clazz;
        }
    }

    private HashMap<Tab, Stack<String>> backStacks;

    private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            Tab tabType = (Tab)tab.getTag();
            Stack<String> backStack = backStacks.get(tabType);

            if(backStack.isEmpty()) {
                Fragment fragment = Fragment.instantiate(MainActivity.this, tabType.clazz.getCanonicalName());
                if(tabType.configurator != null) {
                    Bundle args = new Bundle();
                    tabType.configurator.configureFragmentArguments(args);
                    fragment.setArguments(args);
                }

                addFragment(fragment, backStack, ft);
            } else {
                showFragment(backStack, ft);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            Tab tabType = (Tab)tab.getTag();
            Stack<String> backStack = backStacks.get(tabType);
            String topTag = backStack.peek();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(topTag);
            ft.detach(fragment);
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // TODO: back navigation?
        }
    };

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        if(savedInstanceState != null) {
            // restore back stack state
            backStacks = (HashMap<Tab, Stack<String>>)savedInstanceState.getSerializable(BACK_STACKS_KEY);
        } else {
            // create back stack
            backStacks = new HashMap<Tab, Stack<String>>();
            for(Tab tab : Tab.values()) {
                backStacks.put(tab, new Stack<String>());
            }
        }

        // init tabs
        for(Tab tab : Tab.values()) {
            actionBar.addTab(actionBar.newTab().setTag(tab).setText(tab.titleId).setTabListener(tabListener));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int selectedTab = savedInstanceState.getInt(SAVED_TAB_KEY, 0);
        getSupportActionBar().getTabAt(selectedTab).select();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_TAB_KEY, getSupportActionBar().getSelectedTab().getPosition());
        outState.putSerializable(BACK_STACKS_KEY, backStacks);
    }

    @Override
    public void onBackPressed() {
        ActionBar.Tab tab = getSupportActionBar().getSelectedTab();
        Tab tabType = (Tab)tab.getTag();
        Stack<String> backStack = backStacks.get(tabType);

        String tag = backStack.pop();
        if(backStack.isEmpty()) {
            super.onBackPressed();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            // TODO: animation?

            Fragment fragment = fragmentManager.findFragmentByTag(tag);
            ft.remove(fragment);

            showFragment(backStack, ft);

            ft.commit();
        }
    }

    private void addFragment(Fragment fragment) {
        ActionBar.Tab tab = getSupportActionBar().getSelectedTab();
        Tab tabType = (Tab)tab.getTag();

        Stack<String> backStack = backStacks.get(tabType);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        // TODO: animation?

        String topTag = backStack.peek();
        Fragment topFragment = fragmentManager.findFragmentByTag(topTag);
        ft.detach(topFragment);

        addFragment(fragment, backStack, ft);

        ft.commit();
    }

    private void addFragment(Fragment fragment, Stack<String> backStack, FragmentTransaction ft) {
        String tag = UUID.randomUUID().toString();
        ft.add(android.R.id.content, fragment, tag);
        backStack.push(tag);
    }

    private void showFragment(Stack<String> backStack, FragmentTransaction ft) {
        String tag = backStack.peek();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        ft.attach(fragment);
    }

    public void showAircraftInfo(String id) {
        startActivity(new Intent(AircraftInfoActivity.AIRCRAFT_INFO_ACTION).putExtra(AircraftInfoActivity.AIRCRAFT_ID_KEY, id));
    }

    public void onSearch(SearchParams params) {
        addFragment(SearchResultsFragment.newInstance(SearchResultsAdapter.LoaderType.SEARCH, params));
    }
}
