package com.kurlic.labirints.view.Labyrinth;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.Cells.LabyrinthCell;

public class Character {


    Point coordinates;
    Bitmap bm;
    Bitmap bmSized;
    LabyrinthView labyrinthView;
    double cellSize;

    double maxRelativeMissClick = 0.3;

    public Character(LabyrinthView labyrinthView) {
        super();
        this.labyrinthView = labyrinthView;
        coordinates = new Point();
    }

    private void setX(int x)
    {
        setCoordinates(new Point(x, coordinates.y));
    }

    private void setY(int y)
    {
        setCoordinates(new Point(coordinates.x, y));
    }

    public void moveTo(Point newCoordinates)
    {
        if(isCoordinatesSuitable(newCoordinates)) {
            newCoordinates = oneDirectionCell(newCoordinates);
            if (checkOnlyOneDirection(newCoordinates) == true) {
                if (getCoordinates() != newCoordinates) {
                    labyrinthView.invalidate();
                    startMoving(newCoordinates);
                }
            } else {
                //Toast.makeText(labyrinthView.getContext(), "Перемешаться можно только по одной оси!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void move(@NonNull Point moveDelta)
    {
        Point newCoordinates = new Point();
        newCoordinates.x = getCoordinates().x + moveDelta.x;
        newCoordinates.y = getCoordinates().y + moveDelta.y;

        moveTo(newCoordinates);
    }

    public boolean isCoordinatesSuitable(@NonNull Point newCoordinates)
    {
        if(newCoordinates.x < labyrinthView.getCxCell() && newCoordinates.y < labyrinthView.getCyCell())
        {
            if(newCoordinates.x >= 0 && newCoordinates.y >= 0)
            {
                return true;
            }
        }
        return false;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        if(isCoordinatesSuitable(coordinates)) {
            this.coordinates = coordinates;
            labyrinthView.invalidate();
        }
    }

    void startMoving(@NonNull Point newCoordinates)
    {
        Point deltaMove = new Point();
        if(newCoordinates.x - getCoordinates().x != 0)
        {
            int delta = 1;
            LabyrinthCell.MoveDirection moveDirection = LabyrinthCell.MoveDirection.RIGHT;
            if(newCoordinates.x < getCoordinates().x)
            {
                delta = -1;
                moveDirection = LabyrinthCell.MoveDirection.LEFT;
            }
            for(int pos = getCoordinates().x + delta; ; pos += delta)
            {
                if(!labyrinthView.canEnterCell(pos, getCoordinates().y))
                {
                    break;
                }
                else if (!labyrinthView.getCell(getCoordinates().x, getCoordinates().y).canMove(moveDirection))
                {
                    break;
                }
                else
                {
                    deltaMove.x = newCoordinates.x - getCoordinates().x;
                    deltaMove.y = newCoordinates.y - getCoordinates().y;
                    setX(pos);
                    labyrinthView.getCell(pos, getCoordinates().y).onEnter(this);
                    if(pos == newCoordinates.x || getCoordinates().x != pos)
                    {
                        break;
                    }
                }
            }
        }
        else if(newCoordinates.y - getCoordinates().y != 0)
        {
            int delta = 1;
            LabyrinthCell.MoveDirection moveDirection = LabyrinthCell.MoveDirection.DOWN;
            if(newCoordinates.y < getCoordinates().y)
            {
                delta = -1;
                moveDirection = LabyrinthCell.MoveDirection.UP;
            }
            for(int pos = getCoordinates().y + delta; ; pos += delta)
            {
                if(!labyrinthView.canEnterCell(getCoordinates().x, pos))
                {
                    break;
                }
                else if (!labyrinthView.getCell(getCoordinates().x, getCoordinates().y).canMove(moveDirection))
                {
                    break;
                }
                else
                {
                    deltaMove.x = newCoordinates.x - getCoordinates().x;
                    deltaMove.y = newCoordinates.y - getCoordinates().y;
                    setY(pos);
                    labyrinthView.getCell(getCoordinates().x, pos).onEnter(this);
                    if(pos == newCoordinates.y || getCoordinates().y != pos)
                    {
                        break;
                    }
                }
            }
        }
        onMoveNotify(newCoordinates, deltaMove);

    }

    private void onMoveNotify(Point newCoordinates, Point delta)
    {
        int cxCell = labyrinthView.getCxCell();
        int cyCell = labyrinthView.getCyCell();
        for(int x = 0; x < cxCell; x++)
        {
            for(int y = 0; y < cyCell; y++)
            {
                LabyrinthCell labyrinthCell = labyrinthView.getCell(x, y);
                labyrinthCell.onCharacterMove(this, delta);
            }
        }
    }



    boolean checkOnlyOneDirection(@NonNull Point newCoordinates)
    {
        int dx = newCoordinates.x - this.coordinates.x;
        int dy = newCoordinates.y - this.coordinates.y;

        if(dx == 0 && dy == 0)
        {
            return true;
        }
        if(dx == 0 ^ dy == 0)
        {
            return true;
        }
        return false;
    }

    Point oneDirectionCell(@NonNull Point newCoordinates)
    {
        int dx = newCoordinates.x - this.coordinates.x;
        int dy = newCoordinates.y - this.coordinates.y;
        if(Math.abs(dx) > Math.abs(dy))
        {
            if(dx != 0) {
                if (Math.abs((float)dy / (float)dx) <= maxRelativeMissClick) {
                    dy = 0;
                }
            }
        }
        if(Math.abs(dx) < Math.abs(dy))
        {
            if(dy != 0) {
                if (Math.abs((float)dx / (float)dy) <= maxRelativeMissClick) {
                    dx = 0;
                }
            }
        }
        Point answer = new Point();
        answer.x = getCoordinates().x + dx;
        answer.y = getCoordinates().y + dy;

        return answer;

    }


    public void setCellSize(double cellSize) {
        this.cellSize = cellSize;
        try {
            bm = BitmapFactory.decodeResource(labyrinthView.getResources(), R.drawable.pikachu);
            bmSized = Bitmap.createScaledBitmap(bm, (int) cellSize, (int) cellSize, true);
        }
        catch (Exception e)
        {
            //Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
