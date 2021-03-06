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
import com.aditum.databinding.ContactTypeListBinding;
import com.aditum.models.updates.models.Contact;

import java.util.List;

import lombok.val;

//**************************************************************************
public class CareTakerListAdapter
        extends RecyclerView.Adapter<CareTakerListAdapter.ContactListViewHolder>
//*******************************************************************
{

    private List<Contact> mCareTakerList;
    private Activity mActivity;
    private ListClickListener mListClickListener;
    private int mPosition;


    //*******************************************************************
    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    //*******************************************************************
    {
        ContactTypeListBinding contactTypeListBinding = DataBindingUtil.inflate(LayoutInflater
                        .from(parent.getContext()),
                R.layout.contact_type_list,
                parent, false);
        return new ContactListViewHolder(contactTypeListBinding);
    }

    //********************************************************************************************
    public CareTakerListAdapter(List<Contact> mFilterList, CareTakerListAdapter.ListClickListener listClickListener, Activity activity)
    //********************************************************************************************
    {
        this.mCareTakerList = mFilterList;
        this.mActivity = activity;
        this.mListClickListener = listClickListener;
        setHasStableIds(true);
    }

    //*********************************************************************************************
    @Override
    public void onBindViewHolder(@NonNull ContactListViewHolder holder, int position)
    //*********************************************************************************************
    {
        final val item = mCareTakerList
                .get(position);

        if (TextUtils.isEmpty(item.getFirstName()))
            return;

        holder.contactTypeListBinding.name.setText(item.getFirstName());
        holder.contactTypeListBinding.phone.setText("Phone : " + item.getPhone());
        holder.contactTypeListBinding.optionMenu.setOnClickListener(v ->
        {
            onMenuClicked(holder, item, position);
        });

    }

    //*****************************************************************
    private void onMenuClicked(ContactListViewHolder holder, Contact item, int position)
    //*****************************************************************
    {
        mPosition = position;
        PopupMenu popup = new PopupMenu(mActivity, holder.contactTypeListBinding.optionMenu);
        popup.getMenuInflater()
                .inflate(R.menu.contact_type_menu, popup.getMenu());

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
        return mCareTakerList.size();
    }

    //*******************************************************************
    public class ContactListViewHolder
            extends RecyclerView.ViewHolder
            //*******************************************************************
    {

        ContactTypeListBinding contactTypeListBinding;

        //*******************************************************************
        public ContactListViewHolder(@NonNull ContactTypeListBinding itemView)
        //*******************************************************************
        {
            super(itemView.getRoot());
            contactTypeListBinding = itemView;
        }
    }

    //*******************************************************************
    public interface ListClickListener
            //*******************************************************************
    {
        void onUpdate(int position);

        void onDelete(int position, int id);
    }

}
