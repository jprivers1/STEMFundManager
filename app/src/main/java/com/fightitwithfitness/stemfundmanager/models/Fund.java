package com.fightitwithfitness.stemfundmanager.models;

import java.io.Serializable;

/**
 * Created by jpriv on 9/27/2017.
 */

public class Fund implements Serializable {
    //List
    private int mId;
    private String mInvestmentName;
    private String mAgency;

    //Detail(includes above)
    private String mSubAgency;
    private String mBriefDescription;
    private int mYearEstablished;
    private double mFundingFY2008;
    private double mFundingFY2009;
    private double mFundingFY2010;
    private String mMissionSpecificOrGeneralStem;
    private String mAgencyOrMissionRelatedNeeds;
    private String mPrimaryInvestmentObjective;


    public Fund(){};

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getInvestmentName() {
        return mInvestmentName;
    }

    public void setInvestmentName(String investmentName) {
        mInvestmentName = investmentName;
    }

    public String getAgency() {
        return mAgency;
    }

    public void setAgency(String agency) {
        mAgency = agency;
    }

    public String getSubAgency() {
        return mSubAgency;
    }

    public void setSubAgency(String subAgency) {
        mSubAgency = subAgency;
    }

    public String getBriefDescription() {
        return mBriefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        mBriefDescription = briefDescription;
    }

    public int getYearEstablished() {
        return mYearEstablished;
    }

    public void setYearEstablished(int yearEstablished) {
        mYearEstablished = yearEstablished;
    }

    public double getFundingFY2008() {
        return mFundingFY2008;
    }

    public void setFundingFY2008(double fundingFY2008) {
        mFundingFY2008 = fundingFY2008;
    }

    public double getFundingFY2009() {
        return mFundingFY2009;
    }

    public void setFundingFY2009(double fundingFY2009) {
        mFundingFY2009 = fundingFY2009;
    }

    public double getFundingFY2010() {
        return mFundingFY2010;
    }

    public void setFundingFY2010(double fundingFY2010) {
        mFundingFY2010 = fundingFY2010;
    }

    public String getMissionSpecificOrGeneralStem() {
        return mMissionSpecificOrGeneralStem;
    }

    public void setMissionSpecificOrGeneralStem(String missionSpecificOrGeneralStem) {
        mMissionSpecificOrGeneralStem = missionSpecificOrGeneralStem;
    }

    public String getAgencyOrMissionRelatedNeeds() {
        return mAgencyOrMissionRelatedNeeds;
    }

    public void setAgencyOrMissionRelatedNeeds(String agencyOrMissionRelatedNeeds) {
        mAgencyOrMissionRelatedNeeds = agencyOrMissionRelatedNeeds;
    }

    public String getPrimaryInvestmentObjective() {
        return mPrimaryInvestmentObjective;
    }

    public void setPrimaryInvestmentObjective(String primaryInvestmentObjective) {
        mPrimaryInvestmentObjective = primaryInvestmentObjective;
    }
}
