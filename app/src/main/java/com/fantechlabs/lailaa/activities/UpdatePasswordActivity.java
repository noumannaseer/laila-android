package com.fantechlabs.lailaa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.ActivityUpdatePasswordBinding;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.Constants;
import com.fantechlabs.lailaa.utils.SharedPreferencesUtils;
import com.fantechlabs.lailaa.view_models.UpdatePasswordViewModel;

import lombok.val;

//*******************************************************************
public class UpdatePasswordActivity extends BaseActivity
        implements UpdatePasswordViewModel.UpdatePasswordViewModelListener
//*******************************************************************
{

    private ActivityUpdatePasswordBinding mBinding;
    private UpdatePasswordViewModel mUpdatePasswordViewModel;

    //*******************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //*******************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_password);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControls();
    }

    //*******************************************************************
    @Override
    protected boolean showStatusBar()
    //*******************************************************************
    {
        return false;
    }

    //*****************************************************************
    private void initControls()
    //*****************************************************************
    {
        mUpdatePasswordViewModel = new UpdatePasswordViewModel(this);
        mBinding.changePassword.setOnClickListener(view ->
        {
            val currentPassword = mBinding.currentPassword.getText().toString();
            val newPassword = mBinding.newPassword.getText().toString();
            val confirmPassword = mBinding.confirmPassword.getText().toString();


            if (TextUtils.isEmpty(currentPassword)) {
                AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.current_password_required), AndroidUtil.getString(R.string.alert), this);
                return;
            }
            if (TextUtils.isEmpty(newPassword)) {
                AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.new_password_required), AndroidUtil.getString(R.string.alert), this);
                return;
            }
            if (newPassword.length() < Constants.PASSWORD_LENGTH) {
                AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.new_password_must_be__character), AndroidUtil.getString(R.string.alert), this);
                return;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.confirm_password_required), AndroidUtil.getString(R.string.alert), this);
                return;
            }
            if (confirmPassword.length() < Constants.PASSWORD_LENGTH) {
                AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.confirm_password_must_be__character), AndroidUtil.getString(R.string.alert), this);
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.new_and_confirm_password_not_match), AndroidUtil.getString(R.string.alert), this);
                return;
            }

            showLoadingDialog();
            mUpdatePasswordViewModel.updatePassword(currentPassword, newPassword);

        });
    }

    //*******************************************************************
    @Override
    public void onSuccess(String message)
    //*******************************************************************
    {
        hideLoadingDialog();
        logout(message);
    }

    //*******************************************************************
    @Override
    public void onFailed(String message)
    //*******************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(message, AndroidUtil.getString(R.string.error), this);
    }


    //******************************************************************
    private void logout(@NonNull String msg)
    //******************************************************************
    {
        AndroidUtil.displayAlertDialog(
                msg,
                AndroidUtil.getString(R.string.update),
                this,
                AndroidUtil.getString(
                        R.string.ok),
                (dialog, which) -> {
                    if (which == -1) {
                        SharedPreferencesUtils.setValue(Constants.USER_DATA, (Object) null);
                        Intent logoutIntent = new Intent(this, HomeActivity.class);
                        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(logoutIntent);
                        finish();
                    }
                });

    }

}