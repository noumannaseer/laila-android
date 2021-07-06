package com.aditum.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.activities.QuestionsActivity;
import com.aditum.databinding.FragmentAddQuestionEightBinding;
import com.aditum.models.response_models.QuestionsResponse;
import com.aditum.request_models.QuestionsRequest;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;
import com.aditum.utils.SharedPreferencesUtils;
import com.aditum.view_models.AddQuestionsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.val;

//***********************************************************
public class AddQuestionEightFragment extends BaseFragment
        implements AddQuestionsViewModel.AddQuestionsListener
//***********************************************************
{

    private FragmentAddQuestionEightBinding mBinding;
    private View mRootView;
    private List<String> mRequestedQuestionsList;
    private QuestionsRequest mQuestionsRequest;
    private AddQuestionsViewModel mAddQuestionsViewModel;

    //***********************************************************
    public AddQuestionEightFragment()
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
            mBinding = FragmentAddQuestionEightBinding.inflate(inflater, parent, false);
            mRootView = mBinding.getRoot();
            initControl();
        }
        return mRootView;
    }

    //***********************************************************
    private void initControl()
    //***********************************************************
    {
        mAddQuestionsViewModel = new AddQuestionsViewModel(this);
        setAnswers();
        setRequestedData();
        goBackScreen();
        done();
    }

    //***********************************************************
    private void setAnswers()
    //***********************************************************
    {
        val questionsList = Laila.instance().getMUser_U().getData().getQuestions();
        if (questionsList == null)
            return;
        val answer = questionsList.get(7);
        mBinding.answer.setText(answer);
    }

    //***********************************************************
    private void setRequestedData()
    //***********************************************************
    {
        if (Laila.instance().getMRequestedQuestionsList() == null)
            return;
        if (Laila.instance().getMRequestedQuestionsList().size() == 7)
            return;
        val answer = Laila.instance().getMRequestedQuestionsList().get(7);
        mBinding.answer.setText(answer);
    }

    //***********************************************************
    private void setData()
    //***********************************************************
    {
        mRequestedQuestionsList = new ArrayList<>();
        val mAnswer = mBinding.answer.getText().toString().trim();

        Laila.instance().getMRequestedQuestionsList().add(7, mAnswer);
        mRequestedQuestionsList = Laila.instance().getMRequestedQuestionsList();

        mQuestionsRequest = Laila.instance().getMQuestionsRequest();

        if (mQuestionsRequest == null)
            mQuestionsRequest = Laila.instance().getMQuestionsRequest().Builder();

        mQuestionsRequest.setQuestions(mRequestedQuestionsList);

        Laila.instance().setMQuestionsRequest(mQuestionsRequest);

    }

    //***********************************************************
    private void goBackScreen()
    //***********************************************************
    {
        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((QuestionsActivity) getActivity()).navigateToScreen(6);
            }
        });

    }

    //***********************************************************
    private void done()
    //***********************************************************
    {
        mBinding.done.setOnClickListener(v -> {
            setData();
            showLoadingDialog();
            mAddQuestionsViewModel.addQuestions(mQuestionsRequest);
        });
    }

    //**********************************************************
    @Override
    public void onSuccessfully(@Nullable @org.jetbrains.annotations.Nullable QuestionsResponse response)
    //**********************************************************
    {
        hideLoadingDialog();
        if (response.getData() == null) {
            return;
        }
        Laila.instance().getMUser_U().getData().setQuestions(response.getData().getQuestions());
        SharedPreferencesUtils.setValue(Constants.USER_DATA, Laila.instance().getMUser_U());

        AndroidUtil.displayAlertDialog(
                AndroidUtil.getString(R.string.questionnaire_added),
                AndroidUtil.getString(
                        R.string.questionaries),
                getContext(),
                AndroidUtil.getString(
                        R.string.ok),
                (dialog, which) -> {
                    if (which == -1) {
                        hideLoadingDialog();
                        Laila.instance().setMRequestedQuestionsList(null);
                        getActivity().finish();
                    }
                });
    }

    //**********************************************************
    @Override
    public void onFailed(@NonNull @NotNull String errorMessage)
    //**********************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, "Alert", getContext());
    }
}
