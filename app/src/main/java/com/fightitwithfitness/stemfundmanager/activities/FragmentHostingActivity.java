package com.fightitwithfitness.stemfundmanager.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import com.fightitwithfitness.stemfundmanager.R;

/**
 * Created by jpriv on 9/27/2017.
 *
 * Abstract base activity for hosting an individual fragment
 * Subclass must override createFragment()
 *
 */

public abstract class FragmentHostingActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    protected FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(savedInstanceState == null) {
            if (fragment == null) {
                fragment = createFragment();
                fm.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
        }
    }
}
