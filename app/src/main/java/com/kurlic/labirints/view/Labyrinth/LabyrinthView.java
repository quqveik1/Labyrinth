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
    int strokeWidthDp = 1;
    int strokeWidthPixels;

    int cxCell = 10;
    int cyCell = 20;
    double oneCellSize;
    LabyrinthCell[][] labyrinthCells;

    private Character character;


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
        cellsConstructor();

    }

    void cellsConstructor()
    {
        labyrinthCells = new LabyrinthCell[cxCell][cyCell];

        for(int x = 0; x < cxCell; x++)
        {
            for(int y = 0; y < cyCell; y++)
            {
                labyrinthCells[x][y] = new EmptyCell(this);
            }
        }
        labyrinthCells[0][4] = new WallCell(this);
        labyrinthCells[4][0] = new WallCell(this);

        labyrinthCells[1][5] = new TeleportCell(this, new Point(5, 4));

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
        drawCells(paint, canvas);
        character.draw(canvas, paint);

    }

    void drawCells(@NonNull Paint paint, @NonNull Canvas canvas)
    {
        for(int x = 0; x < cxCell; x++)
        {
            for (int y = 0; y < cyCell; y++)
            {
                Point start = toPixelCoordinates(x, y);
                Rect cellRect = new Rect(start.x, start.y, (int) (start.x + oneCellSize), (int) (start.y + oneCellSize));
                labyrinthCells[x][y].draw(canvas, paint, cellRect);

            }
        }

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

    public double getOneCellSize() {
        oneCellSize = (float)getWidth() / (float)cxCell;
        return oneCellSize;
    }

    public int getStrokeWidthPixels() {
        strokeWidthPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, strokeWidthDp, getResources().getDisplayMetrics());
        return strokeWidthPixels;
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {

        if(event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if(x >= 0 && y >= 0) {
                if (x <= getWidth() && y <= getHeight()) {


                    Point cell = toCellCoordinates(x, y);

                    //Toast.makeText(getContext(), "x: " + cell.x + " y: " + cell.y, Toast.LENGTH_SHORT).show();
                    character.moveTo(cell);
                }
            }
        }

        return true;

    }

    public LabyrinthCell getCell(int x, int y)
    {
        return labyrinthCells[x][y];
    }

    public boolean canEnterCell(int x, int y)
    {
        return labyrinthCells[x][y].canEnter(character);
    }

    public boolean canEnterCell(@NonNull Point point)
    {
        return canEnterCell(point.x, point.y);
    }

    public Point toCellCoordinates(int x, int y)
    {
        Point answer = new Point();

        try {
            int xnum = (int) (x / getOneCellSize());
            int ynum = (int) (y / getOneCellSize());
            xnum = Integer.max(xnum, 0);
            ynum = Integer.max(ynum, 0);
            xnum = Integer.min(xnum, cxCell - 1);
            ynum = Integer.min(ynum, cyCell - 1);

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

    public int getCxCell() {
        return cxCell;
    }

    public void setCxCell(int cxCell) {
        this.cxCell = cxCell;
    }

    public int getCyCell() {
        return cyCell;
    }

    public void setCyCell(int cyCell) {
        this.cyCell = cyCell;
    }

    public Point toPixelCoordinates(int x, int y)
    {
        Point answer = new Point();

        int xnum = (int) (x * getOneCellSize());
        int ynum = (int) (y * getOneCellSize());

        xnum = Integer.max(xnum, 0);
        ynum = Integer.max(ynum, 0);
        xnum = Integer.min(xnum, getWidth() - 1);
        ynum = Integer.min(ynum, getHeight() - 1);

        answer.set(xnum, ynum);

        return answer;

    }

    public Point toPixelCoordinates(@NonNull Point point)
    {
        return toPixelCoordinates(point.x, point.y);

    }

    public Character getCharacter() {
        return character;
    }
}
