//Activity that manages check-in and check-out fragments.
package com.piyush025.tracker;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Home extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        viewPager=(ViewPager)findViewById(R.id.pager);

        tabLayout.addTab(tabLayout.newTab().setText("Check In"));       //Adding Check-in Tab
        tabLayout.addTab(tabLayout.newTab().setText("Check Out"));      //Adding Check-out Tab

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Code for bridging custom adapter and ViewPager instance viewPager.
        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());    //sets the viewPager to tab selected.
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), Track.class);
        startActivity(i);
    }
}
