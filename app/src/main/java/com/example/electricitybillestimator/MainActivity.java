package com.example.electricitybillestimator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.electricitybillestimator.fragments.AboutFragment;
import com.example.electricitybillestimator.fragments.CalculateFragment;
import com.example.electricitybillestimator.fragments.SavedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// Remove setSupportActionBar()
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Electriary");

// Optional: If you want to change title dynamically later
// toolbarTitle.setText("New Title");


        bottomNav = findViewById(R.id.bottomNavigationView);
        loadFragment(new CalculateFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_calculate) {
                selectedFragment = new CalculateFragment();
            } else if (item.getItemId() == R.id.nav_saved) {
                selectedFragment = new SavedFragment();
            }else if (item.getItemId() == R.id.nav_about) {
                selectedFragment = new AboutFragment();
            }
            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
