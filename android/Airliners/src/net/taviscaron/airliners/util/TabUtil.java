package net.taviscaron.airliners.util;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Some reusable chunks of code
 * @author Andrei Senchuk
 */
public class TabUtil {
    public static void addFragmentTab(SherlockFragmentActivity activity, String tabName, String tabTag, Class<? extends Fragment> clazz) {
        ActionBar actionBar = activity.getSupportActionBar();
        ActionBar.Tab tab = actionBar.newTab();
        tab.setText(tabName);
        tab.setTabListener(new DefaultTabListener(activity, tabTag, clazz));
        actionBar.addTab(tab);
    }

    public static void addFragmentTab(SherlockFragmentActivity activity, int tabNameId, String tabTag, Class<? extends Fragment> clazz) {
        String tabName = activity.getString(tabNameId);
        addFragmentTab(activity, tabName, tabTag, clazz);
    }

    public static class DefaultTabListener implements ActionBar.TabListener {
        private Fragment fragment;
        private final Activity activity;
        private final String tag;
        private final Class clazz;

        public DefaultTabListener(Activity activity, String tag, Class<? extends Fragment> clazz) {
            this.activity = activity;
            this.tag = tag;
            this.clazz = clazz;
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (fragment == null) {
                fragment = Fragment.instantiate(activity, clazz.getName());
                ft.add(android.R.id.content, fragment, tag);
            } else {
                ft.attach(fragment);
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (fragment != null) {
                ft.detach(fragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }
}
