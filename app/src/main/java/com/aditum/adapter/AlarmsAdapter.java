package com.aditum.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aditum.R;
import com.aditum.models.updates.models.Medication;
import com.aditum.models.updates.models.ResponseEvent;
import com.aditum.utils.AndroidUtil;
import com.aditum.utils.DateUtils;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import lombok.val;


//**************************************************************************
public final class AlarmsAdapter
        extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder>
//**************************************************************************
{

    private List<ResponseEvent> mEvents;
    private String[] mDays;
    private int mAccentColor = -1;
    private ListClickListener mListClickListener;
    private List<Medication> mMedicineList;
    private Activity mActivity;


    //**************************************************************************
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    //**************************************************************************
    {
        final Context c = parent.getContext();
        final View v = LayoutInflater.from(c)
                .inflate(R.layout.alarm_row, parent, false);
        return new ViewHolder(v);
    }

    //**************************************************************************
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    //**************************************************************************
    {
        final Context c = holder.itemView.getContext();
        if (mAccentColor == -1) {
            mAccentColor = ContextCompat.getColor(c, R.color.darkBlue);
        }
        if (mDays == null) {
            mDays = c.getResources()
                    .getStringArray(R.array.days_abbreviated);
        }
        final ResponseEvent event = mEvents.get(position);
//        val startDate = event.getStartDate().split(" ")[0];
//        val endDate = event.getEndDate().split(" ")[0];

        val startDate = DateUtils.getDateFromTimeStamp(event.getStartDate(), "dd-MMM-yyyy");
        val endDate = DateUtils.getDateFromTimeStamp(event.getEndDate(), "dd-MMM-yyyy");
        DateUtils.getGivenDate(event.getStartDate());
        holder.dateTime.setText(startDate + " - " + endDate);
        holder.label.setText(event.getEventTitle());
        holder.intakeTime.setText(event.getTimeSchedule());
        holder.delete.setOnClickListener(view -> {
            mListClickListener.onDelete(event.getId());
        });
        holder.refillReminder.setOnClickListener(v ->
                AndroidUtil.displayAlertDialog(AndroidUtil.getString(R.string.pharmacy_notified_about_refill), AndroidUtil.getString(R.string.alert), mActivity)
        );
        holder.refillReminder.setVisibility(View.GONE);

        if (mMedicineList == null || mMedicineList.size() == 0)
            return;
        for (val id : mMedicineList) {
            if (Integer.parseInt(event.getMedicationId()) == id.getId()) {
                holder.refillReminder.setVisibility(View.VISIBLE);
                return;
            }
            holder.refillReminder.setVisibility(View.GONE);
        }

    }

    //**************************************************************************
    @Override
    public int getItemCount()
    //**************************************************************************
    {
        return (mEvents == null) ? 0 : mEvents.size();
    }

    //**************************************************************************
    public void setAlarms(List<ResponseEvent> events, List<Medication> medicineList, ListClickListener listClickListener, Activity activity)
    //**************************************************************************
    {
        this.mEvents = events;
        this.mListClickListener = listClickListener;
        this.mMedicineList = medicineList;
        this.mActivity = activity;
        notifyDataSetChanged();
    }

    //**************************************************************************
    static final class ViewHolder
            extends RecyclerView.ViewHolder
            //**************************************************************************
    {

        final TextView dateTime, label, intakeTime;
        final ImageView delete;
        final MaterialButton refillReminder;

        ViewHolder(View itemView) {
            super(itemView);

            dateTime = itemView.findViewById(R.id.date_time);
            label = itemView.findViewById(R.id.ar_label);
            delete = itemView.findViewById(R.id.delete_event);
            intakeTime = itemView.findViewById(R.id.intake_time);
            refillReminder = itemView.findViewById(R.id.refill);
        }
    }

    //*******************************************************************
    public interface ListClickListener
            //*******************************************************************
    {
        void onDelete(int id);
    }

}
