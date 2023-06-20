package com.kurlic.labirints.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
    ImageButton changeLevelButton;
    ImageButton showSolutionButton;
    TextView timerTextView;



    public MainGameFragment(AppCompatActivity activity)
    {
        super("MainGameFragment", activity);
        setToolBarName(getActivityCompat().getString(R.string.app_name));
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

        changeLevelButton = rootView.findViewById(R.id.changeLevelButton);
        changeLevelButton.setOnClickListener(v -> labyrinthView.endLevel());

        showSolutionButton = rootView.findViewById(R.id.showSolutionButton);

        showSolutionButton.setOnClickListener(v ->
        {
            labyrinthView.changeSolutionShowStatus();
            if(labyrinthView.getSolutionShowStatus())
            {
                showSolutionButton.setImageResource(R.drawable.lampfilled);
            }
            else
            {
                showSolutionButton.setImageResource(R.drawable.lampstroked);
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

    @Override
    public void onNavigationItemClicked()
    {
        super.onNavigationItemClicked();
        labyrinthView.onPause();
    }

    @Override
    public void onNavigationItemComeBack()
    {
        super.onNavigationItemComeBack();
        labyrinthView.onResume();
    }
}


