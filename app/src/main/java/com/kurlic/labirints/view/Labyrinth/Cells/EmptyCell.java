package com.kurlic.labirints.view.Labyrinth.Cells;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.kurlic.labirints.view.Labyrinth.Character;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class EmptyCell extends LabyrinthCell
{
    public EmptyCell(LabyrinthView labyrinthView, int x, int y) {
        super(labyrinthView, x, y);
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect) {
        super.draw(canvas, paint, rect);

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
