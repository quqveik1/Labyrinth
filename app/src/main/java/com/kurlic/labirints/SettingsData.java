package com.kurlic.labirints;

import java.io.Serializable;

public class SettingsData implements Serializable
{
    private static String theme = "";

    public static String getTheme()
    {
        return theme;
    }

    public static void setTheme(String theme)
    {
        SettingsData.theme = theme;
    }
}
