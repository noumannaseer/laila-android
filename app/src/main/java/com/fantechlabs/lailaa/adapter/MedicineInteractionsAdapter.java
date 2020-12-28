package com.fantechlabs.lailaa.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.MedicineInteractionLayoutBinding;
import com.fantechlabs.lailaa.models.InteractionMsg;
import com.fantechlabs.lailaa.utils.AndroidUtil;

import java.util.List;

import lombok.val;


//**************************************************************************
public class MedicineInteractionsAdapter
        extends RecyclerView.Adapter<MedicineInteractionsAdapter.MedicineInteractionsViewHolder>
//*******************************************************************
{

    private List<InteractionMsg> mInteractionList;
    private Activity mActivity;
    private ListClickListener mListClickListener;


    //*******************************************************************
    @NonNull
    @Override
    public MedicineInteractionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //*******************************************************************
    {
        MedicineInteractionLayoutBinding medicineInteractionLayoutBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.medicine_interaction_layout,
                parent, false);
        return new MedicineInteractionsViewHolder(medicineInteractionLayoutBinding);
    }

    //********************************************************************************************
    public MedicineInteractionsAdapter(List<InteractionMsg> mFilterList, Activity activity)
    //********************************************************************************************
    {
        this.mInteractionList = mFilterList;
        this.mActivity = activity;
        setHasStableIds(true);
    }

    //*********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull MedicineInteractionsViewHolder holder, int position)
    //*********************************************************************************************
    {
        final val item = mInteractionList
                .get(position);

        holder.MedicineInteractionLayoutBinding.severity.setText(item.getSeverity());
        holder.MedicineInteractionLayoutBinding.medicineName.setText(AndroidUtil.getString(R.string.interaction_medicine_name, item.getSubject(), item.getObject()));
        holder.MedicineInteractionLayoutBinding.reason.setText(item.getText());

        switch (item.getSeverityId()) {
            case 1:
                holder.MedicineInteractionLayoutBinding.severityColor.setBackgroundColor(Color.parseColor("#7aab9d"));
                break;
            case 2:
                holder.MedicineInteractionLayoutBinding.severityColor.setBackgroundColor(Color.parseColor("#e5f545"));
                break;
            default:
                holder.MedicineInteractionLayoutBinding.severityColor.setBackgroundColor(Color.parseColor("#fa8300"));
                break;
        }

    }

    //*******************************************************************
    @Override
    public int getItemCount()
    //*******************************************************************
    {
        return mInteractionList.size();
    }

    //*******************************************************************
    public class MedicineInteractionsViewHolder
            extends RecyclerView.ViewHolder
            //*******************************************************************
    {

        MedicineInteractionLayoutBinding MedicineInteractionLayoutBinding;

        //*******************************************************************
        public MedicineInteractionsViewHolder(@NonNull MedicineInteractionLayoutBinding itemView)
        //*******************************************************************
        {
            super(itemView.getRoot());
            MedicineInteractionLayoutBinding = itemView;
        }
    }

    //*******************************************************************
    public interface ListClickListener
            //*******************************************************************
    {
        void onUpdate(int position);
    }

}
