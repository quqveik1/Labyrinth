package com.kurlic.labirints.view.Labyrinth;

import android.widget.TextView;

import com.kurlic.labirints.Fragments.MainGameFragment;
import com.kurlic.labirints.MainActivity;
import com.kurlic.labirints.SharedData;

public class TimeThread extends Thread {
    TextView timeTextView;
    long elapsedTime;
    boolean needToWork = true;
    boolean needToPause = false;
    int oneCircleTime = 10;
    MainGameFragment mainActivity;

    TimeThread(MainGameFragment mainActivity) {
        super();
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        super.run();
        while (needToWork) {
            String textTime = SharedData.timeInMSToString(elapsedTime);
            mainActivity.updateTimerText(textTime);
            elapsedTime += oneCircleTime;
            try {
                Thread.sleep(oneCircleTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (needToPause) {
            }
            ;

        }
    }

    public void reset() {
        elapsedTime = 0;
    }

    public void pause() {needToPause = true;}

    public void resumeWork() {needToPause = false;}


    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
