package com.kurlic.labirints.view.Labyrinth;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;

public class TeleportCell extends  LabyrinthCell{
    Point teleportDest;


    TeleportCell(LabyrinthView labyrinthView, Point teleportDest) {
        super(labyrinthView);
        setTeleportDest(teleportDest);
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect) {
        paint.setColor(labyrinthView.getResources().getColor(R.color.teleport));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, paint);
    }

    @Override
    public void onCharacterMove(@NonNull Character character, @NonNull Point moveDelta) {
        //Toast.makeText(labyrinthView.getContext(), "onCharacterMove|moveDelta: " + moveDelta.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean canEnter(@NonNull Character character) {
        return true;
    }

    @Override
    public void onEnter(@NonNull Character character) {
        character.setCoordinates(teleportDest);
    }


    public Point getTeleportDest() {
        return teleportDest;
    }

    public void setTeleportDest(Point teleport) {
        this.teleportDest = teleport;
    }
}
