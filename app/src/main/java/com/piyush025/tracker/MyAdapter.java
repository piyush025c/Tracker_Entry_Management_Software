//Custom Adapter to Implement Tabs.

package com.piyush025.tracker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {

    Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                Checkin tab1 = new Checkin();
                return tab1;
            case 1:
                Checkout tab2= new Checkout();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
