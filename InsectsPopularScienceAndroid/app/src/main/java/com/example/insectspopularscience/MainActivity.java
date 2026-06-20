package com.example.insectspopularscience;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.insectspopularscience.fragment.ArticleFragment;
import com.example.insectspopularscience.fragment.CommunityFragment;
import com.example.insectspopularscience.fragment.HomeFragment;
import com.example.insectspopularscience.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                switchFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_article) {
                switchFragment(new ArticleFragment());
                return true;
            } else if (itemId == R.id.nav_community) {
                switchFragment(new CommunityFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                switchFragment(new ProfileFragment());
                return true;
            }
            return false;
        });

        // 默认显示首页
        switchFragment(new HomeFragment());
    }

    private void switchFragment(Fragment fragment) {
        if (currentFragment == null || !currentFragment.getClass().equals(fragment.getClass())) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            currentFragment = fragment;
        }
    }
}