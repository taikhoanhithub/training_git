package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapter.NotificationViewPagerAdapter;
import com.example.socialnetwork.R;
import com.google.android.material.tabs.TabLayout;

public class NotificationFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        viewPager = view.findViewById(R.id.viewPager2);
        // add 2 fragment vao viewPager
        viewPager.setAdapter(new NotificationViewPagerAdapter(getParentFragmentManager()));
        tabLayout = view.findViewById(R.id.tabLayout);
        // setup viewPager vao tabLayout
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
