package com.kurlic.labirints.view.Labyrinth;

import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

public class LabyrinthSwipeDetector extends GestureDetector.SimpleOnGestureListener
{
    LabyrinthView labyrinthView;
    LabyrinthSwipeDetector(LabyrinthView labyrinthView)
    {
        this.labyrinthView = labyrinthView;
    }

    private static final int SWIPE_THRESHOLD = 10;
    private static final int SWIPE_VELOCITY_THRESHOLD = 10;

    @Override
    public boolean onDown(@NonNull MotionEvent e)
    {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            // Swipe left or right
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    onSwipeRight();
                } else {
                    onSwipeLeft();
                }
                return true;
            }
        } else {
            // Swipe up or down
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeDown();
                } else {
                    onSwipeUp();
                }
                return true;
            }
        }
        return false;
    }

    public void onSwipeRight()
    {
        labyrinthView.getCharacter().moveRight();
    }

    public void onSwipeLeft()
    {
        labyrinthView.getCharacter().moveLeft();
    }

    public void onSwipeUp()
    {
        labyrinthView.getCharacter().moveUp();
    }

    public void onSwipeDown()
    {
        labyrinthView.getCharacter().moveDown();
    }
}
