package com.fightitwithfitness.stemfundmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.fightitwithfitness.stemfundmanager.R;
import com.fightitwithfitness.stemfundmanager.fragments.AddFundFragment;
import com.fightitwithfitness.stemfundmanager.fragments.FundListFragment;

public class MainActivity extends FragmentHostingActivity implements FundListFragment.Callbacks {

    public static Intent newIntent(Context context){
        Intent i = new Intent(context, MainActivity.class);
        return i;
    }

    @Override
    public Fragment createFragment() {
        return FundListFragment.newInstance();
    }

    @Override
    public void onItemSelected() {
        Fragment fragment = AddFundFragment.newInstance();
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
