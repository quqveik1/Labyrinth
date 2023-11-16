package com.kurlic.labirints;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    Button okButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        okButton = findViewById(R.id.okButton);

        okButton.setOnClickListener(v ->
        {
            finish();
            SharedData.getSettingsData().setFirstLaunch(true);
        });
    }
}
