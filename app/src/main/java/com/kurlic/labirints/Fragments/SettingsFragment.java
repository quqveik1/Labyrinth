package com.kurlic.labirints.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kurlic.labirints.R;
import com.kurlic.labirints.SettingsData;
import com.kurlic.labirints.SharedData;


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
        try
        {
            String theme = SharedData.getSettingsData().getTheme();
            int spinnerPosition = ((ArrayAdapter) themeSpinner.getAdapter()).getPosition(theme);
            if(spinnerPosition >= 0)
            {
                themeSpinner.setSelection(spinnerPosition);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedOption = parent.getItemAtPosition(position).toString();

                String currentTheme = SharedData.getSettingsData().getTheme();

                if(!selectedOption.equals(currentTheme))
                {
                    SharedData.getSettingsData().setTheme(selectedOption);
                    Toast.makeText(getContext(), getResources().getString(R.string.onOptionChangedMessage), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        return rootView;
    }
}
