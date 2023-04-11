package com.kurlic.labirints.Fragments;

import androidx.fragment.app.Fragment;

abstract class MyCommonFragment extends Fragment
{
    public String uniqueTag;
    MyCommonFragment(String uniqueTag)
    {
        this.uniqueTag = uniqueTag;
    }
}
