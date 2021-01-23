package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AbsListView;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.DetailAllergieAdapter;
import com.fantechlabs.lailaa.databinding.ActivityAlergieResourceDetailsBinding;
import com.fantechlabs.lailaa.models.allergie_models.AllergieListerner;
import com.fantechlabs.lailaa.models.allergie_models.XmlParcer;
import com.fantechlabs.lailaa.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

//************************************************************************
public class AlergieResourceDetails extends BaseActivity
//************************************************************************
{
    private ActivityAlergieResourceDetailsBinding mBinding;
    private DetailAllergieAdapter mDetailAllergieAdapter;
    private List<String> mSummaryList = new ArrayList<>();
    private List<String> mUrlList = new ArrayList<>();
    private Boolean isScroll = false;
    int currentItems, totalItems, scrollOutItems;
    private LinearLayoutManager mLinearLayoutManager;
    private String mDiseaseName = "";

    //*****************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*****************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_alergie_resource_details);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControls();
    }

    //*****************************************************
    private void initControls()
    //*****************************************************
    {
        mLinearLayoutManager = new LinearLayoutManager(this);
        getParcelable();
        setData();
    }

    //*****************************************************
    private void setData()
    //*****************************************************
    {
        if (!Laila.instance().IS_Documents) {
            mBinding.noRecord.setVisibility(View.VISIBLE);
            mBinding.mainView.setVisibility(View.GONE);
            return;
        }
        mBinding.noRecord.setVisibility(View.GONE);
        mBinding.mainView.setVisibility(View.VISIBLE);

        val allergyName = Laila.instance().getMDocument().getTerm();
        mBinding.toolbarText.setText(allergyName);

        getFormatDocumentList();

        mDetailAllergieAdapter = new DetailAllergieAdapter(mSummaryList, new DetailAllergieAdapter.ListClickListener() {
            @Override
            public void onClick(int position) {
                val url = mUrlList.get(position);
                moveToBrowserActivity(allergyName, url);
            }
        });
        mBinding.summaryRecyclerView.setLayoutManager(mLinearLayoutManager);
        mBinding.summaryRecyclerView.setAdapter(mDetailAllergieAdapter);

        mBinding.summaryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScroll = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mLinearLayoutManager.getChildCount();
                scrollOutItems = mLinearLayoutManager.findFirstVisibleItemPosition();
                totalItems = mLinearLayoutManager.getItemCount();

                if (isScroll && currentItems + scrollOutItems == totalItems) {
                    isScroll = false;
                    fetchData();
                }
            }
        });
    }

    private void getFormatDocumentList() {

        if (mSummaryList == null)
            mSummaryList = new ArrayList<>();
        if (mUrlList == null)
            mUrlList = new ArrayList<>();
        for (val docs : Laila.instance().getMDocumentListWithHashMap()) {
            val summaryHtml = docs.get("FullSummary");
            val summary = Html.fromHtml((String) summaryHtml).toString();
            mSummaryList.add(summary);
            mUrlList.add(docs.get("url"));
        }
    }

    //******************************************************
    private void fetchData()
    //******************************************************
    {
        getParcelable();
        val start = totalItems + 1;
        mBinding.progress.setVisibility(View.VISIBLE);
        new XmlParcer(new AllergieListerner() {
            //***********************************************************
            @Override
            public void onExecutionCompleted()
            //***********************************************************
            {

                runOnUiThread(() -> {
                    if (mSummaryList == null)
                        return;
                    mSummaryList.clear();
                    getFormatDocumentList();
                    mBinding.progress.setVisibility(View.GONE);
                    mBinding.summaryRecyclerView.setLayoutManager(mLinearLayoutManager);
                    mBinding.summaryRecyclerView.setAdapter(mDetailAllergieAdapter);
                    mDetailAllergieAdapter.notifyDataSetChanged();
                });
            }

            //&retstart=11&retmax=10
            //***********************************************************
            @Override
            public void onExecutionFailed()
            //***********************************************************
            {
//                mBinding.progress.setVisibility(View.GONE);
            }
        }).execute(Constants.BASE_URL_Terms + mDiseaseName + "&retstart=" + start + "&retmax=10");
    }

    //********************************************
    private void moveToBrowserActivity(String title, String url)
    //********************************************
    {
        Intent browserIntent = new Intent(AlergieResourceDetails.this, BrowserActivity.class);
        browserIntent.putExtra(BrowserActivity.SCREEN_URL, url);
        browserIntent.putExtra(BrowserActivity.INFO_TITLE, title);
        startActivity(browserIntent);
    }

    //*********************************************************************
    private void getParcelable()
    //*********************************************************************
    {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mDiseaseName = args.getString(Constants.DISEASE_NAME);
        }
    }

    //*****************************************************
    @Override
    protected boolean showStatusBar()
    //*****************************************************
    {
        return false;
    }
}