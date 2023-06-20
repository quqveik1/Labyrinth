package com.kurlic.labirints.Fragments;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MyCommonFragment extends Fragment
{
    public String uniqueTag;
    protected String ToolBarName;

    protected AppCompatActivity activityCompat;
    MyCommonFragment(String uniqueTag, AppCompatActivity activityCompat)
    {
        this.uniqueTag = uniqueTag;
        setActivityCompat(activityCompat);
    }
    public void onNavigationItemClicked()
    {

    };

    public void onNavigationItemComeBack()
    {
    };

    public void onEnter()
    {
        updateToolBar();
    }


    @Override
    public void onResume()
    {
        super.onResume();
    }

    void updateToolBar()
    {
        if(getActivityCompat() != null)
        {
            if (getActivityCompat().getSupportActionBar() != null)
            {
                getActivityCompat().getSupportActionBar().setTitle(ToolBarName);
            }
        }
    }

    public AppCompatActivity getActivityCompat()
    {
        return activityCompat;
    }

    public void setActivityCompat(AppCompatActivity activityCompat)
    {
        this.activityCompat = activityCompat;
    }

    public String getToolBarName()
    {
        return ToolBarName;
    }

    public void setToolBarName(String toolBarName)
    {
        ToolBarName = toolBarName;
    }
}
