package com.fantechlabs.lailaa.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.AllergiesListBinding;
import com.fantechlabs.lailaa.databinding.AllergySummaryListBinding;

import java.util.HashMap;
import java.util.List;

import lombok.val;


//**************************************************************************
public class DetailAllergieAdapter
        extends RecyclerView.Adapter<DetailAllergieAdapter.AllergiesListViewHolder>
//*******************************************************************
{
    private final List<String> mSummaryList;
    private final ListClickListener mListClickListener;

    //*******************************************************************
    @NonNull
    @Override
    public AllergiesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //*******************************************************************
    {
        AllergySummaryListBinding AllergySummaryListBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.allergy_summary_list,
                parent, false);
        return new AllergiesListViewHolder(AllergySummaryListBinding);
    }


    //********************************************************************************************
    public DetailAllergieAdapter(List<String> mList, ListClickListener listClickListener)
    //********************************************************************************************
    {
        this.mSummaryList = mList;
        this.mListClickListener = listClickListener;

    }

    //*********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull AllergiesListViewHolder holder, int position)
    //*********************************************************************************************
    {
        final val item = mSummaryList
                .get(position);

        if (TextUtils.isEmpty(item))
            return;
        holder.AllergySummaryListBinding.summary.setText(item);
        holder.AllergySummaryListBinding.readMore.setText(R.string.read_more);
        holder.AllergySummaryListBinding.readMore.setOnClickListener(v ->
        {
            mListClickListener.onClick(position);
        });
    }

    //*******************************************************************
    @Override
    public int getItemCount()
    //*******************************************************************
    {
        return mSummaryList.size();
    }

    //*******************************************************************
    public class AllergiesListViewHolder
            extends RecyclerView.ViewHolder
            //*******************************************************************
    {

        AllergySummaryListBinding AllergySummaryListBinding;

        //*******************************************************************
        public AllergiesListViewHolder(@NonNull AllergySummaryListBinding itemView)
        //*******************************************************************
        {
            super(itemView.getRoot());
            AllergySummaryListBinding = itemView;
        }
    }

    //*******************************************************************
    public interface ListClickListener
            //*******************************************************************
    {
        void onClick(int position);
    }
}
