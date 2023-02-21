package com.kurlic.labirints.view.Labyrinth.Cells;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class StartCell extends LabyrinthCell
{

    Bitmap bmSized;

    public StartCell(LabyrinthView labyrinthView, int x, int y)
    {
        super(labyrinthView, x, y);
        try {
            bm = BitmapFactory.decodeResource(getLabyrinthView().getResources(), R.drawable.startcell);
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

    }

}
