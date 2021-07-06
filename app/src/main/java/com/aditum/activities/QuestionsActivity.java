package com.aditum.activities;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.databinding.ActivityQuestionsBinding;
import com.aditum.fragments.AddQuestionEightFragment;
import com.aditum.fragments.AddQuestionFiveFragment;
import com.aditum.fragments.AddQuestionFourFragment;
import com.aditum.fragments.AddQuestionOneFragment;
import com.aditum.fragments.AddQuestionSevenFragment;
import com.aditum.fragments.AddQuestionSixFragment;
import com.aditum.fragments.AddQuestionThreeFragment;
import com.aditum.fragments.AddQuestionTwoFragment;
import com.aditum.utils.AndroidUtil;

import java.util.ArrayList;
import java.util.List;

//**********************************************************
public class QuestionsActivity extends BaseActivity
//**********************************************************
{
    private ActivityQuestionsBinding mBinding;
    private int mCounter = 0;
    private List<String> mQuestionList;

    //**********************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //**********************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_questions);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setTitle("");
        initControls(savedInstanceState);
    }

    //****************************************************************
    private void initControls(@Nullable Bundle savedInstanceStates)
    //****************************************************************
    {
        if (savedInstanceStates == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_questions,
                            new AddQuestionOneFragment())
                    .commit();
        }
        mQuestionList = new ArrayList<>();
        skip();
    }

    //****************************************************************
    private void skip()
    //****************************************************************
    {
        mBinding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionList.add("");
                Laila.instance().setMRequestedQuestionsList(mQuestionList);
                navigateToScreen(mCounter + 1);
            }
        });
    }

    //****************************************************************
    public void navigateToScreen(int index)
    //****************************************************************
    {
        switch (index) {
            case 0:
                mBinding.view1.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view2.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view3.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view4.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view5.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view6.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view7.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view8.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));

                mCounter = 0;

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_questions,
                                new AddQuestionOneFragment())
                        .commit();
                break;
            case 1:
                mBinding.view1.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view2.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view3.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view4.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view5.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view6.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view7.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view8.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mCounter = 1;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_questions,
                                new AddQuestionTwoFragment())
                        .commit();
                break;
            case 2:
                mBinding.view1.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view2.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view3.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view4.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view5.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view6.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view7.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view8.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mCounter = 2;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_questions,
                                new AddQuestionThreeFragment())
                        .commit();
                break;
            case 3:
                mBinding.view1.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view2.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view3.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view4.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view5.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view6.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view7.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view8.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mCounter = 3;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_questions,
                                new AddQuestionFourFragment())
                        .commit();
                break;
            case 4:
                mBinding.view1.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view2.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view3.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view4.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view5.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view6.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view7.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view8.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mCounter = 4;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_questions,
                                new AddQuestionFiveFragment())
                        .commit();
                break;
            case 5:
                mBinding.view1.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view2.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view3.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view4.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view5.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view6.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view7.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mBinding.view8.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mCounter = 5;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_questions,
                                new AddQuestionSixFragment())
                        .commit();
                break;
            case 6:
                mBinding.view1.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view2.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view3.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view4.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view5.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view6.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view7.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view8.setBackground(AndroidUtil.getDrawable(R.drawable.view_corners));
                mCounter = 6;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_questions,
                                new AddQuestionSevenFragment())
                        .commit();
                break;
            case 7:
                mBinding.view1.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view2.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view3.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view4.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view5.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view6.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view7.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mBinding.view8.setBackground(AndroidUtil.getDrawable(R.drawable.selected_view_corners));
                mCounter = 7;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_questions,
                                new AddQuestionEightFragment())
                        .commit();
                break;
        }
    }

    //**********************************************************
    @Override
    public void onBackPressed()
    //**********************************************************
    {
        super.onBackPressed();
        Laila.instance().setMRequestedQuestionsList(null);
    }

    //**********************************************************
    @Override
    protected boolean showStatusBar()
    //**********************************************************
    {
        return false;
    }
}