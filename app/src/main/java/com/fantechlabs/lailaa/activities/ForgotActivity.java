package com.fantechlabs.lailaa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityForgotBinding;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.view_models.ResetPasswordViewModel;

//*****************************************************************
public class ForgotActivity extends BaseActivity
        implements ResetPasswordViewModel.ResetPasswordViewModelListener
//*****************************************************************
{

    private ActivityForgotBinding mBinding;
    private ResetPasswordViewModel mResetPasswordViewModel;


    //*****************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*****************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        mResetPasswordViewModel = new ResetPasswordViewModel(this);
        initControls();
    }

    //*****************************************************************
    @Override
    protected boolean showStatusBar()
    //*****************************************************************
    {
        return false;
    }

    //*****************************************************************
    private void initControls()
    //*****************************************************************
    {
        mBinding.signinButton.setOnClickListener(view ->
        {
            String email = mBinding.emailEdit.getText().toString();
            if (TextUtils.isEmpty(email)) {
                mBinding.emailEdit.setError(AndroidUtil.getString(R.string.required));
                mBinding.emailEdit.requestFocus();
                return;
            }
            showLoadingDialog();
            mResetPasswordViewModel.resetPassword(email);

        });
    }

    //***********************************************
    @Override
    public void onSuccess(String response)
    //***********************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(
                response,
                AndroidUtil.getString(R.string.forgot_password),
                this,
                AndroidUtil.getString(
                        R.string.ok),
                (dialog, which) -> {
                    if (which == -1) {
                        Intent forgotIntent = new Intent(this, SignInActivity.class);
                        forgotIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(forgotIntent);
                        finish();
                    }
                });
    }

    //***********************************************
    @Override
    public void onFailed(String message)
    //***********************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(message, AndroidUtil.getString(R.string.forgot_password), this);
    }
}
