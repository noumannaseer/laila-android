package com.aditum.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aditum.databinding.DialogDocumentNameBinding;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.aditum.Laila;
import com.aditum.request_models.DocumentRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

import lombok.Setter;
import lombok.val;

import static com.aditum.utils.Constants.DOCUMENT;
import static com.aditum.utils.Constants.IMAGE;
import static com.aditum.utils.Constants.mimeTypes;


//***********************************************************
public class AddDocumentDialog
        extends BottomSheetDialogFragment
//***********************************************************
{

    private DialogDocumentNameBinding mBinding;
    private View rootView;
    @Setter
    private AddDocumentClickListener mListener;
    private DocumentRequest mDocumentRequest;

    //********************************************************************************************************************
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    //********************************************************************************************************************
    {
        if (rootView == null)
        {
            mBinding = DialogDocumentNameBinding.inflate(inflater, container, false);
            rootView = mBinding.getRoot();
        }
        initControls();
        return rootView;
    }

    //***********************************************************
    private void initControls()
    //***********************************************************
    {
        mBinding.captureFromCamera.setOnClickListener(view -> addDocument(0));
        mBinding.fileFromMemory.setOnClickListener(view -> addDocument(1));
        mBinding.cancel.setOnClickListener(view -> this.dismiss());
        mBinding.upload.setOnClickListener(view -> {
            if (mDocumentRequest == null)
            {
                AndroidUtil.toast(false, "Document detail required");
                return;
            }
            this.dismiss();
            if (mListener != null)
                mListener.onNameSubmit(mDocumentRequest);
        });
    }

    //************************************************************
    private void addDocument(int type)
    //************************************************************
    {
        switch (type)
        {
            case 0:
                showCameraDialog();
                break;
            case 1:
                browseDocuments();
                break;
        }
    }

    //**********************************************
    public void showCameraDialog()
    //**********************************************
    {
        this.dismiss();
        ImagePicker.create(this)
                .cameraOnly()
                .start( AddDocumentDialog.this);
    }

    //***********************************************************************
    private void browseDocuments()
    //***********************************************************************
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0)
            {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        }
        else
        {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes)
            {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 120);
    }

    //*********************************************************************
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    //**********************************************
    {
        if (data != null)
        {
            mDocumentRequest = DocumentRequest.Builder();
            mDocumentRequest.setUser_private_code(Laila.instance().getMUser().getProfile().getUserPrivateCode());

            switch (requestCode)
            {
                case 120:
                    val url = data.getData();
                    mDocumentRequest.setType(DOCUMENT);
                    mDocumentRequest.setFile(url.toString());
                    break;
                case 553:
                    Image image = ImagePicker.getFirstImageOrNull(data);
                    if (image != null)
                    {
                        val uri = Uri.fromFile(new File(image.getPath()));
                        mDocumentRequest.setType(IMAGE);
                        mDocumentRequest.setFile(uri.toString());
                        break;
                    }
            }

            if (mDocumentRequest == null)
            {
                AndroidUtil.toast(false, "Document detail required");
                return;
            }
            this.dismiss();
            if (mListener != null)
                mListener.onNameSubmit(mDocumentRequest);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //************************************************************
    public static String getMimeType(String url)
    //************************************************************
    {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null)
        {
            type = MimeTypeMap.getSingleton()
                              .getMimeTypeFromExtension(extension);
        }
        return type;
    }

    //************************************************************
    @Override
    public void onStart()
    //************************************************************
    {
        super.onStart();
    }

    //***********************************************************
    public interface AddDocumentClickListener
    //***********************************************************
    {
        void onNameSubmit(DocumentRequest documentRequest);
    }

}
