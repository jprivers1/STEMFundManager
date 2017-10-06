package com.fightitwithfitness.stemfundmanager.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.fightitwithfitness.stemfundmanager.R;
import com.fightitwithfitness.stemfundmanager.fragments.AddFundFragment;
import com.fightitwithfitness.stemfundmanager.fragments.FundDetailFragment;
import com.fightitwithfitness.stemfundmanager.fragments.UpdateFundFragment;
import com.fightitwithfitness.stemfundmanager.models.Fund;

/**
 * Created by jpriv on 9/27/2017.
 */

public class FundDetailActivity extends FragmentHostingActivity implements FundDetailFragment.Callbacks {

    private static final String EXTRA_FUND_ID = "fund_id";

    public static Intent newIntent(Context context, int fundId){
        Intent i = new Intent(context, FundDetailActivity.class);
        i.putExtra(EXTRA_FUND_ID, fundId);
        return i;
    }

    @Override
    protected Fragment createFragment() {
        int fundId = (Integer) getIntent().getSerializableExtra(EXTRA_FUND_ID);
        return FundDetailFragment.newInstance(fundId);
    }

    @Override
    public void onItemSelected(Fund fund) {
        Fragment fragment = UpdateFundFragment.newInstance(fund);
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}