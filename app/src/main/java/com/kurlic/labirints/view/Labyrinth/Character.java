package com.kurlic.labirints.view.Labyrinth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;

public class Character {


    Point coordinates;
    Bitmap bm;
    Bitmap bmSized;
    LabyrinthView labyrinthView;
    double cellSize;

    public Character(LabyrinthView labyrinthView) {
        super();
        this.labyrinthView = labyrinthView;
        coordinates = new Point();
    }

    private void setX(int x)
    {
        coordinates.x = x;
        labyrinthView.invalidate();
    }

    private void setY(int y)
    {
        coordinates.y = y;
        labyrinthView.invalidate();
    }

    public void setCoordinates(Point newCoordinates) {
        if(checkOnlyOneDirection(newCoordinates) == true)
        {

            if (this.coordinates != newCoordinates) {
                labyrinthView.invalidate();
            }

            startMoving(newCoordinates);
        }
        else
        {
            Toast.makeText(labyrinthView.getContext(), "Перемешаться можно только по одной оси!", Toast.LENGTH_SHORT).show();
        }
    }

    void startMoving(@NonNull Point newCoordinates)
    {
        if(newCoordinates.x - coordinates.x != 0)
        {
            int delta = 1;
            if(newCoordinates.x < coordinates.x)
            {
                delta = -1;
            }
            for(int pos = coordinates.x + delta; ; pos += delta)
            {
                if(!labyrinthView.canEnterCell(pos, coordinates.y))
                {
                    break;
                }
                else
                {
                    setX(pos);
                    labyrinthView.getCell(pos, coordinates.y).onEnter(this);
                    if(pos == newCoordinates.x)
                    {
                        break;
                    }
                }
            }
        }

        if(newCoordinates.y - coordinates.y != 0)
        {
            int delta = 1;
            if(newCoordinates.y < coordinates.y)
            {
                delta = -1;
            }
            for(int pos = coordinates.y + delta; ; pos += delta)
            {
                if(!labyrinthView.canEnterCell(coordinates.x, pos))
                {
                    break;
                }
                else
                {
                    setY(pos);
                    labyrinthView.getCell(coordinates.x, pos).onEnter(this);
                    if(pos == newCoordinates.y)
                    {
                        break;
                    }
                }
            }
        }
    }

    boolean checkOnlyOneDirection(@NonNull Point coordinates)
    {
        int dx = coordinates.x - this.coordinates.x;
        int dy = coordinates.y - this.coordinates.y;
        if(dx == 0 ^ dy == 0)
        {
            return true;
        }
        return false;
    }


    public void setCellSize(double cellSize) {
        this.cellSize = cellSize;
        try {
            bm = BitmapFactory.decodeResource(labyrinthView.getResources(), R.drawable.pikachu);
            bmSized = Bitmap.createScaledBitmap(bm, (int) cellSize, (int) cellSize, true);
        }
        catch (Exception e)
        {
            Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void draw(Canvas canvas, Paint paint)
    {
        try {
            Point pixelsCoordinates = labyrinthView.toPixelCoordinates(coordinates);
            canvas.drawBitmap(bmSized, pixelsCoordinates.x, pixelsCoordinates.y, paint);
        }
        catch (Exception e)
        {
            Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
