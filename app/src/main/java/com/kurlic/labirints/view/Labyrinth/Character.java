package com.kurlic.labirints.view.Labyrinth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.widget.Toast;

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

    public void setX(int x)
    {
        coordinates.x = x;
    }

    public void setY(int y)
    {
        coordinates.y = y;
    }

    public void setCoordinates(Point coordinates) {
        if(this.coordinates != coordinates)
        {
            labyrinthView.invalidate();
        }
        this.coordinates = coordinates;
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
