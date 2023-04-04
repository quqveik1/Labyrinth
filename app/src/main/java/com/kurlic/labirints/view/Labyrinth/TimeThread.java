package com.kurlic.labirints.view.Labyrinth;

import android.widget.TextView;

public class TimeThread extends Thread {
    TextView timeTextView;
    int elapsedTime;
    boolean needToWork = true;
    boolean needToPause = false;
    int oneCircleTime = 100;

    TimeThread(TextView timeTextView)
    {
        super();
        this.timeTextView = timeTextView;
    }

    @Override
    public void run()
    {
        super.run();
        while(needToWork)
        {
            String textTime;
            int seconds = (int) (elapsedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            textTime = String.format("%02d:%02d", minutes, seconds);
            timeTextView.setText(textTime);
            elapsedTime += oneCircleTime;
            try
            {
                Thread.sleep(oneCircleTime);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            while(needToPause) {};

        }
    }
    public void reset()
    {
        elapsedTime = 0;
    }
    public void pause(){needToPause = true;}
    public void resumeWork(){needToPause = false;}

}
