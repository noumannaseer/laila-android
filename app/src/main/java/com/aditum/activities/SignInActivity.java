package com.aditum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.aditum.R;
import com.aditum.databinding.ActivitySigninBinding;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.aditum.Laila;
import com.aditum.models.updates.response_models.ProfileResponse;
import com.aditum.models.updates.response_models.UserResponse;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;
import com.aditum.utils.SharedPreferencesUtils;
import com.aditum.utils.UIUtils;
import com.aditum.view_models.LoginViewModel;
import com.aditum.view_models.SocialLoginViewModel;
import com.aditum.view_models.UpdateProfileViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.Arrays;

import lombok.val;

import static com.aditum.utils.AndroidUtil.getContext;

//***********************************************************
public class SignInActivity extends BaseActivity
        implements LoginViewModel.UserLoginListener,
        SocialLoginViewModel.SocialLoginViewModelListener,
        UpdateProfileViewModel.UpdateProfileListener
//***********************************************************
{
    private ActivitySigninBinding mBinding;
    private LoginViewModel mLoginViewModel;
    private static final int RC_SIGN_IN = 9001;
    private static final int FB_SIGN_IN = 64206;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "Authentication";
    private CallbackManager mCallbackManager;
    private AccessTokenTracker accessTokenTracker;
    private SocialLoginViewModel mSocialLoginViewModel;
    private UpdateProfileViewModel mUpdateProfileViewModel;

    //***********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //***********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_signin);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setTitle("");
        initControls();
    }

    //***********************************************************
    @Override
    protected boolean showStatusBar()
    //***********************************************************
    {
        return false;
    }

    //***********************************************************
    private void initControls()
    //***********************************************************
    {
        mUpdateProfileViewModel = new UpdateProfileViewModel(this);
        mLoginViewModel = new LoginViewModel(this);
        mSocialLoginViewModel = new SocialLoginViewModel(this);
        mBinding.signinButton.setOnClickListener(v -> loginUser());
        mBinding.signupTv.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        mBinding.facebook.setOnClickListener(view -> gotoFaceBookActivity());
        googleLogin();
        mBinding.forgotPassword.setOnClickListener(view -> gotoForgotActivity());
    }

    //********************************************
    @Override
    public void onResume()
    //********************************************
    {
        super.onResume();

        val email = SharedPreferencesUtils.getString(Constants.EMAIL);
        val password = SharedPreferencesUtils.getString(Constants.PASSWORD);

        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password))
            return;
        mBinding.email.setText(email);
        mBinding.password.setText(password);
    }

    //********************************************
    private void loginUser()
    //********************************************
    {
        val email = mBinding.email.getText()
                .toString();
        val password = mBinding.password.getText()
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
//        if (password.length() < Constants.PASSWORD_LENGTH) {
//            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.password_must_be__character), AndroidUtil.getString(R.string.alert), this);
//            return;
//        }

        val userRequest = Laila.instance()
                .getUserRequest().Builder();

        userRequest.setEmail(email);
        userRequest.setPassword(password);
        showLoadingDialog();
        mLoginViewModel.loginUser(userRequest);
    }

    //*******************************************************
    private void gotoForgotActivity()
    //*******************************************************
    {
        Intent forgotIntent = new Intent(getApplicationContext(), ForgotActivity.class);
        startActivity(forgotIntent);
    }

    //*******************************************************
    private void gotoDashboardActivity()
    //*******************************************************
    {
        Intent mainIntent = new Intent(getContext(), HomeActivity.class);
        startActivity(mainIntent);
        this.finish();
    }

    //*******************************************************
    private void gotoFaceBookActivity()
    //*******************************************************
    {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            if (!accessToken.isExpired()) {
                Log.d(TAG, "Token ==> " + accessToken.getToken());
                getFbData(accessToken);
                return;
            }
        }

        facebookLogin();
    }

    //****************************************************************
    private void googleLogin()
    //****************************************************************
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_console_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mBinding.google.setOnClickListener(v -> signIn());
    }

    //****************************************************************
    private void signIn()
    //****************************************************************
    {
        showLoadingDialog();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //****************************************************************
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    //****************************************************************
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_SIGN_IN:
                handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data));
                break;
            case FB_SIGN_IN:
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    //*******************************************************************
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    //*******************************************************************
    {
        try {
            updateUI(completedTask.getResult(ApiException.class));
        } catch (ApiException e) {
            error(this.getClass()
                    .getName(), "signInResult:failed code=" + e.getStatusCode());
            hideLoadingDialog();
            updateUI(null);
        }
    }

    //*******************************************************************
    private void updateUI(GoogleSignInAccount account)
    //*******************************************************************
    {
        if (account != null) {
            val id = account.getId();
            val email = account.getEmail();

            mSocialLoginViewModel.socialLogin(email, id, "google");
        }
    }

    //*******************************************************************
    private void error(String name, String msg)
    //*******************************************************************
    {
        Log.w(name, msg);
        AndroidUtil.displayAlertDialog(msg, AndroidUtil.getString(R.string.error), this);
    }


    //*********************************************
    private void facebookLogin()
    //*********************************************
    {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance()
                .logInWithReadPermissions(
                        this,
                        Arrays.asList(Constants.EMAIL));
        LoginManager.getInstance()
                .setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, " OnSuccess " + loginResult);
                if (loginResult.getAccessToken() != null)
                    getFbData(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, " onCancel ");
            }

            @Override
            public void onError(FacebookException error) {
                hideLoadingDialog();
                Log.d(TAG, " Error " + error);
                AndroidUtil.displayAlertDialog(error.getLocalizedMessage(), AndroidUtil.getString(R.string.error), getApplicationContext());
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null)
                    Log.d(TAG, "sign out");
            }
        };
    }

    //************************************************************
    private void getFbData(@NonNull AccessToken accessToken)
    //************************************************************
    {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                (object, response) ->
                {
                    Log.v("LoginActivity", response.toString());
                    try {
                        String id = object.getString(Constants.ID);
                        String email = object.getString(Constants.EMAIL);
                        onLogin(id, email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    //******************************************************************
    private void onLogin(String id, String email)
    //******************************************************************
    {
        if (email == null || email.equals("")) {
            AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.email_required),
                    AndroidUtil.getString(R.string.login_error),
                    getApplicationContext());
            return;
        }
        showLoadingDialog();
        mSocialLoginViewModel.socialLogin(email, id, "facebook");
    }

    //******************************************************************
    private void setUserInformation()
    //******************************************************************
    {
        SharedPreferencesUtils.setValue(Constants.EMAIL, "");
        SharedPreferencesUtils.setValue(Constants.PASSWORD, "");
    }

    //******************************************************************
    @Override
    public void onLoginSuccessfully(@Nullable UserResponse userResponse)
    //******************************************************************
    {
        val remember = mBinding.rememberMe.isChecked();
        if (remember) {
            mBinding.rememberMe.setChecked(false);
        }
        if (userResponse != null) {
            Laila.instance().setMUser_U(userResponse);

            if (remember)
                SharedPreferencesUtils.setValue(Constants.REMEMBER, true);

            SharedPreferencesUtils.setValue(Constants.USER_DATA, userResponse);
            setUserInformation();
            getProfile();
        }
    }

    //*******************************************************************
    private void getProfile()
    //*******************************************************************
    {
        showLoadingDialog();
        mUpdateProfileViewModel.getProfile();
    }

    //******************************************************************
    @Override
    public void onLoginFailed(@NonNull String errorMessage)
    //******************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(Html.fromHtml(errorMessage).toString(), AndroidUtil.getString(R.string.alert), this);

    }

    //*******************************************************************
    @Override
    public void onSuccessSocialLogin(UserResponse response)
    //*******************************************************************
    {
        val remember = mBinding.rememberMe.isChecked();
        if (response.getData() != null) {
            Laila.instance().setMUser_U(response);
            if (remember)
                SharedPreferencesUtils.setValue(Constants.REMEMBER, true);
            SharedPreferencesUtils.setValue(Constants.USER_DATA, response);
        }
        setUserInformation();
        getProfile();
    }

    //*******************************************************************
    @Override
    public void onFailedSocialLogin(String message)
    //*******************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(Html.fromHtml(message).toString(), AndroidUtil.getString(R.string.error), this);
    }

    //*******************************************************************
    @Override
    public void onUpdateSuccessfully(@Nullable ProfileResponse response)
    //*******************************************************************
    {
        hideLoadingDialog();
        Laila.instance().getMUser_U().getData().setProfile(response.getData().getProfile());
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());
        hideLoadingDialog();
        gotoDashboardActivity();
    }

    //*******************************************************************
    @Override
    public void onUpdateFailed(@NonNull String errorMessage)
    //*******************************************************************
    {
        hideLoadingDialog();
        gotoDashboardActivity();
    }
}
