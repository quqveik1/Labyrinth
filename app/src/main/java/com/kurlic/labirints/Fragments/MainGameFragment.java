package com.kurlic.labirints.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kurlic.labirints.R;
import com.kurlic.labirints.SharedData;
import com.kurlic.labirints.view.Arrow.Arrow;
import com.kurlic.labirints.view.Labyrinth.LabyrinthApi;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainGameFragment extends MyCommonFragment {
    LabyrinthView labyrinthView;
    ImageButton changeLevelButton;
    ImageButton showSolutionButton;
    TextView timerTextView;

    public MainGameFragment(AppCompatActivity activity) {
        super("MainGameFragment", activity);
        setToolBarName(getActivityCompat().getString(R.string.app_name));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_game_fragment, container, false);

        setLabyrinthView(rootView.findViewById(R.id.labyrinthView));

        changeLevelButton = rootView.findViewById(R.id.changeLevelButton);
        changeLevelButton.setOnClickListener(v -> labyrinthView.endLevel());

        showSolutionButton = rootView.findViewById(R.id.showSolutionButton);

        showSolutionButton.setOnClickListener(v ->
        {
            labyrinthView.changeSolutionShowStatus();
            if (labyrinthView.getSolutionShowStatus()) {
                showSolutionButton.setImageResource(R.drawable.lampfilled);
            } else {
                showSolutionButton.setImageResource(R.drawable.lampstroked);
            }
        });

        timerTextView = rootView.findViewById(R.id.timeTextView);
        labyrinthView.setTimeTextView(timerTextView);

        labyrinthView.startGame(this);

        return rootView;
    }


    public void updateTimerText(String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timerTextView.setText(text);
            }
        });
    }

    public LabyrinthView getLabyrinthView() {
        return labyrinthView;
    }

    public void setLabyrinthView(@NonNull LabyrinthView labyrinthView) {
        this.labyrinthView = labyrinthView;
        labyrinthView.setLabyrinthApi(new LabyrinthApiMain());
    }

    class LabyrinthApiMain implements LabyrinthApi {
        @Override
        public void onGameFinished(long time) {
            String timeStr = SharedData.timeInMSToString(time);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Поздравляем!")
                    .setMessage("Вы успешно прошли уровень за " + timeStr + " секунд!")
                    .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        labyrinthView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        labyrinthView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onNavigationItemClicked() {
        super.onNavigationItemClicked();
        labyrinthView.onPause();
    }

    @Override
    public void onNavigationItemComeBack() {
        super.onNavigationItemComeBack();
        labyrinthView.onResume();
    }
}


