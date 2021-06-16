package com.aditum.activities;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.aditum.R;
import com.aditum.databinding.ActivityDocumentDetailsBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

//***************************************************************
public class DocumentDetailsActivity extends BaseActivity
//***************************************************************
{
    private ActivityDocumentDetailsBinding mBinding;
    public static final String NAME = "NAME";
    public static final String IMAGE_URL = "IMAGE_URL";
    private String mImageUrl;
    private RequestManager mGlideRequestManager;

    //***************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //***************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_document_details);
        getParcelable();
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControl();
    }

    //*********************************************************
    private void initControl()
    //*********************************************************
    {
        mGlideRequestManager = Glide.with(this);
        loadImage(mImageUrl, mBinding.image);
    }

    //*******************************************************************
    public void loadImage(String url, ImageView imageView)
    //*******************************************************************
    {
        if (TextUtils.isEmpty(url))
            return;

        mGlideRequestManager
                .load(url)
                .fallback(R.mipmap.logo)
                .placeholder(R.color.background)
                .centerInside()
                .thumbnail(0.1f)
                .into(imageView);
    }

    //*********************************************************
    private void getParcelable()
    //*********************************************************
    {
        if (getIntent().getExtras()
                .containsKey(IMAGE_URL)) {
            mImageUrl = getIntent().getStringExtra(IMAGE_URL);
        }
    }

    //***************************************************************
    @Override
    protected boolean showStatusBar()
    //***************************************************************
    {
        return false;
    }
}