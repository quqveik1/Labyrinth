package com.kurlic.labirints.view.Labyrinth.Cells;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.Character;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public abstract class LabyrinthCell {
    LabyrinthView labyrinthView;
    Point labyrinthPosition;


    LabyrinthCell(@NonNull LabyrinthView labyrinthView, int x, int y)
    {
        setLabyrinthView(labyrinthView);
        setLabyrinthPosition(new Point(x, y));
    }

    LabyrinthCell(@NonNull LabyrinthView labyrinthView, @NonNull Point coordinates)
    {
        setLabyrinthView(labyrinthView);
        setLabyrinthPosition(coordinates);
    }

    public LabyrinthView getLabyrinthView() {
        return labyrinthView;
    }

    public void setLabyrinthView(LabyrinthView labyrinthView) {
        this.labyrinthView = labyrinthView;
    }

    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect)
    {
        try
        {
            String text = "" + getLabyrinthPosition().x + "|" + getLabyrinthPosition().y;
            paint.setTextSize(50);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(labyrinthView.getResources().getColor(R.color.debugColor));
            int midX = (rect.left + rect.right) / 2;
            int midY = (rect.top + rect.bottom) / 2;
            canvas.drawText(text, midX, midY, paint);
        }
        catch (Exception e)
        {
            //Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    };


    public Point getLabyrinthPosition()
    {
        //labyrinthPosition = getLabyrinthView().getCellPosition(this);
        return labyrinthPosition;
    }

    public void setLabyrinthPosition(Point labyrinthPosition)
    {
        this.labyrinthPosition = labyrinthPosition;
    }

    public void onCharacterMove(@NonNull Character character, @NonNull Point moveDelta) {};

    public boolean canEnter(@NonNull Character character) {return true;};

    public void onEnter(@NonNull Character character) {};

    public void onCellSize(int newSize) {};


}
