package com.kurlic.labirints.view.Labyrinth.Cells;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.Character;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class FinishCell extends LabyrinthCell
{
    Bitmap bm;
    Bitmap bmSized;

    public FinishCell(LabyrinthView labyrinthView, int x, int y) {
        super(labyrinthView, x, y);
        try {
            bm = BitmapFactory.decodeResource(getLabyrinthView().getResources(), R.drawable.finishcell);
        }
        catch (Exception e)
        {
            //if(labyrinthView != null) if(labyrinthView.getContext() != null) Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCellSize(@NonNull int newSize)
    {
        super.onCellSize(newSize);
        try
        {
            bmSized = Bitmap.createScaledBitmap(bm, newSize, newSize, true);
        }
        catch (Exception e)
        {
            //if(labyrinthView != null) if(labyrinthView.getContext() != null) Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect) {

        canvas.drawBitmap(bmSized, rect.left, rect.top, paint);
        super.draw(canvas, paint, rect);


    }

    @Override
    public boolean onEnter(@NonNull Character character) {
        //Toast.makeText(getLabyrinthView().getContext(), "Вы успешно прошли игру", Toast.LENGTH_SHORT).show();
        getLabyrinthView().finishLevel();
        return false;
    }
}
