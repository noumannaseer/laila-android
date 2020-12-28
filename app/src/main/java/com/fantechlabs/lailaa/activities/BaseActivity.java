package com.fantechlabs.lailaa.activities;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.utils.AndroidUtil;

//****************************************************
public abstract class BaseActivity
        extends AppCompatActivity
//****************************************************
{
    private Dialog mDialog;

    //********************************************************************
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState)
    //********************************************************************
    {
        super.onCreate(savedInstanceState);
        onCreation(savedInstanceState);
    }

    void processStatusBar(boolean showStatusBar)
    {
        if (showStatusBar)
            return;
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener()
                {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility)
                    {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                        {
                            decorView.setSystemUiVisibility(uiOptions);
                        }
                        else
                        {
                        }
                    }
                });
    }

    //********************************************************************
    protected void showLoadingDialog()
    //********************************************************************
    {
        if (mDialog == null)
        {
             mDialog = new Dialog(this, R.style.CustomTransparentDialog);
            mDialog.setContentView(R.layout.progress_dialog);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }
        mDialog.show();
    }

    //********************************************************************
    protected void startAPICall(Object... params)
    //********************************************************************
    {
        showLoadingDialog();
    }

    //********************************************************************
    protected void showLoadingDialog(@NonNull String loadingMessage)
    //********************************************************************
    {
        if (mDialog == null)
        {
            mDialog = new Dialog(this, R.style.CustomTransparentDialog);
            mDialog.setContentView(R.layout.progress_dialog);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
        }
        ImageView image = mDialog.findViewById(R.id.splash_icon);
        ObjectAnimator animation = ObjectAnimator.ofFloat(image, "rotationY", 0.0f, 360f);
        animation.setDuration(800);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
        TextView textView = mDialog.findViewById(R.id.loading_text);
        textView.setText(loadingMessage);

        mDialog.show();
    }

    //************************************************************
    protected void setText(@androidx.annotation.NonNull TextView view, @Nullable String value)
    //************************************************************
    {
        view.setText(TextUtils.isEmpty(value) ? getString(R.string.n_a) : value);
    }

    //************************************************************
    protected void setTextPrice(@androidx.annotation.NonNull TextView view, @Nullable String value)
    //************************************************************
    {
        view.setText(
                TextUtils.isEmpty(value)
                ? getString(R.string.n_a) :
                AndroidUtil.getString(R.string.total_ammout_template, value));
    }

    //********************************************************************
    protected void hideLoadingDialog()
    //********************************************************************
    {
        if (mDialog != null && mDialog.isShowing())
            mDialog.dismiss();
    }

    /**
     * All the activity should override this.
     *
     * @param savedInstanceState
     */
    //********************************************************************
    protected abstract void onCreation(@Nullable Bundle savedInstanceState);
    //********************************************************************

    /**
     * have your base layout here, inflate/add on top of your base layout
     *
     * @param layoutResID
     */
    //********************************************************************
    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
    }
    //********************************************************************

    /**
     * have your base layout here, inflate/add on top of your base layout
     *
     * @param view
     */
    //*************************************************************
    @Override
    public void setContentView(View view)
    //*************************************************************
    {
        super.setContentView(view);
    }

    protected abstract boolean showStatusBar();

    /**
     * Method 1:
     * This has to be called when theme is changed in the activity.This internally recreated the activity
     */
    //*************************************************************
    public void reCreate()
    //*************************************************************
    {
        Bundle savedInstanceState = new Bundle();
        //this is important to save all your open states/fragment states
        onSaveInstanceState(savedInstanceState);
        //this is needed to release the resources
        super.onDestroy();

        //call on create where new theme is applied
        onCreate(
                savedInstanceState);//you can pass bundle arguments to skip your code/flows on this scenario
    }

    /**
     * Method 2:
     * This has to be called when theme is changed in the activity.This internally restarts the activity
     * This gives you the flicker
     */
    //*************************************************************
    public void restartActivity()
    //*************************************************************
    {
        Intent i = getIntent();
        this.overridePendingTransition(0, 0);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.finish();
        //restart the activity without animation
        this.overridePendingTransition(0, 0);
        this.startActivity(i);
    }


    // ******************************************************************
    @MainThread
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    // ******************************************************************
    {
        switch (item.getItemId())
        {
        case android.R.id.home:
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}