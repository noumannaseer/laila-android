package com.aditum.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.aditum.R;
import com.aditum.databinding.ResourcesListBinding;

import java.util.List;

import lombok.val;


//**************************************************************************
public class ResourcesListAdapter
        extends RecyclerView.Adapter<ResourcesListAdapter.AllergiesListViewHolder>
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
        ResourcesListBinding ResourcesListBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.resources_list,
                parent, false);
        return new AllergiesListViewHolder(ResourcesListBinding);
    }


    //********************************************************************************************
    public ResourcesListAdapter(List<String> mList, ListClickListener listClickListener)
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
        holder.resourcesListBinding.name.setText(item);
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

        ResourcesListBinding resourcesListBinding;

        //*******************************************************************
        public AllergiesListViewHolder(@NonNull ResourcesListBinding itemView)
        //*******************************************************************
        {
            super(itemView.getRoot());
            resourcesListBinding = itemView;
        }
    }

    //*******************************************************************
    public interface ListClickListener
            //*******************************************************************
    {
        void onClick(String title);
    }

}
