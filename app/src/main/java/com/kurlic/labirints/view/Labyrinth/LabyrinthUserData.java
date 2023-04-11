package com.kurlic.labirints.view.Labyrinth;
import java.io.Serializable;

public class LabyrinthUserData implements Serializable
{
    String userName;
    int cFinishedLevels;
    long minTime;

    public LabyrinthUserData(String userName, int cLabyrinthFinished, long minTime)
    {
        this.userName = userName;
        this.cFinishedLevels = cLabyrinthFinished;
        this.minTime = minTime;
    }
    public LabyrinthUserData()
    {
        this.userName = "";
        this.cFinishedLevels = 0;
        this.minTime = 0;
    }

    public String getUserName()
    {
        return userName;
    }

    public int getCLabyrinthFinished()
    {
        return cFinishedLevels;
    }

    public long getMinTime()
    {
        return minTime;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setCFinishedLevels(int cFinishedLevels)
    {
        this.cFinishedLevels = cFinishedLevels;
    }
    public void newLevelWasFinished()
    {
        cFinishedLevels++;
    }

    public void setMinTime(long minTime)
    {
        this.minTime = minTime;
    }

    public void checkAndSetMinTime(long newMinTime)
    {
        if(newMinTime < minTime || minTime == 0)
        {
            minTime = newMinTime;
        }
    }
}
