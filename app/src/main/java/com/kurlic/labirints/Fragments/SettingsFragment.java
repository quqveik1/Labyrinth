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
import androidx.appcompat.app.AppCompatActivity;

import com.kurlic.labirints.R;
import com.kurlic.labirints.SettingsData;
import com.kurlic.labirints.SharedData;


public class SettingsFragment extends MyCommonFragment {
    public SettingsFragment(AppCompatActivity activity) {
        super("SettingsFragment", activity);
        setToolBarName(getActivityCompat().getString(R.string.settings));
    }

    Spinner themeSpinner;
    Spinner languageSpinner;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_fragment, container, false);

        themeSpinner = rootView.findViewById(R.id.themeSpinner);

        setDataToSpinnerFromSettings(SharedData.getSettingsData().getTheme(), themeSpinner);

        createOnClickSpinnerAdapter(themeSpinner, new GetSetSpinnerData() {
            @Override
            public String getData() {
                return SharedData.getSettingsData().getTheme();
            }

            @Override
            public void setData(String data) {
                SharedData.getSettingsData().setTheme(data);
            }
        });

        languageSpinner = rootView.findViewById(R.id.languageSpinner);

        setDataToSpinnerFromSettings(SharedData.getSettingsData().getLanguage(), languageSpinner);

        createOnClickSpinnerAdapter(languageSpinner, new GetSetSpinnerData() {
            @Override
            public String getData() {
                return SharedData.getSettingsData().getLanguage();
            }

            @Override
            public void setData(String data) {
                SharedData.getSettingsData().setLanguage(data);
            }
        });

        return rootView;
    }

    interface GetSetSpinnerData {
        String getData();

        void setData(String data);
    }

    void createOnClickSpinnerAdapter(@NonNull Spinner spinner, GetSetSpinnerData data) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();

                String currentTheme = data.getData();

                if (!selectedOption.equals(currentTheme)) {
                    data.setData(selectedOption);
                    Toast.makeText(getContext(), getResources().getString(R.string.onOptionChangedMessage), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void setDataToSpinnerFromSettings(String data, Spinner spinner) {
        try {
            int spinnerPosition = ((ArrayAdapter) spinner.getAdapter()).getPosition(data);
            if (spinnerPosition >= 0) {
                spinner.setSelection(spinnerPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
