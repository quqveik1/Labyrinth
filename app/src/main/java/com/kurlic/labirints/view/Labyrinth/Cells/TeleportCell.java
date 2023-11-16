package com.kurlic.labirints.view.Labyrinth.Cells;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.Character.Character;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class TeleportCell extends LabyrinthCell {
    Point teleportDest;

    public TeleportCell(LabyrinthView labyrinthView, int x, int y, Point teleportDest) {
        super(labyrinthView, x, y);
        setTeleportDest(teleportDest);
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect) {
        paint.setColor(labyrinthView.getResources().getColor(R.color.teleport));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
        super.draw(canvas, paint, rect);
    }

    @Override
    public void onCharacterMove(@NonNull Character character, @NonNull Point moveDelta) {
    }

    @Override
    public boolean canEnter(@NonNull Character character) {
        return true;
    }

    @Override
    public boolean onEnter(@NonNull Character character) {
        character.setCoordinates(teleportDest);
        return true;
    }

    public Point getTeleportDest() {
        return teleportDest;
    }

    public void setTeleportDest(Point teleport) {
        this.teleportDest = teleport;
    }
}
