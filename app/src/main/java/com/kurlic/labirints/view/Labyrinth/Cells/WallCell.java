package com.kurlic.labirints.view.Labyrinth.Cells;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.Character;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class WallCell extends LabyrinthCell
{



    public WallCell(LabyrinthView labyrinthView, int x, int y) {
        super(labyrinthView, x, y);
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect) {

        paint.setColor(labyrinthView.getResources().getColor(R.color.wall));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
        super.draw(canvas, paint, rect);
    }


    @Override
    public void onCharacterMove(@NonNull Character character, @NonNull Point moveDelta) {

    }

    @Override
    public boolean canEnter(@NonNull Character character) {
        return false;
    }

    @Override
    public void onEnter(@NonNull Character character) {

    }
}
