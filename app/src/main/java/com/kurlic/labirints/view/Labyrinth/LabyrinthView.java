package com.kurlic.labirints.view.Labyrinth;

import android.app.Notification;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kurlic.labirints.R;

public class LabyrinthView extends View {

    private int w,h;
    int strokeWidthDp = 2;
    int strokeWidthPixels;
    int cxCell = 10;
    int cyCell = 20;
    double oneCellSize;

    Character character;


    public LabyrinthView(Context context) {
        super(context);
        commonConstructor();
    }

    public LabyrinthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonConstructor();
    }

    public LabyrinthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonConstructor();
    }

    public LabyrinthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        commonConstructor();
    }

    void commonConstructor()
    {
        character = new Character(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.labyrinthBackground));
        paint.setStyle(Paint.Style.FILL);
        w = getWidth();
        h = getHeight();

        Rect rect = new Rect(0, 0, w, h);
        canvas.drawRect(rect, paint);

        drawNet(paint, canvas);

        character.setCellSize(getOneCellSize());
        character.draw(canvas, paint);

    }

    public double getOneCellSize() {
        oneCellSize = (float)getWidth() / (float)cxCell;
        return oneCellSize;
    }

    public int getStrokeWidthPixels() {
        strokeWidthPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, strokeWidthDp, getResources().getDisplayMetrics());
        return strokeWidthPixels;
    }

    void drawNet (@NonNull Paint paint, Canvas canvas)
    {
        paint.setColor(getResources().getColor(R.color.labyrinthNet));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getStrokeWidthPixels());
        oneCellSize = getOneCellSize();
        for(double i = oneCellSize; i < w; i += oneCellSize)
        {
            canvas.drawLine((float)i, 0, (float) i, h, paint);
        }

        for(double i = oneCellSize; i < h; i += oneCellSize)
        {
            canvas.drawLine(0, (float)i, w, (float)i, paint);
        }


    }


    //DrawSection end



    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point cell = toCellCoordinates(x, y);

            Toast.makeText(getContext(), "x: " + cell.x + " y: " + cell.y, Toast.LENGTH_SHORT).show();
            character.setCoordinates(cell);

        }


        return true;

    }

    public Point toCellCoordinates(int x, int y)
    {
        Point answer = new Point();

        try {
            int xnum = (int) (x / getOneCellSize());
            int ynum = (int) (y / getOneCellSize());

            answer.set(xnum, ynum);
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return answer;

    }

    public Point toCellCoordinates(@NonNull Point point)
    {
        return toCellCoordinates(point.x, point.y);

    }



    public Point toPixelCoordinates(int x, int y)
    {
        Point answer = new Point();

        int xnum = (int) (x * getOneCellSize());
        int ynum = (int) (y * getOneCellSize());

        answer.set(xnum, ynum);

        return answer;

    }

    public Point toPixelCoordinates(@NonNull Point point)
    {
        return toPixelCoordinates(point.x, point.y);

    }




}
