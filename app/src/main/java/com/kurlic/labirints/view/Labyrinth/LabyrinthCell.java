package com.kurlic.labirints.view.Labyrinth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;

public abstract class LabyrinthCell {

    LabyrinthView labyrinthView;

    LabyrinthCell(LabyrinthView labyrinthView)
    {
        this.labyrinthView = labyrinthView;
    }
    abstract public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect);

    abstract public void onCharacterMove(@NonNull Character character);

    abstract public boolean canEnter(@NonNull Character character);

    abstract public void onEnter(@NonNull Character character);


}
