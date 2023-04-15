package com.kurlic.labirints.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kurlic.labirints.R;

public class HowToPlayFragment extends MyCommonFragment
{
    public HowToPlayFragment()
    {
        super("HowToPlayFragment");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.how_to_play_fragment, container, false);
        return rootView;
    }
}
