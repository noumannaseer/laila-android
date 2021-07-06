package com.aditum.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aditum.Laila;
import com.aditum.activities.QuestionsActivity;
import com.aditum.databinding.FragmentAddQuestionOneBinding;
import com.aditum.request_models.QuestionsRequest;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

//***********************************************************
public class AddQuestionOneFragment extends BaseFragment
//***********************************************************
{

    private FragmentAddQuestionOneBinding mBinding;
    private View mRootView;
    private List<String> mRequestedQuestionsList;
    private QuestionsRequest mQuestionsRequest;
    private String mAnswer;

    //***********************************************************
    public AddQuestionOneFragment()
    //***********************************************************
    {
        // Required empty public constructor
    }

    //***********************************************************
    @Override
    public View onCreateViewBaseFragment(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    //***********************************************************
    {
        if (mRootView == null) {
            mBinding = FragmentAddQuestionOneBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
            initControl();
        }
        return mRootView;
    }

    //***********************************************************
    private void initControl()
    //***********************************************************
    {
        setAnswers();
        setRequestedData();
        nextQuestion();
    }

    //***********************************************************
    private void setAnswers()
    //***********************************************************
    {
        val questionsList = Laila.instance().getMUser_U().getData().getQuestions();
        if (questionsList == null)
            return;
        val answer = questionsList.get(0);
        mBinding.answer.setText(answer);
    }

    //***********************************************************
    private void setRequestedData()
    //***********************************************************
    {
        if (Laila.instance().getMRequestedQuestionsList() == null)
            return;
        val answer = Laila.instance().getMRequestedQuestionsList().get(0);
        mBinding.answer.setText(answer);
    }

    //***********************************************************
    private void setData()
    //***********************************************************
    {
        mRequestedQuestionsList = new ArrayList<>();
        mAnswer = mBinding.answer.getText().toString().trim();

        Laila.instance().setMRequestedQuestionsList(new ArrayList<>());
        Laila.instance().getMRequestedQuestionsList().add(0, mAnswer);
        mRequestedQuestionsList = Laila.instance().getMRequestedQuestionsList();

        mQuestionsRequest = Laila.instance().getMQuestionsRequest();

        if (mQuestionsRequest == null)
            mQuestionsRequest = Laila.instance().getMQuestionsRequest().Builder();

        mQuestionsRequest.setQuestions(mRequestedQuestionsList);

        Laila.instance().setMQuestionsRequest(mQuestionsRequest);

        ((QuestionsActivity) getActivity()).navigateToScreen(1);
    }

    //***********************************************************
    private void nextQuestion()
    //***********************************************************
    {
        mBinding.answerMoreQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });
    }
}
