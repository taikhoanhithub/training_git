package com.example.myapplication.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.Fragment.AddFragment;
import com.example.myapplication.Fragment.HomeFragment;
import com.example.myapplication.Fragment.NotificationFragment;
import com.example.myapplication.Fragment.ProfileFragment;
import com.example.myapplication.Fragment.SearchFragment;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {

    public MyViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new NotificationFragment();
            case 2:
                return new AddFragment();
            case 3:
                return new SearchFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
