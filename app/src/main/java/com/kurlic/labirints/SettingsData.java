package com.kurlic.labirints;

import java.io.Serializable;

public class SettingsData implements Serializable
{
    private String theme = "";

    public String getTheme()
    {
        return theme;
    }

    public void setTheme(String theme)
    {
        this.theme = theme;
    }
}
