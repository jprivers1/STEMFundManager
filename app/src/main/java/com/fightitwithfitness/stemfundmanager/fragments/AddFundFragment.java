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
import android.widget.Toast;
import com.fightitwithfitness.stemfundmanager.App;
import com.fightitwithfitness.stemfundmanager.R;
import com.fightitwithfitness.stemfundmanager.activities.MainActivity;
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
 * Created by jpriv on 9/27/2017.
 */

public class AddFundFragment extends Fragment implements View.OnClickListener{

    private EditText mId;
    private EditText mInvestmentName;
    private EditText mAgency;
    private EditText mSubAgency;
    private EditText mBriefDescription;
    private EditText mYearEstablished;
    private EditText mFundingFY2008;
    private EditText mFundingFY2009;
    private EditText mFundingFY2010;
    private EditText mMissionSpecificOrGeneralStem;
    private EditText mAgencyOrMissionRelatedNeeds;
    private EditText mPrimaryInvestmentObjective;
    private Button mAddButton;
    private Context mContext;

    public static Fragment newInstance() {
        return new AddFundFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_fund, container, false);

        mId = (EditText) v.findViewById(R.id.et_fund_id);
        mInvestmentName = (EditText) v.findViewById(R.id.et_fund_investment_name);
        mAgency = (EditText) v.findViewById(R.id.et_fund_agency);
        mSubAgency = (EditText) v.findViewById(R.id.et_fund_sub_agency);
        mBriefDescription = (EditText) v.findViewById(R.id.et_fund_brief_description);
        mYearEstablished = (EditText) v.findViewById(R.id.et_fund_year_established);
        mFundingFY2008 = (EditText) v.findViewById(R.id.et_fund_fy_2008);
        mFundingFY2009 = (EditText) v.findViewById(R.id.et_fund_fy_2009);
        mFundingFY2010 = (EditText) v.findViewById(R.id.et_fund_fy_2010);
        mMissionSpecificOrGeneralStem = (EditText) v.findViewById(R.id.et_fund_mission_specific_or_general_stem);
        mAgencyOrMissionRelatedNeeds = (EditText) v.findViewById(R.id.et_fund_agency_or_mission_related_needs);
        mPrimaryInvestmentObjective = (EditText) v.findViewById(R.id.et_fund_primary_investment_objective);
        mAddButton = (Button) v.findViewById(R.id.bt_add_fund);
        mAddButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add_fund:
                if(App.get().isNetworkAvailableAndConnected(mContext)){
                    if(checkFields()){
                        addFundToWebservice(createFundJsonObject());
                    }
                }else{
                    Toast.makeText(mContext, "Please check network connectivity", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private boolean checkFields() {
        if(TextUtils.isEmpty(mId.getText())){
            Toast.makeText(mContext, "Please enter a valid Id", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mInvestmentName.getText())){
            Toast.makeText(mContext, "Please enter a valid investment name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mAgency.getText())){
            Toast.makeText(mContext, "Please enter a valid agency", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mSubAgency.getText())){
            Toast.makeText(mContext, "Please enter a valid SubAgency", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mBriefDescription.getText())){
            Toast.makeText(mContext, "Please enter a valid description", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mYearEstablished.getText())){
            Toast.makeText(mContext, "Please enter a valid year of establishment", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mFundingFY2008.getText())){
            Toast.makeText(mContext, "Please enter a valid funding for 2008", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mFundingFY2009.getText())){
            Toast.makeText(mContext, "Please enter a valid funding for 2009", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mFundingFY2010.getText())){
            Toast.makeText(mContext, "Please enter a valid funding for 2010", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mMissionSpecificOrGeneralStem.getText())){
            Toast.makeText(mContext, "Please enter mission or general stem", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mAgencyOrMissionRelatedNeeds.getText())){
            Toast.makeText(mContext, "Please enter mission related needs", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(mPrimaryInvestmentObjective.getText())){
            Toast.makeText(mContext, "Please enter a valid primary investment objective", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //Build Json Object to post new fund to webservice
    private JSONObject createFundJsonObject() {
        JSONObject fund = new JSONObject();

        try {
            fund.put("Id", Integer.parseInt(mId.getText().toString()));
            fund.put("InvestmentName", mInvestmentName.getText().toString());
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

    //Webservice call to add fund
    private void addFundToWebservice(JSONObject fund){
        try {
            App.get().initLoadingDialog(mContext);

            String fundJsonString = fund.toString();
            RequestBody body = RequestBody.create(WSApi.JSON, fundJsonString);

            Request request = new Request.Builder().url(WSApi.BASE_URL).addHeader("Accept", WSApi.CONTENT_TYPE_VALUE)
                        .addHeader(WSApi.CONTENT_TYPE, WSApi.CONTENT_TYPE_VALUE).post(body).build();

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
                                    Toast.makeText(mContext, getResources().getString(R.string.fund_added), Toast.LENGTH_LONG).show();
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