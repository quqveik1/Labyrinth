package com.kurlic.labirints.Fragments;

import androidx.fragment.app.Fragment;

public abstract class MyCommonFragment extends Fragment
{
    public String uniqueTag;
    MyCommonFragment(String uniqueTag)
    {
        this.uniqueTag = uniqueTag;
    }
    public void onNavigationItemClicked(){};

    public void onNavigationItemComeBack(){};
}
