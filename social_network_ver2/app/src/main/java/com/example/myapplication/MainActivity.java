package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Activity.LoginActivity;
import com.example.myapplication.Adapter.MyViewPagerAdapter;
import com.example.myapplication.Fragment.AddPostFragment;
import com.example.socialnetwork.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager2;
    Toolbar toolbar;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setOnItemOfBottomNav();
        setupViewPager();

        setSupportActionBar(toolbar);
        MainActivity.this.setTitle("My Profile");
        toolbar.setVisibility(View.GONE);
    }

    void setOnItemOfBottomNav() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        viewPager2.setCurrentItem(0);
                        toolbar.setVisibility(View.GONE);
                        break;
                    case R.id.action_notification:
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        viewPager2.setCurrentItem(1);
                        toolbar.setVisibility(View.GONE);
                        break;
                    case R.id.action_add:
                        transaction.replace(R.id.action_add, new AddPostFragment());
                        viewPager2.setCurrentItem(2);
                        toolbar.setVisibility(View.GONE);
                        break;
                    case R.id.action_search:
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        viewPager2.setCurrentItem(3);
                        toolbar.setVisibility(View.GONE);
                        break;
                    case R.id.action_profile:
                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                        toolbar.setVisibility(View.VISIBLE);
                        viewPager2.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
    }

    private void setupViewPager() {
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager2.setAdapter(adapter);
        viewPager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        toolbar.setVisibility(View.GONE);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.action_notification).setChecked(true);
                        toolbar.setVisibility(View.GONE);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.action_add).setChecked(true);
                        toolbar.setVisibility(View.GONE);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.action_search).setChecked(true);
                        toolbar.setVisibility(View.GONE);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.action_profile).setChecked(true);
                        toolbar.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void initView() {
        bottomNavigationView = findViewById(R.id.bottomBar);
        viewPager2 = findViewById(R.id.viewPager);
        toolbar = findViewById(R.id.toolbar2);
    }
}