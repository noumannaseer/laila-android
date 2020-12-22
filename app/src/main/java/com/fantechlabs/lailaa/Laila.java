package com.fantechlabs.lailaa;

import android.app.Application;
import android.content.Context;

import com.fantechlabs.lailaa.models.Medication;
import com.fantechlabs.lailaa.models.Profile;
import com.fantechlabs.lailaa.models.SearchMedicine;
import com.fantechlabs.lailaa.models.response_models.UserResponse;
import com.fantechlabs.lailaa.request_models.AddMedicationRequest;
import com.fantechlabs.lailaa.request_models.AddPharmacyRequest;
import com.fantechlabs.lailaa.request_models.ProfileRequest;
import com.fantechlabs.lailaa.request_models.UserRequest;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.AutoCompleteLoadingBar;

import lombok.Getter;
import lombok.Setter;

//**********************************************************
public class Laila extends Application
//**********************************************************
{
    private static Context mContext;
    public AutoCompleteLoadingBar mAutoCompleteLoadingBar;

    @Getter
    @Setter
    private UserResponse mUser;
    @Getter
    @Setter
    private UserRequest userRequest;
    @Getter
    @Setter
    private ProfileRequest mProfileRequest;
    @Getter
    @Setter
    private SearchMedicine mSearchMedicine;
    @Getter
    @Setter
    private AddMedicationRequest mAddMedicationRequest;
    @Getter
    @Setter
    public Medication mUpdateMedication;

    public boolean Edit_Profile = false;
    public boolean on_update_medicine = false;
    public boolean is_medicine_added = false;
    public boolean is_pharmacy_added = false;
    @Getter
    @Setter
    public int mMedicationPosition;
    @Getter
    @Setter
    public int mMedicationId;
    @Getter
    @Setter
    private AddPharmacyRequest mAddPharmacyRequest;

    //****************************************************************
    @Override
    public void onCreate()
    //****************************************************************
    {
        super.onCreate();
        mContext = getApplicationContext();
        AndroidUtil.setContext(mContext);
        mAutoCompleteLoadingBar = new AutoCompleteLoadingBar(mContext);

//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }

    //****************************************************************
    public static Laila instance()
    //****************************************************************
    {
        return (Laila) AndroidUtil.getApplicationContext();
    }

    //****************************************************************
    public Profile getCurrentUserProfile()
    //****************************************************************
    {
        if (mUser == null)
            return null;
        if (mUser.getProfile() == null)
            return null;
        return mUser.getProfile();
    }

}
