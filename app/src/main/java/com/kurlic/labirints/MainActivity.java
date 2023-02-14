package com.kurlic.labirints;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;

import com.kurlic.labirints.view.Arrow.Arrow;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class MainActivity extends AppCompatActivity {

    LabyrinthView labyrinthView;
    Arrow leftArrow;
    Arrow rightArrow;
    Arrow upArrow;
    Arrow downArrow;

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
    }
}