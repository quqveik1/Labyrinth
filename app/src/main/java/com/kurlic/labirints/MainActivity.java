package com.kurlic.labirints;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;

import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class MainActivity extends AppCompatActivity {

    LabyrinthView labyrinthView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        labyrinthView = findViewById(R.id.labyrinthView);
    }
}