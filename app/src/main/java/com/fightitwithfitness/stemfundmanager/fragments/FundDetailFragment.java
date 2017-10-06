package com.fightitwithfitness.stemfundmanager.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.fightitwithfitness.stemfundmanager.App;
import com.fightitwithfitness.stemfundmanager.R;
import com.fightitwithfitness.stemfundmanager.models.Fund;
import com.fightitwithfitness.stemfundmanager.utils.WSApi;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jpriv on 9/27/2017.
 */

public class FundDetailFragment extends Fragment {

    private static final String ARG_FUND_ID = "fund_id";

    private Fund mFund;
    private int mFundId;
    private TextView mId;
    private TextView mInvestmentName;
    private TextView mAgency;
    private TextView mSubAgency;
    private TextView mBriefDescription;
    private TextView mYearEstablished;
    private TextView mFundingFY2008;
    private TextView mFundingFY2009;
    private TextView mFundingFY2010;
    private TextView mMissionSpecificOrGeneralStem;
    private TextView mAgencyOrMissionRelatedNeeds;
    private TextView mPrimaryInvestmentObjective;
    private Context mContext;

    public Callbacks mCallbacks;

    public interface Callbacks{
        void onItemSelected(Fund fund);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }

    public static Fragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FUND_ID, id);

        FundDetailFragment fragment = new FundDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
        mFundId = getArguments().getInt(ARG_FUND_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fund_detail, container, false);

        if(App.get().isNetworkAvailableAndConnected(mContext)){
            getFundDetailsFromWebservice(mFundId);
        }else{
            Toast.makeText(mContext, "Please check network availability", Toast.LENGTH_SHORT).show();
        }

        mId = (TextView) v.findViewById(R.id.tv_fund_id);
        mInvestmentName = (TextView) v.findViewById(R.id.tv_fund_investment_name);
        mAgency = (TextView) v.findViewById(R.id.tv_fund_agency);
        mSubAgency = (TextView) v.findViewById(R.id.tv_fund_sub_agency);
        mBriefDescription = (TextView) v.findViewById(R.id.tv_fund_brief_description);
        mYearEstablished = (TextView) v.findViewById(R.id.tv_fund_year_established);
        mFundingFY2008 = (TextView) v.findViewById(R.id.tv_fund_fy_2008);
        mFundingFY2009 = (TextView) v.findViewById(R.id.tv_fund_fy_2009);
        mFundingFY2010 = (TextView) v.findViewById(R.id.tv_fund_fy_2010);
        mMissionSpecificOrGeneralStem = (TextView) v.findViewById(R.id.tv_fund_mission_specific_or_general_stem);
        mAgencyOrMissionRelatedNeeds = (TextView) v.findViewById(R.id.tv_fund_agency_or_mission_related_needs);
        mPrimaryInvestmentObjective= (TextView) v.findViewById(R.id.tv_fund_primary_investment_objective);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.update_fund_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_update_fund:
                mCallbacks.onItemSelected(mFund);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI(Fund fund){
        mId.setText(String.valueOf(fund.getId()));
        mInvestmentName.setText(fund.getInvestmentName());
        mAgency.setText(fund.getAgency());
        mSubAgency.setText(fund.getSubAgency());
        mBriefDescription.setText(fund.getBriefDescription());
        mYearEstablished.setText(String.valueOf(fund.getYearEstablished()));
        mFundingFY2008.setText(String.valueOf(fund.getFundingFY2008()));
        mFundingFY2009.setText(String.valueOf(fund.getFundingFY2009()));
        mFundingFY2010.setText(String.valueOf(fund.getFundingFY2010()));
        mMissionSpecificOrGeneralStem.setText(fund.getMissionSpecificOrGeneralStem());
        mAgencyOrMissionRelatedNeeds.setText(fund.getAgencyOrMissionRelatedNeeds());
        mPrimaryInvestmentObjective.setText(fund.getPrimaryInvestmentObjective());
    }

    //Webservice call to retrieve individual funds details
    private void getFundDetailsFromWebservice(int fundId) {
        try {
            App.get().initLoadingDialog(mContext);
            Request request = new Request.Builder().url(WSApi.BASE_URL + String.format(WSApi.ID, fundId)).addHeader("Accept", WSApi.CONTENT_TYPE_VALUE).build();

            if (request != null) {
                WSApi.getHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        App.get().stopLoadingDialog();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        if (!response.isSuccessful()) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response.body().string());
                                Log.e("Error getting fund list", jsonResponse.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                App.get().stopLoadingDialog();
                            }
                        }
                        if (response.isSuccessful()) {
                            try {
                                JSONObject JsonFund = new JSONObject(response.body().string());
                                Log.v("FUNDS", JsonFund.toString());

                                mFund = new Fund();

                                if(!JsonFund.isNull("Id")){
                                    mFund.setId(JsonFund.getInt("Id"));
                                }

                                if(!JsonFund.isNull("InvestmentName")){
                                    mFund.setInvestmentName(JsonFund.getString("InvestmentName"));
                                }

                                if(!JsonFund.isNull("Agency")){
                                    mFund.setAgency(JsonFund.getString("Agency"));
                                }

                                if(!JsonFund.isNull("Subagency")){
                                    mFund.setSubAgency(JsonFund.getString("Subagency"));
                                }

                                if(!JsonFund.isNull("BriefDescription")){
                                    mFund.setBriefDescription(JsonFund.getString("BriefDescription"));
                                }

                                if(!JsonFund.isNull("YearEstablished")){
                                    mFund.setYearEstablished(JsonFund.getInt("YearEstablished"));
                                }

                                if(!JsonFund.isNull("FundingFY2008")){
                                    mFund.setFundingFY2008(JsonFund.getDouble("FundingFY2008"));
                                }

                                if(!JsonFund.isNull("FundingFY2009")){
                                    mFund.setFundingFY2009(JsonFund.getDouble("FundingFY2009"));
                                }

                                if(!JsonFund.isNull("FundingFY2010")){
                                    mFund.setFundingFY2010(JsonFund.getDouble("FundingFY2010"));
                                }

                                if(!JsonFund.isNull("MissionSpecificOrGeneralStem")){
                                    mFund.setMissionSpecificOrGeneralStem(JsonFund.getString("MissionSpecificOrGeneralStem"));
                                }

                                if(!JsonFund.isNull("AgencyOrMissionRelatedNeeds")){
                                    mFund.setAgencyOrMissionRelatedNeeds(JsonFund.getString("AgencyOrMissionRelatedNeeds"));
                                }

                                if(!JsonFund.isNull("PrimaryInvestmentObjective")){
                                    mFund.setPrimaryInvestmentObjective(JsonFund.getString("PrimaryInvestmentObjective"));
                                }

                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        updateUI(mFund);
                                        App.get().stopLoadingDialog();
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                                App.get().stopLoadingDialog();
                            }
                        }
                    }
                });
            } else {
                Log.v("REQUEST", "Request can not be null");
                App.get().stopLoadingDialog();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            App.get().stopLoadingDialog();
        }
    }
}