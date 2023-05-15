package com.kurlic.labirints.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kurlic.labirints.R;
import com.kurlic.labirints.SettingsData;

public class SettingsFragment extends MyCommonFragment
{
    public SettingsFragment()
    {
        super("SettingsFragment");
    }

    Spinner themeSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);

        themeSpinner = rootView.findViewById(R.id.themeSpinner);

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedOption = parent.getItemAtPosition(position).toString();
                String lightTheme = getResources().getString(R.string.optionLightTheme);
                String nightTheme = getResources().getString(R.string.optionNightTheme);
                String systemTheme = getResources().getString(R.string.optionSystemTheme);

                boolean lightRes = selectedOption.equals(lightTheme);
                boolean nightRes = selectedOption.equals(nightTheme);
                boolean systemRes = selectedOption.equals(systemTheme);

                if(lightRes)
                {
                    if(!SettingsData.getTheme().equals(lightTheme))
                    {
                        getActivity().getApplicationContext().setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light);
                        getActivity().recreate();
                        SettingsData.setTheme(lightTheme);
                    }

                }
                if(nightRes)
                {
                    int g = 3;
                }
                if(systemRes)
                {
                    int x = 3;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }
}
