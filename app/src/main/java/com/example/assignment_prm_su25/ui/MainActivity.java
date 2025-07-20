package com.example.assignment_prm_su25.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.assignment_prm_su25.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabMain;
    private Toolbar toolbar;
    
    // Simple Fragment class for testing
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            int sectionNumber = 1;
            if (getArguments() != null) {
                sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            }
            textView.setText(getString(R.string.section_format, sectionNumber));
            return rootView;
        }
    }
    
    // Simple ViewPager adapter
    public class SectionsPagerAdapter extends FragmentStateAdapter {
        public SectionsPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getItemCount() {
            return 2; // Only 2 fragments for now
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize views
        initViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup view pager and fragments
        setupViewPager();
        
        // Setup bottom navigation
        setupBottomNavigation();
        
        // Setup FAB click listener
        setupFab();
    }
    
    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabMain = findViewById(R.id.fabMain);
        toolbar = findViewById(R.id.toolbar);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }
    
    private void setupViewPager() {
        // Create adapter for view pager with just 2 fragments
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        
        // Disable swiping between tabs
        viewPager.setUserInputEnabled(false);
    }
    
    private void setupBottomNavigation() {
        // Set default selected item
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        
        // Handle item selection - simplified to just 2 items
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.navigation_home) {
                viewPager.setCurrentItem(0, false);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                viewPager.setCurrentItem(1, false);
                return true;
            }
            
            return false;
        });
        
        // Handle page changes to update bottom navigation
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                } else if (position == 1) {
                    bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
                }
            }
        });
    }
    
    private void setupFab() {
        fabMain.setOnClickListener(v -> {
            // Handle FAB click
            Toast.makeText(this, getString(R.string.action_add), Toast.LENGTH_SHORT).show();
            // TODO: Implement FAB click action
        });
        
        // Show/hide FAB based on current page - only show on home
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Show FAB only on home tab
                if (position == 0) {
                    fabMain.show();
                } else {
                    fabMain.hide();
                }
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.action_settings) {
            // Handle settings action
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.action_logout) {
            // Handle logout action
            finish();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        // If not on home screen, go to home
        if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0, true);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else {
            // If on home screen, minimize the app
            moveTaskToBack(true);
        }
    }
}