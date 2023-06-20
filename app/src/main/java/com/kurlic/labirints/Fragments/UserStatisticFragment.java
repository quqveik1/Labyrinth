package com.kurlic.labirints.Fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.kurlic.labirints.MainActivity;
import com.kurlic.labirints.R;
import com.kurlic.labirints.SharedData;
import com.kurlic.labirints.view.Labyrinth.LabyrinthUserData;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UserStatisticFragment extends MyCommonFragment
{
    LabyrinthUserData labyrinthUserData;
    EditText userNameTextEdit;
    TextView userCFinishedLevelsTextView;
    TextView userMinTimeTextView;

    public UserStatisticFragment(AppCompatActivity activity)
    {
        super("UserStatisticFragment", activity);
        setToolBarName(getActivityCompat().getString(R.string.myStatistic));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.user_statistic_fragment, container, false);
        userNameTextEdit = rootView.findViewById(R.id.userNameEditText);
        labyrinthUserData = SharedData.getLabyrinthUserData();
        if(labyrinthUserData != null)
        {
            userNameTextEdit.setText(labyrinthUserData.getUserName());
        }
        userNameTextEdit.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                labyrinthUserData.setUserName(s.toString());
            }
        });

        userCFinishedLevelsTextView = rootView.findViewById(R.id.userCFinishedLevelsTextView);


        userMinTimeTextView = rootView.findViewById(R.id.userMinTimeTextView);

        displayUserData();
        setupUI(rootView);

        return rootView;
    }

    private void setupUI(@NonNull View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v);
                return false;
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            userNameTextEdit.clearFocus();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden)
    {
        super.onHiddenChanged(hidden);
        displayUserData();
    }

    void displayUserData()
    {
        userCFinishedLevelsTextView.setText(Integer.toString(labyrinthUserData.getCLabyrinthFinished()));

        long time = labyrinthUserData.getMinTime();
        String date = SharedData.timeInMSToString(time);
        userMinTimeTextView.setText(date);
    }
    @Override
    public void onNavigationItemClicked()
    {
        hideKeyboard(getView());
    }
}
