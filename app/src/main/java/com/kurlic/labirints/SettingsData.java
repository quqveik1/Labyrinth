package com.kurlic.labirints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class SettingsData implements Serializable
{
    private String theme;
    boolean firstLaunch = false;

    public SettingsData(@NonNull AppCompatActivity activity)
    {
        theme = activity.getResources().getString(R.string.optionSystemTheme);
        wasFirstLaunch();
    }

    public boolean wasFirstLaunch()
    {
        return firstLaunch;
    }

    public void setFirstLaunch(boolean firstLaunch)
    {
        this.firstLaunch = firstLaunch;
    }

    public String getTheme()
    {
        return theme;
    }

    public void setTheme(String theme)
    {
        this.theme = theme;
    }
}
