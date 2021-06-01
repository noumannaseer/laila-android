package com.fantechlabs.lailaa.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.adapter.DocumentsListAdapter;
import com.fantechlabs.lailaa.databinding.ActivityMedicalRecordsBinding;
import com.fantechlabs.lailaa.models.updates.models.Document;
import com.fantechlabs.lailaa.models.updates.request_models.DocumentRequest;
import com.fantechlabs.lailaa.models.updates.response_models.DocumentResponse;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.permissions.Permission;
import com.fantechlabs.lailaa.view_models.DocumentsViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import lombok.val;

import static com.fantechlabs.lailaa.utils.AndroidUtil.getContext;
import static com.fantechlabs.lailaa.utils.Constants.DOCUMENT;
import static com.fantechlabs.lailaa.utils.Constants.IMAGE;
import static com.fantechlabs.lailaa.utils.Constants.mimeTypes;

//******************************************************************************
public class MedicalRecordsActivity extends BaseActivity
        implements DocumentsViewModel.DocumentsViewListener, Permission.OnResult
//******************************************************************************
{
    private ActivityMedicalRecordsBinding mBinding;
    private DocumentsViewModel mDocumentsViewModel;
    private DocumentsListAdapter mDocumentsListAdapter;
    private Permission mPermission;
    private List<Document> mDocumentList;
    private int mPosition, mFileId;
    private AlertDialog.Builder mBuilder;
    private boolean isCalled = false;
    private Uri mUri;

    //******************************************************************************
    @Override
    protected void onCreation(@Nullable Bundle savedInstanceState)
    //******************************************************************************
    {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_medical_records);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        initControls();
    }

    //******************************************************************************
    private void initControls()
    //******************************************************************************
    {
        mDocumentsViewModel = new DocumentsViewModel(this, MedicalRecordsActivity.this);
        mBinding.add.setOnClickListener(v -> {
            initPermission();
        });

        showLoadingDialog();
        mDocumentsViewModel.getDocument();
    }

    //*******************************************
    private void selectDocumentsOrImage()
    //*******************************************
    {
        final CharSequence[] options = {"Take a Picture", "Upload Document", "Cancel"};

        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Select Option");
        mBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (options[item].equals("Take a Picture")) {
                    dialog.dismiss();
                    ImagePicker.cameraOnly().start(MedicalRecordsActivity.this);
                } else if (options[item].equals("Upload Document")) {
                    dialog.dismiss();
                    browseDocuments();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        mBuilder.show();
    }

    //***********************************************************************
    private void browseDocuments()
    //***********************************************************************
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 120);
    }

    //*********************************************************************
    @SneakyThrows
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    //********************************************************************
    {
        if (data != null) {
            switch (requestCode) {
                case 120:
                    val url = data.getData();
                    addDocuments(url.toString(), DOCUMENT);
                    break;
                case 553:
                    Image image = ImagePicker.getFirstImageOrNull(data);
                    if (image != null) {
                        mUri = Uri.fromFile(new File(image.getPath()));
                        addDocuments(mUri.toString(), IMAGE);
                        break;
                    }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //*********************************************************************
    private void addDocuments(@NonNull String url, @NonNull String type)
    //*********************************************************************
    {
        val user_id = Laila.instance().getMUser_U().getData().getUser().getId();
        val token = Laila.instance().getMUser_U().getData().getUser().getToken();

        DocumentRequest documentRequest = new DocumentRequest();
        documentRequest.setFile(url);
        documentRequest.setType(type);
        documentRequest.setUser_id(user_id);
        documentRequest.setToken(token);
        showLoadingDialog();
        mDocumentsViewModel.addDocument(documentRequest);
    }

    //*********************************************************************
    private void initPermission()
    //*********************************************************************
    {
        isCalled = false;
        mPermission = new Permission(
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, MedicalRecordsActivity.this);
        mPermission.request(MedicalRecordsActivity.this);
    }

    //**************************************************************
    @Override
    public void onSuccessfully(@Nullable DocumentResponse response)
    //**************************************************************
    {
        hideLoadingDialog();

        mDocumentList = new ArrayList<>();
        mDocumentList = response.getData().getDocumentsList();
        startRecyclerView();

    }

    //**************************************************************
    @Override
    public void onRecordAddedSuccessfully(@Nullable DocumentResponse response)
    //**************************************************************
    {
        hideLoadingDialog();
        if (response == null)
            return;
        mDocumentList.add(0, response.getData().getDocument());
        startRecyclerView();
    }

    //**************************************************************
    @Override
    public void onSuccessfullyDeleted(@Nullable String msg)
    //**************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(msg, AndroidUtil.getString(R.string.success), this);
        mDocumentList.remove(mPosition);
        startRecyclerView();
    }

    //**************************************************************
    @Override
    public void onFailed(@NonNull String errorMessage)
    //**************************************************************
    {
        hideLoadingDialog();
        AndroidUtil.displayAlertDialog(errorMessage, AndroidUtil.getString(R.string.error), this);
    }

    //**************************************************************
    private void startRecyclerView()
    //**************************************************************
    {
        if (mDocumentList == null || mDocumentList.size() == 0) {
            mBinding.noRecord.setVisibility(View.VISIBLE);
            mBinding.recyclerView.setVisibility(View.GONE);
            return;
        }

        mBinding.noRecord.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);

        mDocumentsListAdapter = new DocumentsListAdapter(mDocumentList, new DocumentsListAdapter.ListClickListener() {

            //******************************************************************
            @Override
            public void onDelete(int position, int id)
            //******************************************************************
            {
                mPosition = position;
                mFileId = id;
                AndroidUtil.displayAlertDialog(
                        AndroidUtil.getString(
                                R.string.delete_item),
                        AndroidUtil.getString(
                                R.string.alert),
                        MedicalRecordsActivity.this,
                        AndroidUtil.getString(
                                R.string.ok),
                        AndroidUtil.getString(
                                R.string.cancel),
                        (dialog, which) -> {
                            if (which == -1) {
                                showLoadingDialog();
                                mDocumentsViewModel.deleteDocument(mFileId);
                            }
                        });

            }

            //******************************************************************
            @Override
            public void onUpdate(int position)
            //******************************************************************
            {

            }

            //******************************************************************
            @Override
            public void openBrowserActivity(String title, String url, String type)
            //******************************************************************
            {
//                if (type.equals("JPEG") || type.equals("PNG") || type.equals("JPG") || type.equals("GIF")) {
//                    Intent specialIntent = new Intent(MedicalRecordsActivity.this, DocumentDetailsActivity.class);
//                    specialIntent.putExtra(DocumentDetailsActivity.IMAGE_URL, url);
//                    startActivity(specialIntent);
//                    return;
//                }
//                goToBrowser(title, url);
                Intent specialIntent = new Intent(MedicalRecordsActivity.this, DocumentDetailsActivity.class);
                specialIntent.putExtra(DocumentDetailsActivity.IMAGE_URL, url);
                startActivity(specialIntent);
            }

        }, this);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mBinding.recyclerView.setAdapter(mDocumentsListAdapter);

    }

    //*****************************************************************************
    @Override
    public void onPermission(@NonNull String permission, @NonNull Permission.Status status)
    //*****************************************************************************
    {
        if (AndroidUtil.checkPermission(Manifest.permission.CAMERA)
                && AndroidUtil.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                & !isCalled) {
            isCalled = true;
            selectDocumentsOrImage();
        }
    }

    //*******************************************************************
    private void goToBrowser(String title, String url)
    //*******************************************************************
    {
        Intent browserIntent = new Intent(this, BrowserActivity.class);
        browserIntent.putExtra(BrowserActivity.SCREEN_URL, url);
        browserIntent.putExtra(BrowserActivity.INFO_TITLE, title);
        startActivity(browserIntent);
    }

    //******************************************************************************
    @Override
    protected boolean showStatusBar()
    //******************************************************************************
    {
        return false;
    }
}