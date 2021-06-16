package com.aditum.utils;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

//***************************************************************************
@SuppressLint("AppCompatCustomView")
public class AutoCompleteLoadingBar extends AutoCompleteTextView
//***************************************************************************
{
        private ProgressBar mLoadingIndicator;

        //***************************************************************************
        public AutoCompleteLoadingBar(Context context)
        //***************************************************************************
        {
            super(context);
        }

        //***************************************************************************
        public AutoCompleteLoadingBar(Context context, AttributeSet attrs)
        //***************************************************************************
        {
            super(context, attrs);
        }

        //***************************************************************************
        public AutoCompleteLoadingBar(Context context, AttributeSet attrs, int defStyleAttr)
        //***************************************************************************
        {
            super(context, attrs, defStyleAttr);
        }

        //***************************************************************************
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public AutoCompleteLoadingBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
        //***************************************************************************
        {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        //***************************************************************************
        @TargetApi(Build.VERSION_CODES.N)
        public AutoCompleteLoadingBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, Resources.Theme popupTheme)
        //***************************************************************************
        {
            super(context, attrs, defStyleAttr, defStyleRes, popupTheme);
        }

        //***************************************************************************
        public void setLoadingIndicator(ProgressBar progressBar)
        //***************************************************************************
        {
            mLoadingIndicator = progressBar;
        }

        //***************************************************************************
        public void dispayIndicator()
        //***************************************************************************
        {
            if(mLoadingIndicator != null)
            {
                mLoadingIndicator.setVisibility(View.VISIBLE);
            }
            this.setText("");
        }

        //***************************************************************************
        public void removeIndicator()
        //***************************************************************************
        {
            if(mLoadingIndicator != null)
            {
                mLoadingIndicator.setVisibility(View.GONE);
            }
        }
    }
