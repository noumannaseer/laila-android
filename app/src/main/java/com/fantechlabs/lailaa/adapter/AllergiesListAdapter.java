package com.fantechlabs.lailaa.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fantechlabs.lailaa.Laila;
import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.AllergiesListBinding;
import com.fantechlabs.lailaa.databinding.ResourcesAllergiesListBinding;

import java.util.List;

import lombok.val;


//**************************************************************************
public class AllergiesListAdapter
        extends RecyclerView.Adapter<AllergiesListAdapter.AllergiesListViewHolder>
//*******************************************************************
{
    private List<String> mAllergiesList;
    private ListClickListener mListClickListener;

    //*******************************************************************
    @NonNull
    @Override
    public AllergiesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //*******************************************************************
    {
        ResourcesAllergiesListBinding AllergiesListViewBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.resources_allergies_list,
                parent, false);
        return new AllergiesListViewHolder(AllergiesListViewBinding);
    }


    //********************************************************************************************
    public AllergiesListAdapter(List<String> mList, ListClickListener listClickListener)
    //********************************************************************************************
    {
        this.mAllergiesList = mList;
        this.mListClickListener = listClickListener;
        setHasStableIds(true);
    }

    //*********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull AllergiesListViewHolder holder, int position)
    //*********************************************************************************************
    {
        final val item = mAllergiesList
                .get(position);

        if (TextUtils.isEmpty(item))
            return;
        holder.AllergiesViewBinding.name.setText(item);

        holder.AllergiesViewBinding.delete.setOnClickListener(v ->
        {
            mListClickListener.onDelete(position);
        });

    }

    //*******************************************************************
    @Override
    public int getItemCount()
    //*******************************************************************
    {
        return mAllergiesList.size();
    }

    //*******************************************************************
    public class AllergiesListViewHolder
            extends RecyclerView.ViewHolder
            //*******************************************************************
    {

        ResourcesAllergiesListBinding AllergiesViewBinding;

        //*******************************************************************
        public AllergiesListViewHolder(@NonNull ResourcesAllergiesListBinding itemView)
        //*******************************************************************
        {
            super(itemView.getRoot());
            AllergiesViewBinding = itemView;
        }
    }

    //*******************************************************************
    public interface ListClickListener
            //*******************************************************************
    {
        void onDelete(int position);

        void onClick(String title);
    }

}
