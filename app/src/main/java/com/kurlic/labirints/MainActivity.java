package com.kurlic.labirints;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.kurlic.labirints.view.Arrow.Arrow;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class MainActivity extends AppCompatActivity {

    LabyrinthView labyrinthView;
    Arrow leftArrow;
    Arrow rightArrow;
    Arrow upArrow;
    Arrow downArrow;
    Button changeLevelButton;
    Button showSolutionButton;

    TextView timerTextView;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        labyrinthView = findViewById(R.id.labyrinthView);
        leftArrow = findViewById(R.id.leftArrow);
        leftArrow.setLabyrinthView(labyrinthView);
        rightArrow = findViewById(R.id.rightArrow);
        rightArrow.setLabyrinthView(labyrinthView);
        upArrow = findViewById(R.id.upArrow);
        upArrow.setLabyrinthView(labyrinthView);
        downArrow = findViewById(R.id.downArrow);
        downArrow.setLabyrinthView(labyrinthView);

        changeLevelButton = findViewById(R.id.changeLevelButton);
        changeLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                labyrinthView.endLevel();
            }
        });

        showSolutionButton = findViewById(R.id.showSolutionButton);

        showSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                labyrinthView.changeSolutionShowStatus();
            }
        });

        timerTextView = findViewById(R.id.timeTextView);
        labyrinthView.setTimeTextView(timerTextView);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.leftMenu);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        labyrinthView.startGame(this);

    }

    public void updateTimerText(String text)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                timerTextView.setText(text);
            }
        });
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        labyrinthView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        labyrinthView.onResume();
    }
}