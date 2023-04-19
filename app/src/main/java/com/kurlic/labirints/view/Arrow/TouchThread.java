package com.kurlic.labirints.view.Arrow;

import android.graphics.Point;
import android.widget.Toast;

import com.kurlic.labirints.view.Labyrinth.Character.Character;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class TouchThread extends Thread{
    private LabyrinthView labyrinthView;
    private Point moveDelta;
    private int oneMoveTimeDelta;
    private boolean needToRun = true;


    public TouchThread(int oneMoveTimeDelta, Point moveDelta) {
        super();
        this.oneMoveTimeDelta = oneMoveTimeDelta;
        this.moveDelta = moveDelta;
    }

    @Override
    public void run() {
        super.run();
        try {

            while (needToRun) {
                if (labyrinthView != null) {
                    Character character = labyrinthView.getCharacter();
                    try {
                        character.move(moveDelta);
                    } catch (Exception e) {
                        Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                    try {
                        sleep(oneMoveTimeDelta);
                    } catch (InterruptedException e) {
                        Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(labyrinthView.getContext(), e.toString(), Toast.LENGTH_LONG).show();

        }

    }

    public boolean isNeedToRun() {
        return needToRun;
    }

    public void setNeedToRun(boolean needToRun) {
        this.needToRun = needToRun;
    }

    public void setLabyrinthView(LabyrinthView labyrinthView) {
        this.labyrinthView = labyrinthView;
    }
}
