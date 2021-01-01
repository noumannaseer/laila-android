package com.fantechlabs.lailaa.activities;

import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivitySettingBinding;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.ConnectionChangeCallback;
import com.fantechlabs.lailaa.utils.NetworkChangeReceiver;

//*********************************************************************
public class BrowserActivity
        extends BaseActivity
        implements ConnectionChangeCallback
//*********************************************************************
{
    private WebView mWebView;
    private View mProgressView;
    public static final String INFO_TITLE = "INFO_TITLE";
    public static final String SCREEN_URL = "SCREEN_URL";
    private String mTitle;
    private String mUrl;
    private NetworkChangeReceiver receiver;
    private ImageView mNoInternetImage;
    private IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private TextView mToolbarTextView;

    //*********************************************************************
    private void getData()
    //*********************************************************************
    {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            mTitle = args.getString(INFO_TITLE);
            mUrl = args.getString(SCREEN_URL);
        }
    }

    //******************************************************************
    private void setTab()
    //******************************************************************
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        if (!TextUtils.isEmpty(mTitle))
            mToolbarTextView.setText(mTitle);

    }

    //**********************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //**********************************************************************
    {
        setContentView(R.layout.browser_activity);
        mToolbarTextView = findViewById(R.id.toolbar_text);
        mWebView = findViewById(R.id.web_view);
        mProgressView = findViewById(R.id.progress_view);
        mNoInternetImage = findViewById(R.id.no_internet);
        mProgressView.setVisibility(View.VISIBLE);
        getData();
        setTab();
        switchVisibility(false);
        mWebView.setWebViewClient(new WebViewClient() {

            //**********************************************************************
            @Override
            public void onPageFinished(WebView view, String url)
            //**********************************************************************
            {
                super.onPageFinished(view, url);
                switchVisibility(true);
            }


            //**********************************************************************
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            //**********************************************************************
            {
                super.onPageStarted(view, url, favicon);
                switchVisibility(false);
            }
        });

        mWebView.getSettings()
                .setDomStorageEnabled(true);
        mWebView.getSettings()
                .setDatabaseEnabled(true);
        mWebView.getSettings()
                .setMinimumFontSize(1);
        mWebView.getSettings()
                .setMinimumLogicalFontSize(1);
        mWebView.getSettings()
                .setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
    }

    //************************************************************
    @Override
    protected boolean showStatusBar()
    //************************************************************
    {
        return false;
    }

    //*********************************************************************
    @Override
    protected void onPostResume()
    //*********************************************************************
    {
        super.onPostResume();
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, intentFilter);
        receiver.setConnectionChangeCallback(this);
    }


    /*//*********************************************************************
    @Override
    protected void onDestroy()
    //*********************************************************************
    {
        if (receiver != null)
            unregisterReceiver(receiver);
        super.onDestroy();
    }
    */

    //**********************************************************************
    @Override
    protected void onStop()
    //**********************************************************************
    {
        super.onStop();
        if (receiver != null)
            unregisterReceiver(receiver);

    }

    //**********************************************************************
    private void switchVisibility(boolean pageLoaded)
    //**********************************************************************
    {
        if (AndroidUtil.isNetworkStatusAvailable()) {
            if (pageLoaded) {
                mWebView.setVisibility(View.VISIBLE);
                mProgressView.setVisibility(View.GONE);
                mNoInternetImage.setVisibility(View.GONE);
            } else {
                mWebView.setVisibility(View.GONE);
                mProgressView.setVisibility(View.VISIBLE);
                mNoInternetImage.setVisibility(View.GONE);
            }

        } else {
            mWebView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.GONE);
            mNoInternetImage.setVisibility(View.VISIBLE);
        }
    }

    //**********************************************************************
    @Override
    public void onBackPressed()
    //**********************************************************************
    {
        hideLoadingDialog();
        if (mWebView.canGoBack())
            mWebView.goBack();
        else
            super.onBackPressed();
    }

    //**********************************************************************
    @Override
    public void onConnectionChanged(boolean isConnected)
    //**********************************************************************
    {
        if (!isConnected) {
            mWebView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.GONE);
            mNoInternetImage.setVisibility(View.VISIBLE);
        } else {
            mWebView.setVisibility(View.GONE);
            mWebView.reload();
            mProgressView.setVisibility(View.VISIBLE);
            mNoInternetImage.setVisibility(View.GONE);
        }
    }

}
