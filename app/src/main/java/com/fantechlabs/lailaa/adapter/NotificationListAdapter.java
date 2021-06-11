package com.fantechlabs.lailaa.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.databinding.NotificationListBinding;
import com.fantechlabs.lailaa.models.updates.models.ResponseEvent;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.fantechlabs.lailaa.utils.DateUtils;

import java.util.List;

import lombok.val;


//**************************************************************************
public class NotificationListAdapter
        extends RecyclerView.Adapter<NotificationListAdapter.NotificationListViewHolder>
//*******************************************************************
{
    private List<ResponseEvent> mEventsList;
    private Activity mActivity;

    //*******************************************************************
    @NonNull
    @Override
    public NotificationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //*******************************************************************
    {
        NotificationListBinding mNotificationListBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.notification_list,
                parent, false);
        return new NotificationListViewHolder(mNotificationListBinding);
    }


    //********************************************************************************************
    public NotificationListAdapter(List<ResponseEvent> mNotificationList, Activity activity)
    //********************************************************************************************
    {
        this.mEventsList = mNotificationList;
        this.mActivity = activity;
        setHasStableIds(true);
    }

    //*********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull NotificationListViewHolder holder, int position)
    //*********************************************************************************************
    {
        final val item = mEventsList
                .get(position);

        if (TextUtils.isEmpty(item.getEventTitle()))
            return;
        val refillDate = DateUtils.getDateFromTimeStamp(item.getEndDate(), "dd-MMM-yyyy");
        holder.NotificationViewBinding.name.setText(item.getEventTitle());
        holder.NotificationViewBinding.amount.setText(AndroidUtil.getString(R.string.amount) + " " + item.getFrequency());
        holder.NotificationViewBinding.refillDate.setText(AndroidUtil.getString(R.string.refill_date) + ": " + refillDate);

    }

    //*******************************************************************
    @Override
    public int getItemCount()
    //*******************************************************************
    {
        return mEventsList.size();
    }

    //*******************************************************************
    public class NotificationListViewHolder
            extends RecyclerView.ViewHolder
            //*******************************************************************
    {

        NotificationListBinding NotificationViewBinding;

        //*******************************************************************
        public NotificationListViewHolder(@NonNull NotificationListBinding itemView)
        //*******************************************************************
        {
            super(itemView.getRoot());
            NotificationViewBinding = itemView;
        }
    }


}
