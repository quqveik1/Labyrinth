package com.kurlic.labirints.view.Labyrinth.Cells;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.Character;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class LabyrinthCell
{
    LabyrinthView labyrinthView;
    Point labyrinthPosition;

    private boolean inSolutionPath = false;


    public LabyrinthCell(@NonNull LabyrinthView labyrinthView, int x, int y)
    {
        setLabyrinthView(labyrinthView);
        setLabyrinthPosition(new Point(x, y));
    }

    LabyrinthCell(@NonNull LabyrinthView labyrinthView, @NonNull Point coordinates)
    {
        setLabyrinthView(labyrinthView);
        setLabyrinthPosition(coordinates);
    }

    public LabyrinthView getLabyrinthView()
    {
        return labyrinthView;
    }

    public void setLabyrinthView(LabyrinthView labyrinthView)
    {
        this.labyrinthView = labyrinthView;
    }

    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect)
    {
        try
        {
            drawSolution(canvas, paint, rect);
            drawBorders(canvas, paint, rect);
            drawCellPos(canvas, paint, rect);
        }
        catch (Exception e)
        {
            //if(getLabyrinthView().getContext())Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void drawSolution(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect)
    {
        if(isInSolutionPath() && getLabyrinthView().getSolutionShowStatus())
        {
            paint.setColor(getLabyrinthView().getResources().getColor(R.color.solution));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(rect, paint);
        }
    }

    protected void drawBorders(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect)
    {
        try
        {
            paint.setColor(getLabyrinthView().getResources().getColor(R.color.wall));
            float strokeWidth = (float) ((rect.right - rect.left) * 0.1);
            paint.setStrokeWidth(strokeWidth);
            if (upBorder) canvas.drawLine(rect.left, rect.top, rect.right, rect.top, paint);
            if (rightBorder) canvas.drawLine(rect.right, rect.top, rect.right, rect.bottom, paint);
            if (downBorder) canvas.drawLine(rect.left, rect.bottom, rect.right, rect.bottom, paint);
            if (leftBorder) canvas.drawLine(rect.left, rect.top, rect.left, rect.bottom, paint);
        }
        catch (Exception e)
        {
            Log.e(String.valueOf(Log.ERROR), e.toString());
        }
    }

    protected void drawCellPos(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect)
    {
        try
        {
            String text = "" + getLabyrinthPosition().x + "|" + getLabyrinthPosition().y;
            paint.setTextSize(20);
            paint.setStrokeWidth(2);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(labyrinthView.getResources().getColor(R.color.debugColor));
            int midX = (rect.left + rect.right) / 2;
            int midY = (rect.top + rect.bottom) / 2;
            canvas.drawText(text, midX, midY, paint);
        } catch (Exception e)
        {
            //Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public Point getLabyrinthPosition()
    {
        //labyrinthPosition = getLabyrinthView().getCellPosition(this);
        return labyrinthPosition;
    }

    public void setLabyrinthPosition(Point labyrinthPosition)
    {
        this.labyrinthPosition = labyrinthPosition;
    }

    public void onCharacterMove(@NonNull Character character, @NonNull Point moveDelta) {}

    public boolean canEnter(@NonNull Character character) {return true;}



    public enum MoveDirection
    {
        LEFT,
        UP,
        RIGHT,
        DOWN
    }

    protected boolean leftBorder = true;
    protected boolean upBorder = true;
    protected boolean rightBorder = true;
    protected boolean downBorder = true;

    public boolean canMove(MoveDirection moveDirection)
    {
        if(moveDirection == MoveDirection.LEFT)
        {
            if(!leftBorder)
            {
                return true;
            }
        }

        if(moveDirection == MoveDirection.UP)
        {
            if(!upBorder)
            {
                return true;
            }
        }

        if(moveDirection == MoveDirection.RIGHT)
        {
            if(!rightBorder)
            {
                return true;
            }
        }

        if(moveDirection == MoveDirection.DOWN)
        {
            if(!downBorder)
            {
                return true;
            }
        }

        return false;
    }


    public void setLeftBorder(boolean leftBorder)
    {
        this.leftBorder = leftBorder;
    }

    public void setUpBorder(boolean upBorder)
    {
        this.upBorder = upBorder;
    }

    public void setRightBorder(boolean rightBorder)
    {
        this.rightBorder = rightBorder;
    }

    public void setDownBorder(boolean downBorder)
    {
        this.downBorder = downBorder;
    }

    public void onEnter(@NonNull Character character) {};

    public void onCellSize(int newSize) {};

    public void setInSolutionPath(boolean inSolutionPath)
    {
        this.inSolutionPath = inSolutionPath;
    }

    public boolean isInSolutionPath()
    {
        return inSolutionPath;
    }
}
