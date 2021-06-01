package com.fantechlabs.lailaa.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.DocumentListLayoutBinding;
import com.fantechlabs.lailaa.models.updates.models.Document;
import com.fantechlabs.lailaa.utils.DateUtils;
import com.fantechlabs.lailaa.utils.UIUtils;

import java.util.List;

import lombok.val;


//**************************************************************************
public class DocumentsListAdapter
        extends RecyclerView.Adapter<DocumentsListAdapter.DocumentsListViewHolder>
//*******************************************************************
{

    private List<Document> mDocumentsList;
    private Activity mActivity;
    private int mPosition;
    private ListClickListener mListClickListener;
    private RequestManager mGlideRequestManager;


    //*******************************************************************
    @NonNull
    @Override
    public DocumentsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //*******************************************************************
    {
        DocumentListLayoutBinding documentsListViewBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.document_list_layout,
                parent, false);
        mGlideRequestManager = Glide.with(parent.getContext());
        return new DocumentsListViewHolder(documentsListViewBinding);
    }

    //************************* *******************************************************************
    public DocumentsListAdapter(List<Document> mFilterList, ListClickListener listClickListener, Activity activity)
    //************************* *******************************************************************
    {
        this.mDocumentsList = mFilterList;
        this.mActivity = activity;
        this.mListClickListener = listClickListener;
        setHasStableIds(true);
    }

    //*********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull DocumentsListViewHolder holder, int position)
    //*********************************************************************************************
    {
        final val item = mDocumentsList
                .get(position);

        if (TextUtils.isEmpty(item.getFileName()))
            return;
        holder.DocumentsViewBinding.name.setText(item.getFileName());
        val createdDate = DateUtils.getDateFromTimeStamp(item.getCreatedAt(), "dd-MMM-yyyy");

        holder.DocumentsViewBinding.date.setText(createdDate);

        getSize(Double.parseDouble(item.getFileSize()), holder);
        holder.DocumentsViewBinding.type.setText(item.getFileType());
        loadThumbnail(item.getThumbnail(), holder.DocumentsViewBinding.image, item.getFileType());

        holder.DocumentsViewBinding.optionMenu.setOnClickListener(v ->
        {
            if (mListClickListener != null)
//                mListClickListener.onDelete(mPosition, item.getId());
                onMenuClicked(holder, item, position);
        });
        holder.DocumentsViewBinding.documentCard.setOnClickListener(v ->
        {
            mListClickListener.openBrowserActivity(item.getFileName(), item.getFileUrl(), item.getFileType());
        });
    }

    //*******************************************************************
    private void getSize(@NonNull double size, DocumentsListViewHolder holder)
    //*******************************************************************
    {
        double rxKb = size / 1000;
        holder.DocumentsViewBinding.size.setText(String.format("%.02f", rxKb) + " KB");
        if (rxKb >= 1000) {
            double rxMB = rxKb / 1024;
            holder.DocumentsViewBinding.size.setText(String.format("%.02f", rxMB) + " MB");
        }
    }

    //*******************************************************************
    public void loadThumbnail(String url, ImageView imageView, String fileType)
    //*******************************************************************
    {
        mGlideRequestManager
                .load(url)
                .fallback(R.drawable.documents)
                .placeholder(fileType.equals(".pdf") ? R.drawable.pdf : fileType.equals(".docx") ? R.drawable.word : R.drawable.documents)
                .centerCrop()
                .thumbnail(0.1f)
                .into(imageView);
    }

    //*****************************************************************
    private void onMenuClicked(DocumentsListViewHolder holder, Document item, int position)
    //*****************************************************************
    {
        mPosition = position;
        PopupMenu popup = new PopupMenu(mActivity, holder.DocumentsViewBinding.optionMenu);
        popup.getMenuInflater()
                .inflate(R.menu.document_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item1 ->
        {
            val choice = item1.getTitle().toString();

            switch (choice) {
                case "Update":
                    if (mListClickListener != null)
                        mListClickListener.onUpdate(mPosition);
                    break;
                case "Delete":
                    if (mListClickListener != null)
                        mListClickListener.onDelete(mPosition, item.getId());
                    break;
            }
            return true;
        });
        popup.show();
    }

    //*******************************************************************
    @Override
    public int getItemCount()
    //*******************************************************************
    {
        return mDocumentsList.size();
    }

    //*******************************************************************
    public class DocumentsListViewHolder
            extends RecyclerView.ViewHolder
            //*******************************************************************
    {

        DocumentListLayoutBinding DocumentsViewBinding;

        //*******************************************************************
        public DocumentsListViewHolder(@NonNull DocumentListLayoutBinding itemView)
        //*******************************************************************
        {
            super(itemView.getRoot());
            DocumentsViewBinding = itemView;
        }
    }

    //*******************************************************************
    public interface ListClickListener
            //*******************************************************************
    {
        void onDelete(int position, int id);

        void onUpdate(int position);

        void openBrowserActivity(String title, String url, String type);
    }

}
