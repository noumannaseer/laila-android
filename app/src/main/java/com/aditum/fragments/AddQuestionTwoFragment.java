package com.aditum.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aditum.Laila;
import com.aditum.activities.QuestionsActivity;
import com.aditum.databinding.FragmentAddQuestionTwoBinding;
import com.aditum.request_models.QuestionsRequest;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

//***********************************************************
public class AddQuestionTwoFragment extends BaseFragment
//***********************************************************
{

    private FragmentAddQuestionTwoBinding mBinding;
    private View mRootView;
    private List<String> mRequestedQuestionsList;
    private QuestionsRequest mQuestionsRequest;

    //***********************************************************
    public AddQuestionTwoFragment()
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
            mBinding = FragmentAddQuestionTwoBinding.inflate(inflater, parent, false);
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
        goBackScreen();
        nextQuestion();
    }

    //***********************************************************
    private void setAnswers()
    //***********************************************************
    {
        val questionsList = Laila.instance().getMUser_U().getData().getQuestions();
        if (questionsList == null)
            return;
        val answer = questionsList.get(1);
        mBinding.answer.setText(answer);
    }

    //***********************************************************
    private void setRequestedData()
    //***********************************************************
    {
        if (Laila.instance().getMRequestedQuestionsList() == null)
            return;
        if (Laila.instance().getMRequestedQuestionsList().size() == 1)
            return;
        val answer = Laila.instance().getMRequestedQuestionsList().get(1);
        mBinding.answer.setText(answer);
    }

    //***********************************************************
    private void setData()
    //***********************************************************
    {
        mRequestedQuestionsList = new ArrayList<>();
        val mAnswer = mBinding.answer.getText().toString().trim();

        Laila.instance().getMRequestedQuestionsList().add(1, mAnswer);
        mRequestedQuestionsList = Laila.instance().getMRequestedQuestionsList();

        mQuestionsRequest = Laila.instance().getMQuestionsRequest();

        if (mQuestionsRequest == null)
            mQuestionsRequest = Laila.instance().getMQuestionsRequest().Builder();

        mQuestionsRequest.setQuestions(mRequestedQuestionsList);

        Laila.instance().setMQuestionsRequest(mQuestionsRequest);

        ((QuestionsActivity) getActivity()).navigateToScreen(2);
    }

    //***********************************************************
    private void goBackScreen()
    //***********************************************************
    {
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((QuestionsActivity) getActivity()).navigateToScreen(0);
            }
        });

    }

    //***********************************************************
    private void nextQuestion()
    //***********************************************************
    {
        mBinding.next.setOnClickListener(v -> setData());
    }
}
