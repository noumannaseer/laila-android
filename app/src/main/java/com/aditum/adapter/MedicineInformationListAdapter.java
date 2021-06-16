package com.aditum.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aditum.R;
import com.aditum.databinding.MedicineDetailInfoListBinding;
import com.aditum.models.updates.models.MedicationInformation;

import java.util.List;

import lombok.val;


//**************************************************************************
public class MedicineInformationListAdapter
        extends RecyclerView.Adapter<MedicineInformationListAdapter.MedicineDetailInfoListViewHolder>
//*******************************************************************
{

    private List<MedicationInformation> mMedicineList;
    private Activity mActivity;
    private ListClickListener mListClickListener;


    //*******************************************************************
    @NonNull
    @Override
    public MedicineDetailInfoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //*******************************************************************
    {
        MedicineDetailInfoListBinding medicineDetailInfoListBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.medicine_detail_info_list,
                parent, false);
        return new MedicineDetailInfoListViewHolder(medicineDetailInfoListBinding);
    }

    //********************************************************************************************
    public MedicineInformationListAdapter(List<MedicationInformation> mFilterList, Activity activity)
    //********************************************************************************************
    {
        this.mMedicineList = mFilterList;
        this.mActivity = activity;
        setHasStableIds(true);
    }

    //*********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull MedicineDetailInfoListViewHolder holder, int position)
    //*********************************************************************************************
    {
        val item = mMedicineList
                .get(position);

        if (TextUtils.isEmpty(item.getBrandName()))
            return;
        holder.mBinding.companyName.setText(item.getCompanyName());
        holder.mBinding.className.setText(item.getClassName());
        holder.mBinding.noOfAis.setText(item.getNumberOfAis().toString());
        holder.mBinding.drugCode.setText(item.getDrugCode().toString());
        holder.mBinding.brandName.setText(item.getBrandName());
        holder.mBinding.aiGrpNo.setText(item.getAiGroupNo().toString());
        holder.mBinding.drugIdentificationNo.setText(item.getDrugIdentificationNumber());
        holder.mBinding.lastUpdateDate.setText(item.getLastUpdateDate().toString());


        if (TextUtils.isEmpty(item.getDescriptor()))
        {
            holder.mBinding.descriptorNo.setText(R.string.n_a);
            return;
        }
        holder.mBinding.descriptorNo.setText(item.getDescriptor());

    }

    //*******************************************************************
    @Override
    public int getItemCount()
    //*******************************************************************
    {
        return mMedicineList.size();
    }

    //*******************************************************************
    public class MedicineDetailInfoListViewHolder
            extends RecyclerView.ViewHolder
            //*******************************************************************
    {
        MedicineDetailInfoListBinding mBinding;

        //*******************************************************************
        public MedicineDetailInfoListViewHolder(@NonNull MedicineDetailInfoListBinding itemView)
        //*******************************************************************
        {
            super(itemView.getRoot());
            mBinding = itemView;
        }
    }

    //*******************************************************************
    public interface ListClickListener
            //*******************************************************************
    {
        void onUpdate(int position);
    }

}
