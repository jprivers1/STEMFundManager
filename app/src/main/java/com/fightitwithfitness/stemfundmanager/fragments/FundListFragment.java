package com.fightitwithfitness.stemfundmanager.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.fightitwithfitness.stemfundmanager.App;
import com.fightitwithfitness.stemfundmanager.R;
import com.fightitwithfitness.stemfundmanager.adapters.FundListAdapter;
import com.fightitwithfitness.stemfundmanager.models.Fund;
import com.fightitwithfitness.stemfundmanager.services.FundUpdatesService;
import com.fightitwithfitness.stemfundmanager.utils.WSApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jpriv on 9/27/2017.
 */

public class FundListFragment extends Fragment {

    private RecyclerView mFundListRecyclerView;
    private FundListAdapter mFundListAdapter;
    private List<Fund> mFundList;
    private Context mContext;
    private EditText mFilterEditText;
    private TextWatcher mFilterWatcher;

    public Callbacks mCallbacks;

    public interface Callbacks{
        void onItemSelected();
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

    public static FundListFragment newInstance(){
        return new FundListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mContext = getActivity();
        mFundList = new ArrayList<>();

        FundUpdatesService.setServiceAlarm(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fund_list, container, false);

        mFundListRecyclerView = (RecyclerView)v.findViewById(R.id.rv_stem_fund_list);
        mFilterEditText = (EditText) v.findViewById(R.id.et_search_filter);
        mFilterWatcher = getFilterWatcher();
        mFilterEditText.addTextChangedListener(mFilterWatcher);


        if (App.get().isNetworkAvailableAndConnected(mContext)) {
            getFundsFromWebservice();
        } else {
            Toast.makeText(mContext, "Please check network availability", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private TextWatcher getFilterWatcher(){
        TextWatcher filterWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterList(s.toString());
            }
        };
            return filterWatcher;
    }

    private void filterList(String text){
        List<Fund> filteredList = new ArrayList();
        for(Fund f: mFundList){
            if(f.getInvestmentName().toLowerCase().contains(text)){
                filteredList.add(f);
            }
        }
        mFundListAdapter.filterList(filteredList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_fund_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_add_fund:
                mCallbacks.onItemSelected();
               return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initRecyclerViewAdapter(List<Fund> fundList){
        mFundListAdapter = new FundListAdapter(getActivity(), mFundList);
        mFundListRecyclerView.setAdapter(mFundListAdapter);
        mFundListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //Webservice call to retrieve list of funds
    private void getFundsFromWebservice() {
        try {
            App.get().initLoadingDialog(mContext);

            Request request = new Request.Builder().url(WSApi.BASE_URL).addHeader("Accept", WSApi.CONTENT_TYPE_VALUE).build();

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
                                JSONArray jsonResponse = new JSONArray(response.body().string());
                                Log.e("Error getting fund list", jsonResponse.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                                App.get().stopLoadingDialog();
                            }
                        }
                        if (response.isSuccessful()) {
                            mFundList.clear();
                            try {
                                JSONArray JsonFundArray = new JSONArray(response.body().string());
                                Log.v("FUNDS", JsonFundArray.toString());

                                for(int i = 0; i < JsonFundArray.length(); i++){
                                    Fund fund = new Fund();
                                    JSONObject JsonFund = JsonFundArray.getJSONObject(i);

                                    if(!JsonFund.isNull("Id")){
                                        fund.setId(JsonFund.getInt("Id"));
                                    }

                                    if(!JsonFund.isNull("InvestmentName")){
                                        fund.setInvestmentName(JsonFund.getString("InvestmentName"));
                                    }

                                    if(!JsonFund.isNull("Agency")){
                                        fund.setAgency(JsonFund.getString("Agency"));
                                    }

                                    mFundList.add(fund);
                                }

                                ((Activity)mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        initRecyclerViewAdapter(mFundList);
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