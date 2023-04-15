package com.kurlic.labirints.view.Labyrinth;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.Cells.LabyrinthCell;

public class Character {


    private  Point coordinates;
    private  Point pixelCoordinates;
    private  Bitmap bm;
    private  Bitmap bmSized;
    private  LabyrinthView labyrinthView;
    double cellSize;

    double maxRelativeMissClick = 0.3;

    public Character(LabyrinthView labyrinthView) {
        super();
        this.labyrinthView = labyrinthView;
        coordinates = new Point();
        pixelCoordinates = new Point();
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

    public void moveUp()
    {
        verticalMove(LabyrinthCell.MoveDirection.UP);
    }
    public void moveDown()
    {
        verticalMove(LabyrinthCell.MoveDirection.DOWN);
    }

    public void moveLeft()
    {
        horizontalMove(LabyrinthCell.MoveDirection.LEFT);
    }

    public void moveRight()
    {
        horizontalMove(LabyrinthCell.MoveDirection.RIGHT);
    }

    private void verticalMove(LabyrinthCell.MoveDirection moveDirection)
    {
        int delta = 1;
        Point newCoordinates = new Point(getCoordinates());

        if(moveDirection == LabyrinthCell.MoveDirection.UP)
        {
            delta = -1;
        }

        boolean needToContinue = true;
        boolean isFirstMove = true;
        for(int pos = getCoordinates().y + delta; ; pos += delta)
        {
            if(pos < 0 || pos >= labyrinthView.getCyCell())
            {
                break;
            }
            else if(!labyrinthView.canEnterCell(newCoordinates.x, pos))
            {
                break;
            }
            else if (!labyrinthView.getCell(newCoordinates.x, newCoordinates.y).canMove(moveDirection))
            {
                break;
            }
            else if((labyrinthView.getCell(newCoordinates.x, newCoordinates.y).canMove(LabyrinthCell.MoveDirection.LEFT) ||
                    labyrinthView.getCell(newCoordinates.x, newCoordinates.y).canMove(LabyrinthCell.MoveDirection.RIGHT))
                    && !isFirstMove)
            {
                break;
            }
            else
            {
                newCoordinates.y += delta;
                isFirstMove = false;
                needToContinue = labyrinthView.getCell(newCoordinates.x, newCoordinates.y).onEnter(this);
                if(!needToContinue)
                {
                    break;
                }
            }
        }
        if(needToContinue)
        {
            Log.d("New cell position before animation", newCoordinates.toString());
            Point oldCoordinates = new Point(getCoordinates());
            setCoordinates(newCoordinates, false);
            animateVerticalWay(oldCoordinates, getCoordinates());
        }
    }

    private void horizontalMove(LabyrinthCell.MoveDirection moveDirection)
    {
        int delta = 1;
        Point newCoordinates = new Point(getCoordinates());

        if(moveDirection == LabyrinthCell.MoveDirection.LEFT)
        {
            delta = -1;
        }

        boolean needToContinue = true;
        boolean isFirstMove = true;
        for(int pos = getCoordinates().x + delta; ; pos += delta)
        {
            if(pos < 0 || pos >= labyrinthView.getCxCell())
            {
                break;
            }
            else if(!labyrinthView.canEnterCell(pos, newCoordinates.y))
            {
                break;
            }
            else if (!labyrinthView.getCell(newCoordinates.x, newCoordinates.y).canMove(moveDirection))
            {
                break;
            }
            else if((labyrinthView.getCell(newCoordinates.x, newCoordinates.y).canMove(LabyrinthCell.MoveDirection.UP) ||
                    labyrinthView.getCell(newCoordinates.x, newCoordinates.y).canMove(LabyrinthCell.MoveDirection.DOWN))
                    && !isFirstMove)
            {
                break;
            }
            else
            {
                newCoordinates.x += delta;
                isFirstMove = false;
                needToContinue = labyrinthView.getCell(newCoordinates.x, newCoordinates.y).onEnter(this);
                if(!needToContinue) break;
            }
        }
        if(needToContinue)
        {
            Log.d("New cell position before animation", newCoordinates.toString());
            Point oldCoordinates = new Point(getCoordinates());
            setCoordinates(newCoordinates, false);
            if (needToContinue) animateHorizontalWay(oldCoordinates, getCoordinates());
        }
    }

    ValueAnimator wayAnimator;
    private final long animatorDuration = 70;
    private void animateHorizontalWay(@NonNull Point start, @NonNull Point finish)
    {
        int cellDelta = finish.x - start.x;
        Point pixelStart = labyrinthView.toPixelCoordinates(start);
        Point pixelFinish = labyrinthView.toPixelCoordinates(finish);
        float pixelDelta = pixelFinish.x - pixelStart.x;

        setYPixelCoordinates(pixelFinish.y);

        wayAnimator = ValueAnimator.ofFloat(0, 1);
        wayAnimator.setDuration(animatorDuration * Math.abs(cellDelta));
        Character character = this;

        float oneCellSize = (float) labyrinthView.getOneCellSize();
        wayAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float lastOnEnterCellCalled = 0;

            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation)
            {
                float progress = (float) animation.getAnimatedValue();
                float relativePixelDelta = progress * pixelDelta;
                float newX = pixelStart.x + relativePixelDelta;
                setXPixelCoordinates((int)newX);
                Log.d("onAnimationUpdate", "newX:" + newX + " progress" + progress + " pixelDelta" + pixelDelta + " cell" + getCoordinates() + " start" + start);

                /*
                float currCell = relativePixelDelta / oneCellSize;
                if(Math.abs(currCell) > Math.abs(lastOnEnterCellCalled))
                {
                    labyrinthView.getCell((int) (start.x + lastOnEnterCellCalled + 1), getCoordinates().y).onEnter(character);
                    lastOnEnterCellCalled = currCell;
                }

                 */
            }
        });

        wayAnimator.start();

    }
    private void animateVerticalWay(@NonNull Point start, @NonNull Point finish)
    {
        int cellDelta = finish.y - start.y;
        Point pixelStart = labyrinthView.toPixelCoordinates(start);
        Point pixelFinish = labyrinthView.toPixelCoordinates(finish);
        float pixelDelta = pixelFinish.y - pixelStart.y;

        setXPixelCoordinates(pixelFinish.x);

        wayAnimator = ValueAnimator.ofFloat(0, 1);
        wayAnimator.setDuration(animatorDuration * Math.abs(cellDelta));

        Character character = this;

        float oneCellSize = (float) labyrinthView.getOneCellSize();
        wayAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float lastOnEnterCellCalled = 0;
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation)
            {
                float progress = (float) animation.getAnimatedValue();
                float relativePixelDelta = progress * pixelDelta;
                float newY = pixelStart.y + relativePixelDelta;
                setYPixelCoordinates((int)newY);
                Log.d("onAnimationUpdate", "newY:" + newY + " progress" + progress + " pixelDelta" + pixelDelta + " cell" + getCoordinates() + " start" + start);
            }
        });

        wayAnimator.start();

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
            if(wayAnimator != null)
            {
                if(wayAnimator.isRunning())
                {
                    wayAnimator.cancel();
                }
            }
            setPixelCoordinates(labyrinthView.toPixelCoordinates(coordinates));
        }
    }
    private void setCoordinates(Point coordinates, boolean needToRefreshPixel) {
        if(isCoordinatesSuitable(coordinates)) {
            this.coordinates = coordinates;
            if(needToRefreshPixel) setPixelCoordinates(labyrinthView.toPixelCoordinates(coordinates));
        }
    }

    public Point getPixelCoordinates()
    {
        return pixelCoordinates;
    }

    public void setPixelCoordinates(Point pixelCoordinates)
    {
        this.pixelCoordinates = pixelCoordinates;
        labyrinthView.invalidate();
    }

    public void setXPixelCoordinates(int x)
    {
        this.pixelCoordinates.x = x;
        labyrinthView.invalidate();
    }
    public void setYPixelCoordinates(int y)
    {
        this.pixelCoordinates.y = y;
        labyrinthView.invalidate();
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
        try
        {
            //Point pixelsCoordinates = labyrinthView.toPixelCoordinates(coordinates);
            canvas.drawBitmap(bmSized, pixelCoordinates.x, pixelCoordinates.y, paint);
            Log.d("pixelCoordinates", pixelCoordinates.toString());
        }
        catch (Exception e)
        {
            //Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
