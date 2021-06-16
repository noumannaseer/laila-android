package com.aditum.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aditum.R;
import com.aditum.databinding.MedicationListViewBinding;
import com.aditum.models.updates.models.Medication;

import java.util.ArrayList;

import lombok.val;


//**************************************************************************
public class MedicationListAdapter
        extends RecyclerView.Adapter<MedicationListAdapter.MedicationListViewHolder>
//*******************************************************************
{

    private ArrayList<Medication> mMedicationList;
    private Activity mActivity;
    private int mPosition;
    private ListClickListener mListClickListener;


    //*******************************************************************
    @NonNull
    @Override
    public MedicationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //*******************************************************************
    {
        MedicationListViewBinding MedicationListViewBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.medication_list_view,
                parent, false);
        return new MedicationListViewHolder(MedicationListViewBinding);
    }

    //********************************************************************************************
    public MedicationListAdapter(ArrayList<Medication> mFilterList, ListClickListener listClickListener, Activity activity)
    //********************************************************************************************
    {
        this.mMedicationList = mFilterList;
        this.mActivity = activity;
        this.mListClickListener = listClickListener;
        setHasStableIds(true);
    }

    //*********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull MedicationListViewHolder holder, int position)
    //*********************************************************************************************
    {
        final val item = mMedicationList
                .get(position);

        if (TextUtils.isEmpty(item.getMedicationName()))
            return;
        holder.MedicationViewBinding.medicationName.setText(item.getMedicationName());
        holder.MedicationViewBinding.medicationStrength.setText(item.getStrength() + item.getStrengthUom());
        holder.MedicationViewBinding.medicationDosage.setText("" + item.getDosage());

        val medicineName = item.getMedecineForm();

        switch (medicineName) {
            case "Tablet":
                holder.MedicationViewBinding.medicineImage.setImageResource(R.drawable.tab);
                break;
            case "Teaspoon":
            case "teaspoon":
                holder.MedicationViewBinding.medicineImage.setImageResource(R.drawable.tspoon3x);
                break;
            case "Capsule":
                holder.MedicationViewBinding.medicineImage.setImageResource(R.drawable.cap);
                break;
            case "InHaler":
            case "Inhaler":
                holder.MedicationViewBinding.medicineImage.setImageResource(R.drawable.inhaler3x);
                break;
            case "Drops":
                holder.MedicationViewBinding.medicineImage.setImageResource(R.drawable.drops);
                break;
            case "Injection":
                holder.MedicationViewBinding.medicineImage.setImageResource(R.drawable.inj);
                break;
        }

        holder.MedicationViewBinding.optionMenu.setOnClickListener(v ->
        {
            onMenuClicked(holder, item, position);
        });
    }

    //*****************************************************************
    private void onMenuClicked(MedicationListViewHolder holder, Medication item, int position)
    //*****************************************************************
    {
        mPosition = position;
        PopupMenu popup = new PopupMenu(mActivity, holder.MedicationViewBinding.optionMenu);
        popup.getMenuInflater()
                .inflate(R.menu.medication_menu, popup.getMenu());

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
                case "Info":
                    if (mListClickListener != null)
                        mListClickListener.onInformation(item.getDinRxNumber().toString());
                    break;
                case "Check Interactions":
                    if (mListClickListener != null)
                        mListClickListener.viewInteractions(item.getId());
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
        return mMedicationList.size();
    }

    //*******************************************************************
    public class MedicationListViewHolder
            extends RecyclerView.ViewHolder
            //*******************************************************************
    {

        MedicationListViewBinding MedicationViewBinding;

        //*******************************************************************
        public MedicationListViewHolder(@NonNull MedicationListViewBinding itemView)
        //*******************************************************************
        {
            super(itemView.getRoot());
            MedicationViewBinding = itemView;
        }
    }

    //*******************************************************************
    public interface ListClickListener
            //*******************************************************************
    {
        void onDelete(int position, int id);

        void onUpdate(int position);

        void onInformation(String rxDinNumber);

        void viewInteractions(int id);

    }

}
