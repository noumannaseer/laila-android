package com.fantechlabs.lailaa.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import lombok.val;


//************************************************************************************
public class ProfileViewPagerAdapter extends FragmentPagerAdapter
//************************************************************************************
{

    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private final ArrayList<String> mFragmentTitle = new ArrayList<>();

    //************************************************************************************
    public ProfileViewPagerAdapter(@NonNull FragmentManager fm)
    //************************************************************************************
    {
        super(fm);
    }

    //************************************************************************************
    public ProfileViewPagerAdapter(@NonNull FragmentManager fm, int behavior)
    //************************************************************************************
    {
        super(fm, behavior);
    }

    //**********************************************************
    @NonNull
    @Override
    public Fragment getItem(int position)
    //**********************************************************
    {
        return mFragmentList.get(position);
    }

    //**********************************************************
    @Override
    public int getCount()
    //**********************************************************
    {
        return mFragmentList.size();
    }

    //**********************************************************
    public void setSaveButton()
    //**********************************************************
    {
        val size = mFragmentList.size();

    }

    //**********************************************************
    public void addFragment(Fragment fragment, String title)
    //**********************************************************
    {
        mFragmentList.add(fragment);
        mFragmentTitle.add(title);
    }

    //**********************************************************
    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    //**********************************************************
    {
        return mFragmentTitle.get(position);
    }
}
