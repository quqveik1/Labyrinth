package com.kurlic.labirints.view.Labyrinth.Cells;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kurlic.labirints.AppConstants;
import com.kurlic.labirints.R;
import com.kurlic.labirints.view.Labyrinth.Character.Character;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class FinishCell extends LabyrinthCell {
    Bitmap bm;
    Bitmap bmSized;

    public FinishCell(LabyrinthView labyrinthView, int x, int y) {
        super(labyrinthView, x, y);
        try {
            bm = BitmapFactory.decodeResource(getLabyrinthView().getResources(), R.drawable.finishcell);
        } catch (Exception e) {
            logException(e);
        }
    }

    @Override
    public void onCellSize(@NonNull int newSize) {
        super.onCellSize(newSize);
        try {
            bmSized = Bitmap.createScaledBitmap(bm, newSize, newSize, true);
        } catch (Exception e) {
            logException(e);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull Paint paint, @NonNull Rect rect) {
        canvas.drawBitmap(bmSized, rect.left, rect.top, paint);
        super.draw(canvas, paint, rect);
    }

    @Override
    public boolean onEnter(@NonNull Character character) {
        getLabyrinthView().finishLevel();
        return false;
    }
}
