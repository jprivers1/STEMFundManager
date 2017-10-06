package com.fightitwithfitness.stemfundmanager.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.fightitwithfitness.stemfundmanager.App;
import com.fightitwithfitness.stemfundmanager.R;
import com.fightitwithfitness.stemfundmanager.activities.MainActivity;
import com.fightitwithfitness.stemfundmanager.models.Fund;
import com.fightitwithfitness.stemfundmanager.utils.WSApi;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jpriv on 9/28/2017.
 */

public class UpdateFundFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_FUND = "fund";

    private Fund mFund;
    private TextView mId;
    private TextView mInvestmentNameTextView;
    private EditText mInvestmentNameEditText;
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
    private Button mUpdateButton;
    private Context mContext;

    public static Fragment newInstance(Fund fund) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FUND, fund);

        UpdateFundFragment fragment = new UpdateFundFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle arguments = getArguments();

        if(arguments.containsKey(ARG_FUND)) {
            mFund = (Fund) arguments.getSerializable(ARG_FUND);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fund_detail, container, false);

        mId = (TextView) v.findViewById(R.id.tv_fund_id);
        mInvestmentNameTextView = (TextView) v.findViewById(R.id.tv_fund_investment_name);
        mInvestmentNameEditText = (EditText) v.findViewById(R.id.et_fund_investment_name);
        mAgency = (TextView) v.findViewById(R.id.tv_fund_agency);
        mSubAgency = (TextView) v.findViewById(R.id.tv_fund_sub_agency);
        mBriefDescription = (TextView) v.findViewById(R.id.tv_fund_brief_description);
        mYearEstablished = (TextView) v.findViewById(R.id.tv_fund_year_established);
        mFundingFY2008 = (TextView) v.findViewById(R.id.tv_fund_fy_2008);
        mFundingFY2009 = (TextView) v.findViewById(R.id.tv_fund_fy_2009);
        mFundingFY2010 = (TextView) v.findViewById(R.id.tv_fund_fy_2010);
        mMissionSpecificOrGeneralStem = (TextView) v.findViewById(R.id.tv_fund_mission_specific_or_general_stem);
        mAgencyOrMissionRelatedNeeds = (TextView) v.findViewById(R.id.tv_fund_agency_or_mission_related_needs);
        mPrimaryInvestmentObjective = (TextView) v.findViewById(R.id.tv_fund_primary_investment_objective);
        mUpdateButton = (Button) v.findViewById(R.id.bt_update_fund);
        mUpdateButton.setVisibility(View.VISIBLE);
        mUpdateButton.setOnClickListener(this);

        //Populate UI
        mId.setText(String.valueOf(mFund.getId()));
        mInvestmentNameTextView.setVisibility(View.GONE);
        mInvestmentNameEditText.setVisibility(View.VISIBLE);
        mInvestmentNameEditText.setText(mFund.getInvestmentName());
        mAgency.setText(mFund.getAgency());
        mSubAgency.setText(mFund.getSubAgency());
        mBriefDescription.setText(mFund.getBriefDescription());
        mYearEstablished.setText(String.valueOf(mFund.getYearEstablished()));
        mFundingFY2008.setText(String.valueOf(mFund.getFundingFY2008()));
        mFundingFY2009.setText(String.valueOf(mFund.getFundingFY2009()));
        mFundingFY2010.setText(String.valueOf(mFund.getFundingFY2010()));
        mMissionSpecificOrGeneralStem.setText(mFund.getMissionSpecificOrGeneralStem());
        mAgencyOrMissionRelatedNeeds.setText(mFund.getAgencyOrMissionRelatedNeeds());
        mPrimaryInvestmentObjective.setText(mFund.getPrimaryInvestmentObjective());
        mUpdateButton.setText(getResources().getString(R.string.update_fund));

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_update_fund:
                if(App.get().isNetworkAvailableAndConnected(mContext)){
                    if(checkFields()){
                        updateFundOnWebservice(createFundJsonObject());
                    }
                }else{
                    Toast.makeText(mContext, "Please check network connectivity", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private boolean checkFields() {
        if(TextUtils.isEmpty(mInvestmentNameEditText.getText())){
            Toast.makeText(mContext, "Please enter a valid investment name", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //Create Json object to update fund with webservice
    private JSONObject createFundJsonObject() {
        JSONObject fund = new JSONObject();

        try {
            fund.put("Id", Integer.parseInt(mId.getText().toString()));
            fund.put("InvestmentName", mInvestmentNameEditText.getText().toString());
            fund.put("Agency", mAgency.getText().toString());
            fund.put("Subagency", mSubAgency.getText().toString());
            fund.put("BriefDescription", mBriefDescription.getText().toString());
            fund.put("YearEstablished", Integer.parseInt(mYearEstablished.getText().toString()));
            fund.put("FundingFY2008", Double.parseDouble(mFundingFY2008.getText().toString()));
            fund.put("FundingFY2009", Double.parseDouble(mFundingFY2009.getText().toString()));
            fund.put("FundingFY2010", Double.parseDouble(mFundingFY2010.getText().toString()));
            fund.put("MissionSpecificOrGeneralStem", mMissionSpecificOrGeneralStem.getText().toString());
            fund.put("AgencyOrMissionRelatedNeeds", mAgencyOrMissionRelatedNeeds.getText().toString());
            fund.put("PrimaryInvestmentObjective", mPrimaryInvestmentObjective.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fund;
    }

    //Webservice call to update fund with webservice
    private void updateFundOnWebservice(JSONObject fund){
        try {
            App.get().initLoadingDialog(mContext);

            String fundJsonString = fund.toString();
            RequestBody body = RequestBody.create(WSApi.JSON, fundJsonString);

            Request request = new Request.Builder().url(WSApi.BASE_URL + String.format(WSApi.ID, mFund.getId())).addHeader("Accept", WSApi.CONTENT_TYPE_VALUE)
                        .addHeader(WSApi.CONTENT_TYPE, WSApi.CONTENT_TYPE_VALUE).put(body).build();

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
                                Log.e("Error posting fund", jsonResponse.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                App.get().stopLoadingDialog();
                            }
                        }
                        if (response.isSuccessful()) {
                            ((Activity)mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    App.get().stopLoadingDialog();
                                    Toast.makeText(mContext, getResources().getString(R.string.fund_was_updated), Toast.LENGTH_LONG).show();
                                    Intent i = MainActivity.newIntent(mContext);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    ((Activity) mContext).finish();
                                }
                            });
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