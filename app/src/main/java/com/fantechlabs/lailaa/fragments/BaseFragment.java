package com.fantechlabs.lailaa.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.utils.AndroidUtil;

import lombok.NonNull;


//************************************************************
public abstract class BaseFragment
        extends Fragment
//************************************************************
{

    private Activity mBaseActivity;
    private Fragment mFragment;
    private Dialog mLoadingBar;
    protected View mRootView;

    //************************************************************
    public void onCreate(Bundle savedInstanceState)
    //************************************************************
    {
        super.onCreate(savedInstanceState);
    }

    //************************************************************************************************
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState)
    //************************************************************************************************
    {
        View view = onCreateViewBaseFragment(inflater, parent, savedInstanseState);
        return view;
    }

    //*********************************************************************
    @NonNull
    public abstract View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);
    //*********************************************************************

    //****************************************************************
    public void setFragment(Fragment fragment)
    //****************************************************************
    {
        if (mFragment != null)
            return;
        mFragment = fragment;
        mBaseActivity = mFragment.getActivity();
    }

    //****************************************************************
    protected void setText(TextView textView, String value)
    //****************************************************************
    {
        if (TextUtils.isEmpty(value))
            textView.setText(AndroidUtil.getString(R.string.n_a));
        else
            textView.setText(value);
    }

    //****************************************************************
    protected void setTextWithoutNA(TextView textView, String value)
    //****************************************************************
    {
        if (!TextUtils.isEmpty(value))
            textView.setText(value);
    }

    //***********************************************************************
    public void showLoadingDialog()
    //***********************************************************************
    {
        if (mLoadingBar == null)
        {
            mLoadingBar = new Dialog(getContext(), R.style.CustomTransparentDialog);
            mLoadingBar.setContentView(R.layout.progress_dialog);
            mLoadingBar.setCancelable(false);
            mLoadingBar.setCanceledOnTouchOutside(false);
        }
        mLoadingBar.show();
    }

    //***********************************************************************
    public void hideLoadingDialog()
    //***********************************************************************
    {
        if (mLoadingBar != null)
        {
            mLoadingBar.dismiss();
        }
    }

}
