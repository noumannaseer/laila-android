package com.aditum.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.aditum.Laila;
import com.aditum.activities.QuestionsActivity;
import com.aditum.databinding.FragmentAddQuestionThreeBinding;
import com.aditum.request_models.QuestionsRequest;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

//***********************************************************
public class AddQuestionThreeFragment extends BaseFragment
//***********************************************************
{

    private FragmentAddQuestionThreeBinding mBinding;
    private View mRootView;
    private List<String> mRequestedQuestionsList;
    private QuestionsRequest mQuestionsRequest;

    //***********************************************************
    public AddQuestionThreeFragment()
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
            mBinding = FragmentAddQuestionThreeBinding.inflate(inflater, parent, false);
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
        val answer = questionsList.get(2);

        if (mBinding.question3Yes.getText().toString().toLowerCase().equals(answer))
            mBinding.question3Yes.setChecked(true);
        if (mBinding.question3No.getText().toString().toLowerCase().equals(answer))
            mBinding.question3No.setChecked(true);

    }

    //***********************************************************
    private void setRequestedData()
    //***********************************************************
    {
        if (Laila.instance().getMRequestedQuestionsList() == null)
            return;
        if (Laila.instance().getMRequestedQuestionsList().size() == 2)
            return;
        val answer = Laila.instance().getMRequestedQuestionsList().get(2);
        if (mBinding.question3Yes.getText().toString().toLowerCase().equals(answer))
            mBinding.question3Yes.setChecked(true);
        if (mBinding.question3No.getText().toString().toLowerCase().equals(answer))
            mBinding.question3No.setChecked(true);

    }

    //***********************************************************
    private void setData()
    //***********************************************************
    {
        mRequestedQuestionsList = new ArrayList<>();

        mRequestedQuestionsList = Laila.instance().getMRequestedQuestionsList();

        int selectedId = mBinding.question3RadioGroup.getCheckedRadioButtonId();
        val radioButton = (RadioButton) mRootView.findViewById(selectedId);
        val answer = radioButton.getText().toString().toLowerCase();

        mRequestedQuestionsList.add(2, answer);

        mQuestionsRequest = Laila.instance().getMQuestionsRequest();

        if (mQuestionsRequest == null)
            mQuestionsRequest = Laila.instance().getMQuestionsRequest().Builder();

        mQuestionsRequest.setQuestions(mRequestedQuestionsList);

        Laila.instance().setMQuestionsRequest(mQuestionsRequest);

        ((QuestionsActivity) getActivity()).navigateToScreen(3);
    }

    //***********************************************************
    private void goBackScreen()
    //***********************************************************
    {
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((QuestionsActivity) getActivity()).navigateToScreen(1);
            }
        });

    }

    //***********************************************************
    private void nextQuestion()
    //***********************************************************
    {
        mBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
            }
        });
    }
}
