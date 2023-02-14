package com.kurlic.labirints.view.Labyrinth;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;

public class EmptyCell extends LabyrinthCell{
    EmptyCell(LabyrinthView labyrinthView) {
        super(labyrinthView);
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect) {

    }

    @Override
    public void onCharacterMove(Character character, @NonNull Point moveDelta) {

    }

    @Override
    public boolean canEnter(Character character) {
        return true;
    }

    @Override
    public void onEnter(Character character) {

    }
}
