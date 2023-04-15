package com.kurlic.labirints.view.Arrow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class Arrow extends View {

    LabyrinthView labyrinthView;
    int direction = 0;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_UP = 2;
    public static final int DIRECTION_RIGHT = 3;
    public static final int DIRECTION_DOWN = 4;
    int oneMoveTimeDelta = 100;
    Point moveDelta = new Point();
    TouchThread touchThread;


    public Arrow(Context context) {
        super(context);
        commonConstructor(context, null);
    }

    public Arrow(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonConstructor(context, attrs);
    }

    public Arrow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        commonConstructor(context, attrs);

    }

    void commonConstructor(Context context, AttributeSet attrs)
    {
        if(attrs != null)
        {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Arrow);
            String strDirection = array.getString(R.styleable.Arrow_direction);
            if(strDirection.compareTo("left") == 0)
            {
                direction = DIRECTION_LEFT;
                moveDelta = new Point(-1, 0);
            }
            if(strDirection.compareTo("up")== 0)
            {
                direction = DIRECTION_UP;
                moveDelta = new Point(0, -1);
            }
            if(strDirection.compareTo("right") == 0)
            {
                direction = DIRECTION_RIGHT;
                moveDelta = new Point(1, 0);
            }
            if(strDirection.compareTo("down") == 0)
            {
                direction = DIRECTION_DOWN;
                moveDelta = new Point(0, 1);
            }

        }
    }

    public void setLabyrinthView(LabyrinthView labyrinthView) {
        this.labyrinthView = labyrinthView;
    }



    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        Paint paint = new Paint();


        paint.setColor(getResources().getColor(R.color.arrow));
        paint.setStyle(Paint.Style.FILL);

        Path triangle = null;

        if(direction == DIRECTION_LEFT)
        {
            triangle = drawLeft();
        }
        if(direction == DIRECTION_RIGHT)
        {
            triangle = drawRight();
        }
        if(direction == DIRECTION_UP)
        {
            triangle = drawUp();
        }
        if(direction == DIRECTION_DOWN)
        {
            triangle = drawDown();
        }


        if (triangle != null)canvas.drawPath(triangle, paint);
    }

    @NonNull
    private Path drawLeft()
    {
        Path triangle = new Path();
        int halfOfHeight = getHeight()/2;
        triangle.setFillType(Path.FillType.EVEN_ODD);
        triangle.moveTo(getWidth(), 0);
        triangle.lineTo(getWidth(), getHeight());
        triangle.lineTo(0, halfOfHeight);
        triangle.lineTo(getWidth(), 0);
        triangle.close();
        return  triangle;
    }


    @NonNull
    private Path drawRight()
    {
        Path triangle = new Path();
        int halfOfHeight = getHeight()/2;
        triangle.setFillType(Path.FillType.EVEN_ODD);
        triangle.moveTo(0, 0);
        triangle.lineTo(getWidth(), halfOfHeight);
        triangle.lineTo(0, getHeight());
        triangle.lineTo(0, 0);
        triangle.close();
        return triangle;
    }

    @NonNull
    private Path drawUp()
    {
        Path triangle = new Path();
        int halfOfWidth = getWidth()/2;
        triangle.setFillType(Path.FillType.EVEN_ODD);
        triangle.moveTo(0, getHeight());
        triangle.lineTo(halfOfWidth, 0);
        triangle.lineTo(getWidth(), getHeight());
        triangle.lineTo(0, getHeight());
        triangle.close();
        return triangle;
    }
    @NonNull
    private Path drawDown()
    {
        Path triangle = new Path();
        int halfOfWidth = getWidth()/2;
        triangle.setFillType(Path.FillType.EVEN_ODD);
        triangle.moveTo(0, 0);
        triangle.lineTo(getWidth(), 0);
        triangle.lineTo(halfOfWidth, getHeight());
        triangle.lineTo(0, 0);
        triangle.close();
        return triangle;
    }



    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if(direction == DIRECTION_LEFT)
            {
                labyrinthView.getCharacter().moveLeft();
            }
            if(direction == DIRECTION_RIGHT)
            {
                labyrinthView.getCharacter().moveRight();
            }
            if(direction == DIRECTION_UP)
            {
                labyrinthView.getCharacter().moveUp();
            }
            if(direction == DIRECTION_DOWN)
            {
                labyrinthView.getCharacter().moveDown();
            }

        }

        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            //touchThread.setNeedToRun(false);
        }

        if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
        {
            //touchThread.setNeedToRun(false);
        }
        return true;
    }

}
