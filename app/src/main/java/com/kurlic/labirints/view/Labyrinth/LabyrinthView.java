package com.kurlic.labirints.view.Labyrinth;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.Cells.EmptyCell;
import com.kurlic.labirints.view.Labyrinth.Cells.FinishCell;
import com.kurlic.labirints.view.Labyrinth.Cells.LabyrinthCell;
import com.kurlic.labirints.view.Labyrinth.Cells.StartCell;
import com.kurlic.labirints.view.Labyrinth.Cells.WallCell;

public class LabyrinthView extends View {

    private int w, h;
    int strokeWidthDp = 1;
    int strokeWidthPixels;

    int cxCell = 9;
    int cyCell = 19;
    double oneCellSize;
    LabyrinthCell[][] labyrinthCells;
    LabyrinthCell startCell;

    LabyrinthGenerator activeLabyrinth;

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

    void commonConstructor() {
        character = new Character(this);
        cellsConstructor();

    }

    void cellsConstructor()
    {

        try
        {
            labyrinthCells = new LabyrinthCell[getCxCell()][getCyCell()];

            for (int x = 0; x < getCxCell(); x++)
            {
                for (int y = 0; y < getCyCell(); y++)
                {
                    setLabyrinthCell(new EmptyCell(this, x, y));
                }
            }

            try
            {
                StartCell startCell = new StartCell(this, 0, 0);
                setLabyrinthCell(startCell);
                setStartCell(startCell);

                generateLevel();

                setLabyrinthCell(new FinishCell(this, getCxCell() - 1, getCyCell() - 1));
            } catch (Exception e)
            {
                //Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        if(getOneCellSize() > 0)
        {
            for (int x = 0; x < getCxCell(); x++)
            {
                for (int y = 0; y < getCyCell(); y++)
                {
                    LabyrinthCell cell = getCell(x, y);
                    if(cell != null)
                    {
                        cell.onCellSize((int) getOneCellSize());
                    }
                }
            }

        }

    }

    private void generateLevel()
    {
        setActiveLabyrinth(new LabyrinthGenerator((int) Math.ceil((double) getCxCell() / 2), (int) Math.ceil((double) getCyCell() / 2), this));
    }

    public LabyrinthGenerator getActiveLabyrinth()
    {
        return activeLabyrinth;
    }

    public void setActiveLabyrinth(LabyrinthGenerator activeLabyrinth)
    {
        this.activeLabyrinth = activeLabyrinth;
    }

    public void setLabyrinthCell(LabyrinthCell labyrinthCell, int x, int y)
    {
        if(0 <= x && x < getCxCell() && 0 <= y && y < getCyCell())
        {
            labyrinthCells[x][y] = labyrinthCell;
        }
    }

    public void setLabyrinthCell(@NonNull LabyrinthCell labyrinthCell)
    {
        Point coordinates = labyrinthCell.getLabyrinthPosition();
        if(0 <= coordinates.x && coordinates.x < getCxCell() && 0 <= coordinates.y && coordinates.y < getCyCell())
        {
            labyrinthCells[coordinates.x][coordinates.y] = labyrinthCell;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Point requiredSize = getOnMeasureSize(heightMeasureSpec);

        int wRequiredMeasureSpecs = MeasureSpec.makeMeasureSpec(requiredSize.x, MeasureSpec.EXACTLY);
        int hRequiredMeasureSpecs = MeasureSpec.makeMeasureSpec(requiredSize.y, MeasureSpec.EXACTLY);
        super.onMeasure(wRequiredMeasureSpecs, hRequiredMeasureSpecs);
    }

    @NonNull
    Point getOnMeasureSize(int heightMeasureSpec)
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);


        int height = (int) (screenSize.y * 0.70);
        int halfOfHeight = (int) Math.ceil(height * (double)getCxCell() / (double)getCyCell());

        Point answer = new Point();


        if(halfOfHeight > screenSize.x)
        {
            answer.x = screenSize.x;
            answer.y = answer.x * 2;
        }
        else
        {
            answer.x = halfOfHeight;
            answer.y = height;
        }


        return answer;
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
        for(int x = 0; x < getCxCell(); x++)
        {
            for (int y = 0; y < getCyCell(); y++)
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        for(int x = 0; x < getCxCell(); x++)
        {
            for(int y = 0; y < getCyCell(); y++)
            {
                LabyrinthCell labyrinthCell = getCell(x, y);
                if(labyrinthCell != null) labyrinthCell.onCellSize((int) getOneCellSize());
            }
        }
    }

    public Point getCellPosition(LabyrinthCell cell)
    {
        Point answer = new Point();
        for(int x = 0; x < getCxCell(); x++)
        {
            for (int y = 0; y < getCyCell(); y++)
            {
                if(cell == getCell(x, y))
                {
                    answer.set(x, y);
                }
            }
        }

        return answer;
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
            //Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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

    public void endLevel()
    {
        getCharacter().setCoordinates(getStartCell().getLabyrinthPosition());
        cellsConstructor();
    }

    public LabyrinthCell getStartCell()
    {
        return startCell;
    }

    public void setStartCell(LabyrinthCell startCell)
    {
        this.startCell = startCell;
    }

    public Character getCharacter() {
        return character;
    }
}
