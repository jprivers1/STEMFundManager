package com.fightitwithfitness.stemfundmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fightitwithfitness.stemfundmanager.R;
import com.fightitwithfitness.stemfundmanager.activities.FundDetailActivity;
import com.fightitwithfitness.stemfundmanager.models.Fund;
import java.util.List;
import static android.support.v7.widget.RecyclerView.*;

/**
 * Created by jpriv on 9/27/2017.
 */

public class FundListAdapter extends RecyclerView.Adapter<FundListAdapter.FundViewHolder> {

    private Context mContext;
    private List<Fund> mFundList;

    public FundListAdapter(Context context, List<Fund> fundList){
        this.mContext = context;
        this.mFundList = fundList;
    }

    @Override
    public FundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new FundViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FundViewHolder holder, int position) {
        Fund fund = mFundList.get(position);
        holder.bindFund(fund);
    }

    @Override
    public int getItemCount() {
        return mFundList.size();
    }

    //Filters list
    public void filterList(List<Fund> temp) {
        mFundList = temp;
        notifyDataSetChanged();
    }

    public class FundViewHolder extends ViewHolder implements View.OnClickListener{
        private Fund mFund;
        private TextView mFundIDTextView;
        private TextView mFundInvestmentName;
        private TextView mFundAgency;

        public FundViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mFundIDTextView = (TextView)itemView.findViewById(R.id.et_fund_id);
            mFundInvestmentName = (TextView)itemView.findViewById(R.id.et_fund_investment_name);
            mFundAgency = (TextView)itemView.findViewById(R.id.et_fund_agency);
        }

        public void bindFund(Fund fund) {
            mFund = fund;
            mFundIDTextView.setText("ID: " + String.valueOf(mFund.getId()));
            mFundInvestmentName.setText(mFund.getInvestmentName());
            mFundAgency.setText(mFund.getAgency());
        }

        @Override
        public void onClick(View v) {
            Intent i = FundDetailActivity.newIntent(mContext, mFund.getId());
            mContext.startActivity(i);
        }
    }
}
