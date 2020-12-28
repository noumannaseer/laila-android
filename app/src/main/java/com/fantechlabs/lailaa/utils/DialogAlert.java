package com.fantechlabs.lailaa.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.InputDialogAlertBinding;
import com.fantechlabs.lailaa.models.Contact;

import lombok.Setter;
import lombok.val;


//********************************************************
public class DialogAlert extends DialogFragment
//********************************************************
{

    private InputDialogAlertBinding mBinding;
    private View rootView;
    public  DialogAlert.DialogAlertValues mDialogListener;
    @Setter
    Contact mContact;
    private int id;
    private String mPhone;


    //***********************************************************
    public DialogAlert(@NonNull DialogAlertValues mDialogListener)
    //***********************************************************
    {
        this.mDialogListener = mDialogListener;
    }

    //********************************************
    public DialogAlert()
    //********************************************
    {
    }

    //********************************************************************************************************************
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    //********************************************************************************************************************
    {
        mBinding = InputDialogAlertBinding.inflate(inflater, container, false);
        rootView = mBinding.getRoot();
        initControls();
        return rootView;
    }

    //***************************************
    public void initControls()
    //***************************************
    {
        onUpdateDate();
        mBinding.cancelButton.setOnClickListener(v -> this.dismiss());
        mBinding.saveBtn.setOnClickListener(v ->
        {
            val name = mBinding.careTakerName.getText().toString();
            mPhone = mBinding.careTakerPhone.getText().toString();

            if (TextUtils.isEmpty(name)) {
                mBinding.careTakerName.setError(AndroidUtil.getString(R.string.required));
                mBinding.careTakerName.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(mPhone)) {
                mBinding.careTakerPhone.setError(AndroidUtil.getString(R.string.phone_required));
                mBinding.careTakerPhone.requestFocus();
                return;
            }
            if (!TextUtils.isDigitsOnly(mPhone)) {
                mBinding.careTakerPhone.setError(AndroidUtil.getString(R.string.phone_digit));
                mBinding.careTakerPhone.requestFocus();
                return;
            }
            if (mPhone.length() != 10) {
                mBinding.careTakerPhone.setError(AndroidUtil.getString(R.string.phone_check));
                mBinding.careTakerPhone.requestFocus();
                return;
            }
            mPhone = "1" + mPhone;
            if (mContact != null)
            {
                mDialogListener.onUpdate(id, name, mPhone);
                this.dismiss();
                return;
            }
                mDialogListener.getDialogValues(0, name, mPhone);


            this.dismiss();
        });


    }

    //*********************************************
    private void onUpdateDate()
    //*********************************************
    {
        if (mContact == null)
            return;
        if (!TextUtils.isEmpty(mContact.getFirstName()))
            mBinding.careTakerName.setText(mContact.getFirstName());
        if (!TextUtils.isEmpty(mContact.getPhone()))
            mBinding.careTakerPhone.setText(mContact.getPhone());
        id = mContact.getId();
    }

    //*********************************************
    @Override
    public void onStart()
    //*********************************************
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow()
                    .setLayout(width, height);
        }
    }

    //***********************************************************
    public interface DialogAlertValues
    //***********************************************************
    {
        void getDialogValues(@NonNull int id, @Nullable String name, @NonNull String phone);

        void onUpdate(@NonNull int id, @Nullable String name, @NonNull String phone);
    }

}

