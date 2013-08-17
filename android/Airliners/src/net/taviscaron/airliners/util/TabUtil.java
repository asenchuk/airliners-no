package net.taviscaron.airliners.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Some reusable chunks of code
 * @author Andrei Senchuk
 */
public class  TabUtil {
    public static void addFragmentTab(SherlockFragmentActivity activity, int tabNameId, String tabTag, FragmentCreator creator) {
        ActionBar actionBar = activity.getSupportActionBar();
        ActionBar.Tab tab = actionBar.newTab();
        tab.setText(activity.getString(tabNameId));

        final FragmentCreator finalCreator = creator;
        tab.setTabListener(new TabListener(activity, tabTag) {
            @Override
            public Fragment instantiateFragment() {
                return finalCreator.createFragment();
            }
        });

        actionBar.addTab(tab);
    }

    public interface FragmentCreator {
        public Fragment createFragment();
    }

    public static abstract class TabListener implements ActionBar.TabListener {
        private Fragment fragment;
        private final FragmentActivity activity;
        private final String tag;

        public TabListener(FragmentActivity activity, String tag) {
            this.activity = activity;
            this.tag = tag;
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if(fragment == null) {
                fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
            }

            if(fragment == null) {
                fragment = instantiateFragment();
                ft.add(android.R.id.content, fragment, tag);
            }

            ft.attach(fragment);
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (fragment != null) {
                ft.detach(fragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // noop
        }

        public abstract Fragment instantiateFragment();
    }
}
