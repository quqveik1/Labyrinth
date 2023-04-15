package com.kurlic.labirints;

import com.kurlic.labirints.view.Labyrinth.LabyrinthUserData;

public class SharedData
{
    static private LabyrinthUserData labyrinthUserData;

    public static LabyrinthUserData getLabyrinthUserData()
    {
        return labyrinthUserData;
    }

    public static void setLabyrinthUserData(LabyrinthUserData labyrinthUserData)
    {
        SharedData.labyrinthUserData = labyrinthUserData;
    }

    public static String timeInMSToString(long time)
    {
        String textTime = "";
        int seconds = (int) (time / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        seconds = seconds % 60;
        minutes = minutes % 60;
        if(hours != 0)
        {
            textTime = String.format("%02d:", hours);
        }
        textTime += String.format("%02d:%02d", minutes, seconds);
        return textTime;
    }
}
