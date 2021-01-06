package com.krizz.introtucedemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements EnrollFragment.AddUserUpdate {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabsLayout);
        viewPager = findViewById(R.id.viewpager);

        getTabs();

    }

    public void getTabs(){
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        new Handler().post(new Runnable() {

            @Override
            public void run() {
                viewPagerAdapter.addFragment(UsersFragment.getInstance(),"Users");
                viewPagerAdapter.addFragment(EnrollFragment.getInstance(),"Enroll");

                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    @Override
    public void onAddUserClick() {
        viewPager.getAdapter().notifyDataSetChanged();
    }
}