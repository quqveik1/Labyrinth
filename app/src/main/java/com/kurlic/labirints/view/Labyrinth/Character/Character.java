package com.kurlic.labirints.view.Labyrinth.Character;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.Cells.LabyrinthCell;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Character {


    private  Point coordinates;
    private  Point pixelCoordinates;
    private  Bitmap bm;
    private  Bitmap bmSized;
    private LabyrinthView labyrinthView;
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

    private boolean isAnimationActive = false;

    private boolean isAnimationActive()
    {
        return isAnimationActive;
    }

    private void setAnimationActive(boolean animationActive)
    {
        isAnimationActive = animationActive;
    }

    class MoveThread extends Thread
    {
        private LabyrinthCell.MoveDirection moveDirection;

        public LabyrinthCell.MoveDirection getMoveDirection()
        {
            return moveDirection;
        }

        public void setMoveDirection(LabyrinthCell.MoveDirection moveDirection)
        {
            this.moveDirection = moveDirection;
        }

        private boolean workStatus;

        public boolean getWorkStatus()
        {
            return workStatus;
        }

        public void setWorkStatus(boolean workStatus)
        {
            this.workStatus = workStatus;
            if(workStatus == false)
            {
                if(wayAnimator != null)
                {
                    if(wayAnimator.isRunning())
                    {
                        wayAnimator.end();
                    }
                }
            }
        }


        @Override
        public void run()
        {
            super.run();
            setWorkStatus(true);
            int delta = 1;
            Point newCoordinates = new Point(getCoordinates());

            if(moveDirection == LabyrinthCell.MoveDirection.LEFT)
            {
                delta = -1;
            }

            boolean isFirstMove = true;
            while(getWorkStatus())
            {
                newCoordinates.x += delta;
                if(newCoordinates.x < 0 || newCoordinates.x >= labyrinthView.getCxCell())
                {
                    break;
                }
                else if(!labyrinthView.canEnterCell(newCoordinates.x, newCoordinates.y))
                {
                    break;
                }
                else if (!labyrinthView.getCell(getCoordinates()).canMove(moveDirection))
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
                    labyrinthView.getCell(newCoordinates.x, newCoordinates.y).onEnter(character);
                    animateHorizontalWay(getCoordinates(), newCoordinates);
                    while (wayAnimator.isRunning())
                    {
                        try
                        {
                            sleep(5);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    }

    Character character = this;

    MoveThread activeMoveThread;
    MoveAnimator moveAnimator;
    private void horizontalMove(LabyrinthCell.MoveDirection moveDirection)
    {
        if(moveAnimator != null)
        {
            if(moveAnimator.isRunning())
            {
                moveAnimator.softStop();
            }
        }
        moveAnimator = new MoveAnimator(moveDirection);
        moveAnimator.start();

        /*
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

         */
    }
    class MoveAnimator extends ValueAnimator
    {
        LabyrinthCell.MoveDirection moveDirection;
        Point newCoordinates;
        Point oldCoordinates;
        int delta = 1;
        boolean isFirstMove = true;

        public LabyrinthCell.MoveDirection getMoveDirection()
        {
            return moveDirection;
        }

        public void setMoveDirection(LabyrinthCell.MoveDirection moveDirection)
        {
            this.moveDirection = moveDirection;
            if(moveDirection == LabyrinthCell.MoveDirection.LEFT)
            {
                delta = -1;
            }
        }

        public void softStop()
        {
            //setRepeatCount(0);
            int currentRepeatCount = (int) (getCurrentPlayTime() / animatorDuration); // вычисляем текущее количество повторений
            setRepeatCount(currentRepeatCount);
            setNeedToDoNewAnimation(false);
        }

        MoveAnimator(LabyrinthCell.MoveDirection moveDirection)
        {
            super();
            setMoveDirection(moveDirection);
            setDuration(animatorDuration);
            setFloatValues(0, 1);
            setEvaluator(new FloatEvaluator());
            newCoordinates = new Point(getCoordinates());
            oldCoordinates = new Point(getCoordinates());
            setRepeatCount(INFINITE);
            addUpdateListener(new MoveUpdateListener());
            addListener(new MoveAnimationListener());
        }
        class MoveAnimationListener implements Animator.AnimatorListener
        {
            @Override
            public void onAnimationStart(@NonNull Animator animation)
            {
                controlAnimation();
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation)
            {

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation)
            {
                Log.d("MoveAnimation", "Animation canceled");
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation)
            {
                controlAnimation();
            }

            void controlAnimation()
            {
                oldCoordinates = new Point(getCoordinates());
                newCoordinates.x += delta;
                if(newCoordinates.x < 0 || newCoordinates.x >= labyrinthView.getCxCell())
                {
                    softStop();
                }
                else if(!labyrinthView.canEnterCell(newCoordinates.x, newCoordinates.y))
                {
                    softStop();
                }
                else if (!labyrinthView.getCell(getCoordinates()).canMove(moveDirection))
                {
                    softStop();
                }
                else if((labyrinthView.getCell(getCoordinates()).canMove(LabyrinthCell.MoveDirection.UP) ||
                        labyrinthView.getCell(getCoordinates()).canMove(LabyrinthCell.MoveDirection.DOWN))
                        && !isFirstMove)
                {
                    softStop();
                }
                else
                {
                    isFirstMove = false;
                    labyrinthView.getCell(newCoordinates).onEnter(character);
                    setCoordinates(newCoordinates);
                    setNeedToDoNewAnimation(true);
                    computePixelPosForAnimation();
                }
            }
        }


        void computePixelPosForAnimation()
        {
            pixelAnimationStart = labyrinthView.toPixelCoordinates(oldCoordinates);
            pixelAnimationFinish = labyrinthView.toPixelCoordinates(newCoordinates);
            pixelDelta = pixelAnimationFinish.x - pixelAnimationStart.x;
        }

        Point pixelAnimationStart = new Point();
        Point pixelAnimationFinish = new Point();
        float pixelDelta;

        private boolean needToDoNewAnimation = true;

        public boolean isNeedToDoNewAnimation()
        {
            return needToDoNewAnimation;
        }

        public void setNeedToDoNewAnimation(boolean needToDoNewAnimation)
        {
            this.needToDoNewAnimation = needToDoNewAnimation;
        }

        class MoveUpdateListener implements AnimatorUpdateListener
        {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation)
            {
                float progress = (float) animation.getAnimatedValue();
                if(progress < 0.3)
                {
                    if(!isNeedToDoNewAnimation())
                    {
                        return;
                    }
                }
                float relativePixelDelta = progress * pixelDelta;
                float newX = pixelAnimationStart.x + relativePixelDelta;
                setXPixelCoordinates((int) newX);
                Log.d("MoveAnimationPixelPos", "pos" + newX + " progress:" + progress + " pixelAnimationStart.x: " + pixelAnimationStart.x + " pixelAnimationFinish.x:" + pixelAnimationFinish.x);
            }
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
        wayAnimator.setRepeatCount(ValueAnimator.INFINITE);
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
            }
        });

        wayAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(@NonNull Animator animation)
            {
                setAnimationActive(true);

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation)
            {
                setAnimationActive(false);

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation)
            {
                setAnimationActive(false);
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation)
            {

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
            this.coordinates.x = coordinates.x;
            this.coordinates.y = coordinates.y;
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
            this.coordinates.x = coordinates.x;
            this.coordinates.y = coordinates.y;
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
