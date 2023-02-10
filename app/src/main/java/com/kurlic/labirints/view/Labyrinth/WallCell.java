package com.kurlic.labirints.view.Labyrinth;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;

public class WallCell extends LabyrinthCell{



    WallCell(LabyrinthView labyrinthView) {
        super(labyrinthView);
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect) {
        paint.setColor(labyrinthView.getResources().getColor(R.color.wall));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
    }


    @Override
    public void onCharacterMove(@NonNull Character character) {

    }

    @Override
    public boolean canEnter(@NonNull Character character) {
        return false;
    }

    @Override
    public void onEnter(@NonNull Character character) {

    }
}
