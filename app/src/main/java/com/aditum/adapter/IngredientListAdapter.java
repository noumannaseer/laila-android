package com.aditum.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aditum.R;
import com.aditum.databinding.IngredientListBinding;
import com.aditum.models.updates.models.ActiveIngredient;

import java.util.List;

import lombok.val;


//**************************************************************************
public class IngredientListAdapter
        extends RecyclerView.Adapter<IngredientListAdapter.IngredientListViewHolder>
//*******************************************************************
{

    private List<ActiveIngredient> mIngredientList;
    private Activity mActivity;
    private ListClickListener mListClickListener;


    //*******************************************************************
    @NonNull
    @Override
    public IngredientListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //*******************************************************************
    {
        IngredientListBinding ingredientListBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.ingredient_list,
                parent, false);
        return new IngredientListViewHolder(ingredientListBinding);
    }

    //********************************************************************************************
    public IngredientListAdapter(List<ActiveIngredient> mFilterList, Activity activity)
    //********************************************************************************************
    {
        this.mIngredientList = mFilterList;
        this.mActivity = activity;
        setHasStableIds(true);
    }

    //*********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull IngredientListViewHolder holder, int position)
    //*********************************************************************************************
    {
        final val item = mIngredientList
                .get(position);

        if (TextUtils.isEmpty(item.getIngredientName()))
            return;

        holder.IngredientListBindingViewBinding.name.setText(item.getIngredientName());
        holder.IngredientListBindingViewBinding.strength.setText(item.getStrength());
        holder.IngredientListBindingViewBinding.strengthUnit.setText(item.getStrengthUnit().toString());
        if (item.getDosageUnit() != null) {
            holder.IngredientListBindingViewBinding.dosageUnitView.setVisibility(View.VISIBLE);
            holder.IngredientListBindingViewBinding.dosageUnit.setText(item.getDosageUnit().toString());
        }
    }

    //*******************************************************************
    @Override
    public int getItemCount()
    //*******************************************************************
    {
        return mIngredientList.size();
    }

    //*******************************************************************
    public class IngredientListViewHolder
            extends RecyclerView.ViewHolder
            //*******************************************************************
    {

        IngredientListBinding IngredientListBindingViewBinding;

        //*******************************************************************
        public IngredientListViewHolder(@NonNull IngredientListBinding itemView)
        //*******************************************************************
        {
            super(itemView.getRoot());
            IngredientListBindingViewBinding = itemView;
        }
    }

    //*******************************************************************
    public interface ListClickListener
            //*******************************************************************
    {
        void onUpdate(int position);
    }

}
