package com.aditum.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.databinding.ActivitySignUpBinding;
import com.aditum.models.updates.response_models.SignUpResponse;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;
import com.aditum.utils.UIUtils;
import com.aditum.view_models.SignUpViewModel;

import lombok.val;

//****************************************************
public class SignUpActivity extends BaseActivity
        implements SignUpViewModel.UserSignUpListener
//****************************************************
{
    private ActivitySignUpBinding mBinding;
    private SignUpViewModel mSignUpViewModel;

    //****************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //****************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControls();
    }

    //***********************************************************
    private void initControls()
    //***********************************************************
    {
        mBinding.signinTv.setOnClickListener(view -> startActivity(new Intent(SignUpActivity.this, SignInActivity.class)));
        mSignUpViewModel = new SignUpViewModel(this);
        mBinding.signupButton.setOnClickListener(v -> signUpUser());
    }

    //********************************************
    private void signUpUser()
    //********************************************
    {
        val email = mBinding.email.getText()
                .toString();
        val password = mBinding.password.getText()
                .toString();
        val confirmPassword = mBinding.confirmPassword.getText()
                .toString();

        if (TextUtils.isEmpty(email)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.email_is_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (!UIUtils.isValidEmailId(email)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.invalid_email), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.password_required), AndroidUtil.getString(R.string.alert), this);
            return;
        }
        if (password.length() < Constants.PASSWORD_LENGTH) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.password_must_be__character), AndroidUtil.getString(R.string.alert), this);
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
        if (!password.equals(confirmPassword)) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.password_not_match), AndroidUtil.getString(R.string.alert), this);
            return;
        }

        val userRequest = Laila.instance()
                .getUserRequest().Builder();
        userRequest.setEmail(email);
        userRequest.setPassword(password);
        showLoadingDialog();
        mSignUpViewModel.signUpUser(userRequest);
    }

    //****************************************************
    @Override
    protected boolean showStatusBar()
    //****************************************************
    {
        return false;
    }

    //****************************************************
    @Override
    public void onSignUpSuccessfully(@Nullable SignUpResponse userResponse)
    //****************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.user_signup),
                AndroidUtil.getString(R.string.signup),
                this,
                AndroidUtil.getString(R.string.ok),
                (dialog, which) ->
                {
                    if (which == -1) {
                        mBinding.email.setText("");
                        mBinding.password.setText("");
                        mBinding.confirmPassword.setText("");
                        startActivity(new Intent(this, SignInActivity.class));
                    }
                });
    }

    //****************************************************
    @Override
    public void onSignUpFailed(@NonNull String errorMessage)
    //****************************************************
    {
        hideLoadingDialog();
        val error = Html.fromHtml((String) errorMessage).toString();
        AndroidUtil.displayAlertDialog(error, AndroidUtil.getString(R.string.error), this);
    }
}