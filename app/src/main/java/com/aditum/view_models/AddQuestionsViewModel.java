package com.aditum.view_models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.aditum.Laila;
import com.aditum.R;
import com.aditum.models.response_models.QuestionsResponse;
import com.aditum.models.updates.request_models.AddMedicationRequest;
import com.aditum.models.updates.response_models.MedicationResponse;
import com.aditum.network.NetworkUtils;
import com.aditum.network.ServiceGenerator;
import com.aditum.network.services.MedicationService;
import com.aditum.request_models.QuestionsRequest;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.Constants;

import java.util.HashMap;

import lombok.val;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//***********************************************************
public class AddQuestionsViewModel
        extends ViewModel
//***********************************************************
{
    public AddQuestionsListener mAddQuestionsListener;

    //***********************************************************
    public AddQuestionsViewModel(@NonNull AddQuestionsListener mUserLoginListener)
    //***********************************************************
    {
        this.mAddQuestionsListener = mUserLoginListener;
    }

    //***********************************************************
    public void addQuestions(@NonNull QuestionsRequest questionsRequest)
    //***********************************************************
    {
        val service = ServiceGenerator.createService(MedicationService.class, true,
                Constants.BASE_URL_U);
        if (service == null) {
            mAddQuestionsListener.onFailed(
                    AndroidUtil.getString(R.string.internet_not_vailable));
            return;
        }

        val user_token = Laila.instance().getMUser_U().getData().getUser().getToken();
        val user_id = Laila.instance().getMUser_U().getData().getUser().getId();
        HashMap<String, Object> questionsList = new HashMap<String, Object>();


        questionsList.put("questions", questionsRequest.getQuestions());
        questionsList.put(Constants.USER_TOKEN, user_token);
        questionsList.put(Constants.USER_ID, user_id);

        val medicationService = service.addQuestions(questionsList);

        //***********************************************************
        medicationService.enqueue(new Callback<QuestionsResponse>()
                //***********************************************************
        {
            //***********************************************************
            @Override
            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response)
            //***********************************************************
            {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() != 200) {
                        mAddQuestionsListener.onFailed(response.body().getData().getMessage());
                        return;
                    }
                    mAddQuestionsListener.onSuccessfully(response.body());
                    return;
                }
                val error = NetworkUtils.errorResponse(response.errorBody());
                mAddQuestionsListener.onFailed(error);
            }

            //***********************************************************
            @Override
            public void onFailure(Call<QuestionsResponse> call, Throwable t)
            //***********************************************************
            {
                mAddQuestionsListener.onFailed(t.getLocalizedMessage());
            }
        });

    }


    //***********************************************************
    public interface AddQuestionsListener
            //***********************************************************
    {
        void onSuccessfully(@Nullable QuestionsResponse response);

        void onFailed(@NonNull String errorMessage);

    }

}
