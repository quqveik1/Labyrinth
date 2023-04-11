package com.kurlic.labirints.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Arrow.Arrow;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class MainGameFragment extends MyCommonFragment
{
    LabyrinthView labyrinthView;
    Arrow leftArrow;
    Arrow rightArrow;
    Arrow upArrow;
    Arrow downArrow;
    Button changeLevelButton;
    Button showSolutionButton;

    TextView timerTextView;



    public MainGameFragment()
    {
        super("MainGameFragment");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.main_game_fragment, container, false);

        setLabyrinthView(rootView.findViewById(R.id.labyrinthView));
        leftArrow = rootView.findViewById(R.id.leftArrow);
        leftArrow.setLabyrinthView(labyrinthView);
        rightArrow = rootView.findViewById(R.id.rightArrow);
        rightArrow.setLabyrinthView(labyrinthView);
        upArrow = rootView.findViewById(R.id.upArrow);
        upArrow.setLabyrinthView(labyrinthView);
        downArrow = rootView.findViewById(R.id.downArrow);
        downArrow.setLabyrinthView(labyrinthView);

        changeLevelButton = rootView.findViewById(R.id.changeLevelButton);
        changeLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                labyrinthView.endLevel();
            }
        });

        showSolutionButton = rootView.findViewById(R.id.showSolutionButton);

        showSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                labyrinthView.changeSolutionShowStatus();
            }
        });

        timerTextView = rootView.findViewById(R.id.timeTextView);
        labyrinthView.setTimeTextView(timerTextView);




        labyrinthView.startGame(this);

        return rootView;
    }

    public void updateTimerText(String text)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                timerTextView.setText(text);
            }
        });
    }

    public LabyrinthView getLabyrinthView()
    {
        return labyrinthView;
    }

    public void setLabyrinthView(LabyrinthView labyrinthView)
    {
        this.labyrinthView = labyrinthView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        labyrinthView.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        labyrinthView.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

    }
}


